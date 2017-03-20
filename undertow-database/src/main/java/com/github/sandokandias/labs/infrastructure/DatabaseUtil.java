package com.github.sandokandias.labs.infrastructure;

import com.github.sandokandias.labs.model.Transaction;
import com.github.sandokandias.labs.model.TransactionItem;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

public final class DatabaseUtil {

    private static final String INSERT_TRANSACTION = "insert into transactions (id, amount, description) values (:id, :amount, :description)";
    private static final String INSERT_ITEM = "insert into transaction_items (transaction_id, code, name, price) values (:transaction_id, :code, :name, :price)";

    private Sql2o sql2o;

    private DatabaseUtil() {
        HikariConfig config = new HikariConfig("/database.properties");
        HikariDataSource ds = new HikariDataSource(config);
        this.sql2o = new Sql2o(ds);
    }

    public static DatabaseUtil newInstance() {
        return new DatabaseUtil();
    }

    public void persistTransaction(Transaction transaction) {
        try (Connection con = sql2o.beginTransaction()) {
            con.createQuery(INSERT_TRANSACTION)
                    .addParameter("id", transaction.getId())
                    .addParameter("amount", transaction.getAmount())
                    .addParameter("description", transaction.getDescription())
                    .executeUpdate();

            Query queryItems = con.createQuery(INSERT_ITEM);
            for (int i = 0; i < transaction.getItems().size(); i++) {
                TransactionItem transactionItem = transaction.getItems().get(i);
                queryItems
                        .addParameter("transaction_id", transaction.getId())
                        .addParameter("code", transactionItem.getCode())
                        .addParameter("name", transactionItem.getName())
                        .addParameter("price", transactionItem.getPrice())
                        .addToBatch();
            }
            queryItems.executeBatch();
            con.commit();
        }


    }


}
