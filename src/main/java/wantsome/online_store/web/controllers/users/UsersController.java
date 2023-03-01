package wantsome.online_store.web.controllers.users;

import io.javalin.http.Context;
import wantsome.online_store.db.clients.ClientsDao;
import wantsome.online_store.db.clients.ClientsDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static wantsome.project.OnlineStore.*;

public class UsersController {


    public static void checkUserIsLoggedIn(Context ctx) {

        if (isRestrictedPage(ctx.path()) //only some pages should be checked (the rest are always allowed, like /login or static resources)
                && ctx.sessionAttribute("currentUser") == null) { //no user on session (not logged in yet, or has expired)
            ctx.redirect(LOGIN_PAGE);
        }
    }

    private static boolean isRestrictedPage(String path) {
        return path.startsWith(SECURE_PREFIX);
    }


    public static void handleLoginRequest(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        boolean isValidUser = ClientsDao.clientLogin(email, password);
        try {
            if (isValidUser) {
                ctx.sessionAttribute("currentUserEmail", email);
                ctx.sessionAttribute("currentUser", ClientsDao.getClientsByEmail(email).get().getName());//save user on session
                ctx.redirect(HOME_PAGE);
            } else {
                renderLoginPage(ctx, email, "Invalid email or password!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get client name!" + " " + e.getMessage());
        }
    }

    public static void showLoginPage(Context ctx) {
        renderLoginPage(ctx, null, null);
    }

    private static void renderLoginPage(Context ctx, String email, String error) {
        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("error", error);
        ctx.render("static/loginpage.vm", model);
    }

    public static void renderRegisterPage(Context ctx, String email, String error) {
        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("error", error);
        ctx.render("static/registerpage.vm", model);
    }

    public static void showRegisterPage(Context ctx) {
        renderRegisterPage(ctx, null, null);
    }

    public static void handleRegisterRequest(Context ctx) throws SQLException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String name = ctx.formParam("name");
        String address = ctx.formParam("address");

        boolean succesfulyRegistered = ClientsDao.addClient(new ClientsDto(email, password, name, address));

        if (succesfulyRegistered) {
            ctx.sessionAttribute("currentUserEmail", email);
            ctx.sessionAttribute("currentUser", name);//save user on session
            ctx.redirect(LOGIN_PAGE);
        } else {
            renderRegisterPage(ctx, email, "This email is already registered!");
        }
    }

    public static void addUserInfoToModel(Context ctx, Map<String, Object> model) {
        model.put("currentUser", ctx.sessionAttribute("currentUser"));
    }
    public static Integer getCurrentClientId(Context ctx) {
        String currentUserEmail = ctx.sessionAttribute("currentUserEmail");
        try {
            Optional<ClientsDto> client = ClientsDao.getClientsByEmail(currentUserEmail);
            return client.map(ClientsDto::getId).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get client id!" + " " + e.getMessage());
        }
    }
}
