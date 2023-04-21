package wantsome.online_store.web.controllers.cart;

import io.javalin.http.Context;
import wantsome.online_store.cart.CartClass;
import wantsome.online_store.db.orderItem.OrderItemDao;
import wantsome.online_store.db.orderItem.OrderItemDto;
import wantsome.online_store.db.orders.OrdersDao;
import wantsome.online_store.db.orders.OrdersDto;
import wantsome.online_store.db.products.ProductsDao;
import wantsome.online_store.web.controllers.ShopController.ShopController;
import wantsome.online_store.web.controllers.users.UsersController;
import wantsome.project.OnlineStore;


import java.sql.SQLException;
import java.util.*;

public class CartController {

    public static void showCartPage(Context ctx) {
        renderCartPage(ctx);
    }

    public static void renderCartPage(Context ctx) {
        Map<String, Object> model = new HashMap<>();

        int orderId = getCurrentOrderId(ctx);
        CartClass.setOrderId(orderId);
        if (!checkIfOrderIsNotClosed(ctx, orderId)) {
            CartClass.getListForExistingOrder(orderId);
        }
        List<OrderItemDto> orderItems = CartClass.getOrderItemsList();
        CartClass.updateTotalPrice(orderItems);
        double totalPrice = CartClass.getTotalPrice();
        model.put("totalPrice", totalPrice);
        model.put("orderItems", orderItems);
        model.put("orderId", orderId);
        ctx.render("/secure/cartpage.vm", model);

    }

    public static void handleCheckOut(Context ctx) {

        String option = ctx.formParam("option");

        if (option.equals("cash")) {
            try {
                OrdersDao.closeOrder(getCurrentOrderId(ctx));
                renderCheckOut(ctx, "Your order has been placed.");
            } catch (SQLException exception) {
                throw new RuntimeException("Failed to close order!" + exception.getMessage());
            }
        }
        if (option.equals("card")) {
            ctx.render("/secure/cardpayment.vm");
        }
    }

    public static void showCheckOut(Context ctx) {
        renderCheckOut(ctx, null);
    }

    public static void renderCheckOut(Context ctx, String message) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        ctx.render("/secure/checkout.vm", model);
    }

    public static void addProductToCart(Context ctx) {
        int clientId = UsersController.getCurrentClientId(ctx);
        int productId = Integer.parseInt(ctx.formParam("productId"));
        CartClass.addOrderItemToList(clientId, productId, 1);
        CartClass.updateTotalPrice(CartClass.getOrderItemsList());
        try {
            ProductsDao.updateStock(productId, 1);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating stock");
        }
        ctx.render("/secure/shoppage.vm");
        ShopController.handleSearchRequests(ctx);


    }

    public static int getCurrentOrderId(Context ctx) {
        try {
            Optional<OrdersDto> ordersDtoOptional = OrdersDao.getCurrentForClient(UsersController.getCurrentClientId(ctx));
            if (ordersDtoOptional.isPresent()) {
                int orderId = ordersDtoOptional.get().getId();
                return orderId;
            } else {
                OrdersDto ordersDto = new OrdersDto(UsersController.getCurrentClientId(ctx));
                int orderId = ordersDto.getId();
                return orderId;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get currentOrderId" + " " + e.getMessage());
        }
    }

    public static boolean checkIfOrderIsNotClosed(Context ctx, int orderId) {
        try {
            boolean isClosed = false;
            int clientId = UsersController.getCurrentClientId(ctx);
            List<OrdersDto> ordersDtoList = OrdersDao.getallClosedByClientId(clientId);
            for (OrdersDto order : ordersDtoList) {
                if (order.getId() == orderId) {
                    isClosed = true;
                }
            }
            return isClosed;
        } catch (SQLException e) {
            throw new RuntimeException("Error at checking  if order is not closed" + e.getMessage());
        }
    }

    public static void deleteProductFromCart(Context ctx) {
        int orderId = getCurrentOrderId(ctx);
        int productId = ctx.queryParamAsClass("id", Integer.class).get();
        CartClass.removeProductFromCart(orderId, productId);
        try {
            ProductsDao.updateStock(productId, -1);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating stock" + e.getMessage());
        }
        ctx.redirect(OnlineStore.CART_PAGE);
    }

    public static void updateQuantityForAnOrderedItem(Context ctx) {
        int productId = Integer.parseInt(ctx.formParam("productId"));
        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        int quantity = Integer.parseInt(ctx.formParam("quantity"));
        try {
            int actualQuantity = OrderItemDao.getOrderItemByOrderAndId(productId, orderId).get().getQuantity();
            if (actualQuantity >= 1) {
                if (OrderItemDao.updateQuantity(orderId, productId, quantity)) {
                    ProductsDao.updateStock(productId, quantity);
                    ctx.redirect(OnlineStore.CART_PAGE);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating quantity " + " " + e.getMessage());
        }
    }

}
