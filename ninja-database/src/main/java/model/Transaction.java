package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
public class Transaction {
    @Id
    private String id;
    private BigDecimal amount;
    private String description;
    @ElementCollection(targetClass = TransactionItem.class)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TransactionItem> items;
}
