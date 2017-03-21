CREATE TABLE IF NOT EXISTS transactions (
    id varchar(36) not null,
    amount numeric(19,2) not null,
    description varchar(100),
    PRIMARY KEY (id)
) ENGINE=INNODB;


CREATE TABLE IF NOT EXISTS transaction_items (
     id MEDIUMINT NOT NULL AUTO_INCREMENT,
     transaction_id varchar(36) not null,
     code varchar(100) NOT NULL,
     name varchar(100) NOT NULL,
     price numeric(19,2) not null,
     PRIMARY KEY (id),
     INDEX idx_transaction_id (transaction_id),
     FOREIGN KEY (transaction_id)
        REFERENCES transactions(id)
        ON DELETE CASCADE
) ENGINE=INNODB;