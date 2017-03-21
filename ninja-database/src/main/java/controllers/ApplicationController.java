package controllers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import model.Transaction;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.Router;

import javax.persistence.EntityManager;
import java.util.UUID;

@Singleton
public class ApplicationController {

    @Inject
    Provider<EntityManager> entityManagerProvider;

    @Inject
    Router router;

    @Inject
    Context context;

    @Transactional
    public Result create(Transaction transaction) {
        String id = UUID.randomUUID().toString();
        transaction.setId(id);
        transaction.getItems().stream().forEach(transactionItem -> {
            transactionItem.setTransactionId(id);
        });
        entityManagerProvider.get().persist(transaction);
        return Results.json().render(transaction);
    }
}
