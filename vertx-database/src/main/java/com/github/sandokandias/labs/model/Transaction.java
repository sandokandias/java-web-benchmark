package com.github.sandokandias.labs.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class Transaction {
    private String id;
    private BigDecimal amount;
    private String description;
    private List<TransactionItem> items;
}
