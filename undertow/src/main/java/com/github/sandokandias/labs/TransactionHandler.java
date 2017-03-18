package com.github.sandokandias.labs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class TransactionHandler implements HttpHandler {

    private final ObjectMapper MAPPER = new ObjectMapper();

    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderValues contentType = exchange.getRequestHeaders().get(Headers.CONTENT_TYPE);
        if (contentType.getFirst().equals("application/json")) {
            exchange.getRequestReceiver().receiveFullBytes((e, data) -> {
                        try {
                            Transaction transaction = MAPPER.readValue(data, Transaction.class);
                            transaction.setId(UUID.randomUUID().toString());
                            sendCreated(exchange, transaction);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            sendError(exchange, e1.getMessage());
                        }
                    },
                    (e, exception) -> {
                        exception.printStackTrace();
                        sendError(exchange, exception.getMessage());
                    }
            );
        } else {
            sendError(exchange, "Content-Type not supported.");
        }
    }

    private void sendCreated(HttpServerExchange exchange, Transaction transaction) throws JsonProcessingException {
        byte[] transactionInBytes = MAPPER.writeValueAsBytes(transaction);
        exchange.setStatusCode(201);
        exchange.getResponseSender().send(ByteBuffer.wrap(transactionInBytes));
    }

    private void sendError(HttpServerExchange exchange, String message) {
        exchange.setStatusCode(500);
        exchange.getResponseSender().send(message);
    }
}
