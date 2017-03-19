package com.github.sandokandias.model;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int i) throws SQLException {
        String transactionId = rs.getString("transaction_id");
        String transactionDescription = rs.getString("transaction_description");
        BigDecimal transactionAmount = rs.getBigDecimal("transaction_amount");

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setDescription(transactionDescription);
        transaction.setAmount(transactionAmount);

        return transaction;
    }
}
