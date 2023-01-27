package wantsome.online_store.web.controllers.home;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class HomeController {

    public static void showHomePage(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        ctx.render("/secure/home.vm", model);
    }
}
