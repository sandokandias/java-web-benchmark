package com.github.sandokandias.labs;


import io.undertow.Undertow;

public class UndertowServer {

    public static void main(String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new TransactionHandler()).build();
        server.start();
    }

}
