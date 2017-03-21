package conf;

import controllers.ApplicationController;
import ninja.Router;
import ninja.application.ApplicationRoutes;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {
        router.POST().route("/").with(ApplicationController::create);
    }

}
