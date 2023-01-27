package wantsome.project;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.rendering.template.JavalinVelocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wantsome.online_store.web.controllers.ShopController.ShopController;
import wantsome.online_store.web.controllers.users.UsersController;
import wantsome.online_store.web.controllers.home.HomeController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Example of main class for a web application (using Javalin framework)
 */
public class OnlineStore {

    public static final String LOGIN_PAGE = "/loginpage";
    public static final String REGISTER_PAGE = "/registerpage";
    // secure pages (can be accesses only after login)
    public static final String SECURE_PREFIX = "/secure";
    public static final String HOME_PAGE = SECURE_PREFIX + "/home";
    public static final String SHOP_PAGE = SECURE_PREFIX + "/shoppage";
    public static final String PROFILE_PAGE = SECURE_PREFIX + "/profilepage";
    public static final String CART_PAGE = SECURE_PREFIX + "/cartpage";
    private static final Logger logger = LoggerFactory.getLogger(OnlineStore.class);

    public static final String DEFAULT_PAGE = HOME_PAGE;

    public static void main(String[] args) {
        //      DatabaseManager.createDatabase();

        JavalinVelocity.init(null); //enable the Velocity rendering engine (to recognize .vm files)
        Javalin.create(config -> {
                    config.staticFiles.add("/static"); //location of static resources (like images, .css ..), relative to /resources dir
                    config.plugins.enableDevLogging(); //extra logging, for development/debug
                    config.showJavalinBanner = false;
                })
                .before(UsersController::checkUserIsLoggedIn)

                .get("/", ctx -> ctx.redirect(DEFAULT_PAGE))
                .get(HOME_PAGE, HomeController::showHomePage)
                .get(LOGIN_PAGE, UsersController::showLoginPage)
                .post(LOGIN_PAGE, UsersController::handleLoginRequest)
                .get(REGISTER_PAGE, UsersController::showRegisterPage)
                .post(REGISTER_PAGE, UsersController::handleRegisterRequest)
                .get(SHOP_PAGE, ShopController::shopWithAllCat)
                .post(SHOP_PAGE, ShopController::handleByCategories)
                .start();

        logger.info("Server has started!");
    }


}