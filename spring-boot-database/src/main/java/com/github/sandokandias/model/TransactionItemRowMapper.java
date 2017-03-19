package com.github.sandokandias.model;


import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionItemRowMapper implements RowMapper<TransactionItem> {

    @Override
    public TransactionItem mapRow(ResultSet rs, int i) throws SQLException {
        String itemCode = rs.getString("item_code");
        String itemName = rs.getString("item_name");
        BigDecimal itemPrice = rs.getBigDecimal("item_price");

        TransactionItem transactionItem = new TransactionItem();
        transactionItem.setCode(itemCode);
        transactionItem.setName(itemName);
        transactionItem.setPrice(itemPrice);

        return transactionItem;
    }
}
