package com.github.sandokandias.labs.infrastructure;

import com.github.sandokandias.labs.model.Transaction;
import com.github.sandokandias.labs.model.TransactionItem;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

public class Sql2oTransaction {

    public static final String INSERT_TRANSACTION = "insert into transactions (id, amount, description) values (:id, :amount, :description)";
    public static final String INSERT_ITEM = "insert into transaction_items (transaction_id, code, name, price) values (:transaction_id, :code, :name, :price)";

    private Sql2o sql2o;

    private Sql2oTransaction() {
        HikariConfig config = new HikariConfig("/database.properties");
        HikariDataSource ds = new HikariDataSource(config);
        this.sql2o = new Sql2o(ds);
    }

    public static Sql2oTransaction newInstance() {
        return new Sql2oTransaction();
    }

    public void save(Transaction transaction) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery(INSERT_TRANSACTION)
                    .addParameter("id", transaction.getId())
                    .addParameter("amount", transaction.getAmount())
                    .addParameter("description", transaction.getDescription())
                    .executeUpdate();
            conn.commit();

            Query queryInsertItems = conn.createQuery(INSERT_ITEM);
            for (int i = 0; i < transaction.getItems().size(); i++) {
                TransactionItem transactionItem = transaction.getItems().get(i);
                queryInsertItems.addParameter("transaction_id", transaction.getId())
                        .addParameter("code", transactionItem.getCode())
                        .addParameter("name", transactionItem.getName())
                        .addParameter("price", transactionItem.getPrice())
                        .addToBatch();
            }
            queryInsertItems.executeBatch();
            conn.commit();
        }
    }

}
