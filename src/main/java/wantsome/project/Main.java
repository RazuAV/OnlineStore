package wantsome.project;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.rendering.template.JavalinVelocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Example of main class for a web application (using Javalin framework)
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        //todo: should also initialize db here (create tables, etc)

        JavalinVelocity.init(null); //enable the Velocity rendering engine (to recognize .vm files)
        Javalin.create(config -> {
                    config.staticFiles.add("/static"); //location of static resources (like images, .css ..), relative to /resources dir
                    config.plugins.enableDevLogging(); //extra logging, for development/debug
                    config.showJavalinBanner = false;
                })
                .get("/", ctx -> ctx.redirect("/main")) //set default page
                .get("/main", Main::getMainWebPage) //set the rest of the pages...
                .start();

        logger.info("Server has started!");
    }

    //example of returning a web page
    private static void getMainWebPage(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("serverTime", LocalDateTime.now());
        ctx.render("main.vm", model);
    }
}
