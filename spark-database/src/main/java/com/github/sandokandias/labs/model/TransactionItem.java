package com.github.sandokandias.labs.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionItem {
    private String code;
    private String name;
    private BigDecimal price;
}

