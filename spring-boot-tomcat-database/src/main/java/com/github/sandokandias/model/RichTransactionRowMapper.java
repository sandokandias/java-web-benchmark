package com.github.sandokandias.model;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RichTransactionRowMapper implements ResultSetExtractor<List<Transaction>> {

    private final TransactionRowMapper transactionRowMapper = new TransactionRowMapper();
    private final TransactionItemRowMapper transactionItemRowMapper = new TransactionItemRowMapper();

    @Override
    public List<Transaction> extractData(ResultSet rs) throws SQLException, DataAccessException {
        int rowNum = 0;
        Map<String, Transaction> transactions = new HashMap<>();
        while (rs.next()) {
            rowNum++;
            String id = rs.getString("transaction_id");
            Transaction transaction = transactions.get(id);
            if (Objects.isNull(transaction)) {
                transaction = transactionRowMapper.mapRow(rs, rowNum);
                transactions.put(id, transaction);
            }
            TransactionItem item = transactionItemRowMapper.mapRow(rs, rowNum);
            transaction.addItem(item);
        }
        return transactions.values().stream().collect(Collectors.toList());
    }
}
