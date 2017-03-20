package com.github.sandokandias.labs;

import com.github.sandokandias.labs.infrastructure.DatabaseUtil;
import com.github.sandokandias.labs.model.Transaction;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.UUID;

public class VertxDatabaseApp extends AbstractVerticle {

    private JDBCClient jdbc;

    @Override
    public void start(Future<Void> future) {
        jdbc = JDBCClient.createShared(vertx, config(), "VertxDatabase-App-DS");

        Router router = Router.router(vertx);
        router.route("/*").handler(BodyHandler.create());

        router.post("/").handler(context -> {
            Transaction transaction = Json.decodeValue(context.getBodyAsString(), Transaction.class);
            transaction.setId(UUID.randomUUID().toString());
            persistTransaction(transaction, context);
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(result.cause());
                            }
                        }
                );
    }

    private void persistTransaction(Transaction transaction, RoutingContext context) {

        jdbc.getConnection(conn -> {
            if (conn.failed()) {
                System.err.println(conn.cause().getMessage());
                return;
            }

            DatabaseUtil.startTx(conn.result(), beginTrans -> {
                DatabaseUtil.insertTransaction(conn.result(), transaction, resTransaction -> {
                    DatabaseUtil.insertItem(conn.result(), transaction, resItem -> {
                        DatabaseUtil.endTx(conn.result(), commitTrans -> {
                            conn.result().close(done -> {
                                if (done.failed()) {
                                    throw new RuntimeException(done.cause());
                                }
                                context.response()
                                        .setStatusCode(201)
                                        .putHeader("content-type", "application/json")
                                        .end(Json.encode(transaction));
                            });
                        });
                    });
                });
            });
        });
    }
}
