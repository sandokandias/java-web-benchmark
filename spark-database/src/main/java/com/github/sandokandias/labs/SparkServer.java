package com.github.sandokandias.labs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sandokandias.labs.infrastructure.Sql2oTransaction;
import com.github.sandokandias.labs.model.Transaction;
import org.sql2o.Sql2o;

import java.util.UUID;

import static spark.Spark.post;

public class SparkServer {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        Sql2o sql2o = new Sql2o("jdbc:mysql://localhost:3306/labs-java?useSSL=false", "labs-java", "labs-java");
        Sql2oTransaction sql2oTransaction = new Sql2oTransaction(sql2o);

        post("/", (request, response) -> {
            Transaction transaction = mapper.readValue(request.body(), Transaction.class);
            transaction.setId(UUID.randomUUID().toString());
            sql2oTransaction.save(transaction);
            return mapper.writeValueAsString(transaction);
        });
    }
}