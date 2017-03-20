package com.github.sandokandias.labs;

import com.github.sandokandias.labs.infrastructure.TransactionHandler;
import io.undertow.Undertow;


public class UndertowDatabaseApp {
    public static void main(String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new TransactionHandler()).build();
        server.start();
    }
}
