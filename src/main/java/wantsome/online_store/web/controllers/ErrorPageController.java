package wantsome.online_store.web.controllers;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ErrorPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorPageController.class);

    public static void showErrorPage(Exception exception, Context ctx) {
        // prepare a nice page as response for end user (with info and link to redirect to home page)
        Map<String, Object> model = new HashMap<>();
        model.put("errorMsg", exception.getMessage());
        ctx.render("static/errorPage.vm", model);

        // but useful here to also write the exception details to log, for later debug by developers
        LOGGER.error("An unexpected exception occurred: ", exception);
    }
}
