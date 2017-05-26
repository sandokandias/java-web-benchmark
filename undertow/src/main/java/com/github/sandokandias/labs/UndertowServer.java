package com.github.sandokandias.labs;


import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

public class UndertowServer {

    public static void main(String[] args) {

        RoutingHandler routingHandler = Handlers.routing()
                .get("/", new DefaultHandler())
                .post("/transactions", new TransactionHandler());

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(routingHandler).build();


        server.start();
    }

}
