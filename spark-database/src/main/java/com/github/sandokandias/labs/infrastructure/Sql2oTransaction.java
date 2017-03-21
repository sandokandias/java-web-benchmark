package com.github.sandokandias.labs.infrastructure;

import com.github.sandokandias.labs.model.Transaction;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Sql2oTransaction {

    private Sql2o sql2o;

    public Sql2oTransaction(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public static final String INSERT_TRANSACTION = "insert into transactions (id, amount, description) values (:id, :amount, :description)";
    public static final String INSERT_ITEM = "insert into transaction_items (transaction_id, code, name, price) values (:transaction_id, :code, :name, :price)";

    public void save(Transaction transaction) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery(INSERT_TRANSACTION)
                    .addParameter("id", transaction.getId())
                    .addParameter("amount", transaction.getAmount())
                    .addParameter("description", transaction.getDescription())
                    .executeUpdate();

            transaction.getItems().forEach(transactionItem -> {
                conn.createQuery(INSERT_ITEM)
                        .addParameter("transaction_id", transaction.getId())
                        .addParameter("code", transactionItem.getCode())
                        .addParameter("name", transactionItem.getName())
                        .addParameter("price", transactionItem.getPrice())
                        .executeUpdate();
            });
            conn.commit();
        }
    }

}
