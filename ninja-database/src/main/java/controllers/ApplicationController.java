/**
 * Copyright (C) 2013 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    Provider<EntityManager> entitiyManagerProvider;

    @Inject
    Router router;

    @Inject
    Context context;


    public Result index() {
        return Results.html();
    }

    public Result helloWorldJson() {
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Hello World! Hello Json!";
        return Results.json().render(simplePojo);
    }

    public static class SimplePojo {
        public String content;
    }

    @Transactional
    public Result create(Transaction transaction) {
        String id = UUID.randomUUID().toString();
        transaction.setId(UUID.randomUUID().toString());
        transaction.getItems().stream().forEach(transactionItem -> {
            transactionItem.setTransactionId(id);
        });
        entitiyManagerProvider.get().persist(transaction);
        return Results.json().render(transaction);
    }
}
