package com.github.sandokandias.labs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sandokandias.labs.infrastructure.Sql2oTransaction;
import com.github.sandokandias.labs.model.Transaction;

import java.util.UUID;

import static spark.Spark.post;

public class SparkServer {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        Sql2oTransaction sql2oTransaction = Sql2oTransaction.newInstance();

        post("/", (request, response) -> {
            Transaction transaction = mapper.readValue(request.body(), Transaction.class);
            transaction.setId(UUID.randomUUID().toString());
            sql2oTransaction.save(transaction);
            return mapper.writeValueAsString(transaction);
        });
    }
}