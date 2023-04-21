package wantsome.online_store.web.controllers.cart;

import io.javalin.http.Context;
import wantsome.online_store.db.orders.OrdersDao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CardPaymentController {

    public static void showCardPaymentPageSuccesfully(Context ctx) {
        renderCardPaymentPage(ctx, "Your order has been placed succesfuly!");
    }

    public static void renderCardPaymentPage(Context ctx, String message) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        ctx.render("/secure/cardpayment.vm", model);
        if ("POST".equals(ctx.req().getMethod())) {
            try {
                OrdersDao.closeOrder(CartController.getCurrentOrderId(ctx));
            } catch (SQLException exception) {
                throw new RuntimeException("Failed to close order" + exception.getMessage());
            }
        }
    }

}
