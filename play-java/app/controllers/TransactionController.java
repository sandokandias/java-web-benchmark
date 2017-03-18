package controllers;

import model.Transaction;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class TransactionController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public TransactionController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        Form<Transaction> payload = formFactory.form(Transaction.class).bind(request().body().asJson());
        if (payload.hasErrors()) {
            return badRequest(payload.errorsAsJson());
        }
        Transaction transaction = payload.get();
        transaction.setId(UUID.randomUUID().toString());
        return created(Json.toJson(transaction));
    }

}
