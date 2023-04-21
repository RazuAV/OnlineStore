package wantsome.online_store.web.controllers.users;

import io.javalin.http.Context;
import wantsome.online_store.db.clients.ClientsDao;
import wantsome.online_store.db.orders.OrdersDao;
import wantsome.online_store.db.orders.OrdersDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilePageController {

    public static void showProfilePage(Context ctx) {
        renderProfilePage(ctx);
    }

    public static void renderProfilePage(Context ctx) {
        Map<String, Object> model = new HashMap<>();

        try {
            String name = ClientsDao.getClientsById(UsersController.getCurrentClientId(ctx)).get().getName();
            List<OrdersDto> ordersDtoList = OrdersDao.getallClosedByClientId(UsersController.getCurrentClientId(ctx));
            model.put("ordersList", ordersDtoList);
            model.put("name", name);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get orders list" + e.getMessage());
        }
        ctx.render("/secure/profilepage.vm", model);
    }
}
