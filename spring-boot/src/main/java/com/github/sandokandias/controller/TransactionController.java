package com.github.sandokandias.controller;

import com.github.sandokandias.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("/")
public class TransactionController {

    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction create(@RequestBody Transaction transaction) {
        transaction.setId(UUID.randomUUID().toString());
        return transaction;
    }

}
