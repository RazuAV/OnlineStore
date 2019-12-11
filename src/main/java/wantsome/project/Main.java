package wantsome.project;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Example of main class for a web application (using Javalin framework)
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        setup();
        startWebServer();
    }

    private static void setup() {
        //create and configure all needed resources (like db tables, sample data, etc)
    }

    private static void startWebServer() {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH); //location of static resources (like images, .css ..), relative to /resources dir
            config.enableDevLogging(); //extra logging, for development/debug
        }).start();

        //set default page
        app.get("/", ctx -> ctx.redirect("/main"));

        //configure all routes
        app.get("/main", Main::getMainWebPage);

        logger.info("Server started: http://localhost:" + app.port());
    }

    //example of returning a web page
    private static void getMainWebPage(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("serverTime", new Date().toString());
        ctx.render("main.vm", model);
    }
}
