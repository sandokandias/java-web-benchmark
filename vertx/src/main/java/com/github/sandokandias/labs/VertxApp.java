package com.github.sandokandias.labs;

import com.github.sandokandias.labs.model.Transaction;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.UUID;

public class VertxApp extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        Router router = Router.router(vertx);
        router.route("/*").handler(BodyHandler.create());
        router.post("/").handler(context -> {

            Transaction transaction = Json.decodeValue(context.getBodyAsString(), Transaction.class);
            transaction.setId(UUID.randomUUID().toString());

            context.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json")
                    .end(Json.encode(transaction));
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }
}
