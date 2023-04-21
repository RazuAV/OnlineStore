package wantsome.online_store.cart;

import wantsome.online_store.db.orderItem.OrderItemDao;
import wantsome.online_store.db.orderItem.OrderItemDto;
import wantsome.online_store.db.orders.OrdersDao;
import wantsome.online_store.db.orders.OrdersDto;
import wantsome.online_store.db.products.ProductsDao;
import wantsome.online_store.db.products.ProductsDto;

import java.sql.SQLException;
import java.util.*;

public class CartClass {

    private static List<OrderItemDto> orderItemsList = new ArrayList<>();
    private static int orderId;
    private static double totalPrice;

    public CartClass(int orderId) {
        this.orderId = orderId;
    }

    public static List<OrderItemDto> getOrderItemsList() {
        return orderItemsList;
    }

    public static void setOrderItemsList(List<OrderItemDto> orderItemsList) {
        CartClass.orderItemsList = orderItemsList;
    }


    public static int getOrderId() {
        return orderId;
    }

    public static void setOrderId(int orderId) {
        CartClass.orderId = orderId;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }

    public static void setTotalPrice(double totalPrice) {
        CartClass.totalPrice = totalPrice;
    }
// adding a product to the cart


    public static void addOrderItemToList(int clientId, int productId, int quantity) {
        try {
            Optional<OrdersDto> ordersDtoOptional = OrdersDao.getCurrentForClient(clientId);
            Optional<ProductsDto> productsDtoOptional = ProductsDao.getProductsById(productId);

            OrdersDto order = ordersDtoOptional.orElseGet(() -> createNewOrder(clientId));
            ProductsDto product = productsDtoOptional.orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItemDto orderedItem = new OrderItemDto(
                    order.getId(),
                    productId,
                    product.getproductType(),
                    product.getDescription(),
                    product.getPrice(),
                    quantity
            );

            if (OrderItemDao.insertOrderItem(orderedItem)) {
                if (!orderItemExists(orderItemsList, productId)) {
                    orderItemsList.add(orderedItem);
                }
            } else {
                System.out.println("No orderItem with such Id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add OrderItem to list!" + " " + e.getMessage());
        }
    }

    private static OrdersDto createNewOrder(int clientId) {
        try {
            if (!OrdersDao.addOrder(new OrdersDto(clientId))) {
                throw new RuntimeException("Failed to create new order");
            }

            return OrdersDao.getCurrentForClient(clientId).orElseThrow(() -> new RuntimeException("Order not found"));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create new order" + e.getMessage());
        }
    }

    private static boolean orderItemExists(List<OrderItemDto> orderItems, int productId) {
        return orderItems.stream().anyMatch(item -> item.getProductId() == productId);
    }

    public static void updateTotalPrice(List<OrderItemDto> orderItemsList) {

        double price = 0;
        for (OrderItemDto orderItemDto : orderItemsList) {
            price += orderItemDto.getProductPrice() * orderItemDto.getQuantity();
        }
        setTotalPrice(price);
    }

    public static void getListForExistingOrder(int orderId) {

        List<OrderItemDto> orderItemDtoList = OrderItemDao.getAllItemsOrderedForASpecificOrderId(orderId);
        CartClass.setOrderItemsList(orderItemDtoList);

    }

    public static void removeProductFromCart(int orderId, int productId) {
        List<OrderItemDto> orderItemDtoListCopy = new ArrayList<>(orderItemsList);
        for (OrderItemDto orderItemDto : orderItemDtoListCopy) {
            if (orderItemDto.getOrderId() == orderId && orderItemDto.getProductId() == productId) {
                orderItemsList.remove(orderItemDto);
                OrderItemDao.deleteItem(productId, orderId);
            }
        }
    }
}
