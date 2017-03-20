package com.github.sandokandias.labs.infrastructure;

import com.github.sandokandias.labs.model.Transaction;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.stream.Collectors;

public final class DatabaseUtil {

    private DatabaseUtil() {
    }

    public static final String INSERT_TRANSACTION = "insert into transactions (id, amount, description) values (?, ?, ?)";

    public static final String INSERT_ITEM = "insert into transaction_items (transaction_id, code, name, price) values (?, ?, ?, ?)";

    public static void insertTransaction(SQLConnection conn, Transaction transaction, Handler<Void> done) {

        conn.updateWithParams(DatabaseUtil.INSERT_TRANSACTION,
                new JsonArray().add(transaction.getId()).add(transaction.getAmount().toString()).add(transaction.getDescription()), res -> {
                    if (res.failed()) {
                        throw new RuntimeException(res.cause());
                    }

                    done.handle(null);
                });
    }


    public static void insertItem(SQLConnection conn, Transaction transaction, Handler<Void> done) {

        List<JsonArray> batch = transaction.getItems().stream().map(i -> new JsonArray().add(transaction.getId()).add(i.getCode()).add(i.getName()).add(i.getPrice().toString())).collect(Collectors.toList());
        conn.batchWithParams(DatabaseUtil.INSERT_ITEM, batch, res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }

            done.handle(null);
        });
    }

    public static void startTx(SQLConnection conn, Handler<ResultSet> done) {
        conn.setAutoCommit(false, res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }

            done.handle(null);
        });
    }

    public static void endTx(SQLConnection conn, Handler<ResultSet> done) {
        conn.commit(res -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }

            done.handle(null);
        });
    }
}
