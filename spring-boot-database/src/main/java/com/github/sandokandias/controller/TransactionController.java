package com.github.sandokandias.controller;

import com.github.sandokandias.model.RichTransactionRowMapper;
import com.github.sandokandias.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController("/")
public class TransactionController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction create(@RequestBody Transaction transaction) {
        transaction.setId(UUID.randomUUID().toString());
        persistTransaction(transaction);
        return transaction;
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction get(@PathVariable("id") String transactionId) {
        return getTransactionById(transactionId);
    }

    private void persistTransaction(final Transaction transaction) {
        jdbcTemplate.update(
                INSERT_TRANSACTION,
                transaction.getId(), transaction.getAmount(), transaction.getDescription());

        jdbcTemplate.batchUpdate(INSERT_ITEM, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, transaction.getId());
                ps.setString(2, transaction.getItems().get(i).getCode());
                ps.setString(3, transaction.getItems().get(i).getName());
                ps.setBigDecimal(4, transaction.getItems().get(i).getPrice());
            }

            @Override
            public int getBatchSize() {
                return transaction.getItems().size();
            }
        });
    }

    private Transaction getTransactionById(@PathVariable("id") String transactionId) {
        List<Transaction> result = jdbcTemplate.query(GET_TRANSACTION, new RichTransactionRowMapper(), transactionId);
        return result.isEmpty() ? null : result.get(0);
    }


    private static final String INSERT_TRANSACTION = "insert into transactions (id, amount, description) values (?, ?, ?)";

    private static final String INSERT_ITEM = "insert into transaction_items (transaction_id, code, name, price) values (?, ?, ?, ?)";

    private static final String GET_TRANSACTION = "select t.id transaction_id, t.amount transaction_amount, t.description transaction_description, " +
            "i.code item_code, i.name item_name, i.price item_price from transactions t" +
            " inner join transaction_items i on i.transaction_id = t.id" +
            " where t.id = ?";
}
