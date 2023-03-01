package wantsome.online_store.db.orderItem;

import wantsome.online_store.db.products.ProductType;
import wantsome.online_store.db.service.ConnectionManager;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderItemDao {
    /**
     * Get items ordered for a specific id
     */
    public static Optional<OrderItemDto> getOrderItemByOrderAndId(int productId, int orderId) throws SQLException {
        Optional<OrderItemDto> orderItemDtoOptional = Optional.empty();

        String sql = "SELECT id,orderId,productId,productType,productDescription,productPrice,quantity FROM orderItem WHERE productId = ? AND orderId = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, orderId);

            try (ResultSet orderItemData = ps.executeQuery()) {

                while (orderItemData.next()) {
                    OrderItemDto orderItemDto = new OrderItemDto(
                            orderItemData.getInt("id"),
                            orderItemData.getInt("orderId"),
                            orderItemData.getInt("productId"),
                            ProductType.valueOf(orderItemData.getString("productType")),
                            orderItemData.getString("productDescription"),
                            orderItemData.getDouble("productPrice"),
                            orderItemData.getInt("quantity")
                    );
                    orderItemDtoOptional = Optional.of(orderItemDto);
                }
            }
            return orderItemDtoOptional;
        } catch (SQLException e) {
            throw new RuntimeException("No order with this product id found!" + " " + e.getMessage());
        }
    }

    /**
     * Helping method to get the stock for a specific product
     */
    public static int getStockForAProduct(OrderItemDto orderItemDto) throws SQLException {

        int stock = 0;
        String sql = "SELECT stock from products WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItemDto.getProductId());

            try (ResultSet rs = ps.executeQuery()) {
                stock = rs.getInt("stock");
            }
            return stock;
        } catch (SQLException e) {
            throw new SQLException("Failed to get stock: " + " " + e.getMessage());
        }
    }

    /**
     * Add a new order item only if the stock is not 0 or if its >= quantity
     */
    public static boolean insertOrderItem(OrderItemDto orderItem) throws SQLException {
        Optional<OrderItemDto> orderItemDtoOptional = getOrderItemByOrderAndId(orderItem.getProductId(), orderItem.getOrderId());
        if (getStockForAProduct(orderItem) < orderItem.getQuantity() || getStockForAProduct(orderItem) == 0) {
            System.out.println("Not enough products in stock!");
            return false;
        }
        if (orderItemDtoOptional.isPresent()) {
            return false;
        }

        String sql = "INSERT INTO orderItem VALUES (?, ?, ?, ?,?,?,?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(2, orderItem.getOrderId());
            ps.setInt(3, orderItem.getProductId());
            ps.setString(4, orderItem.getProductType().name());
            ps.setString(5, orderItem.getProductDescription());
            ps.setDouble(6, orderItem.getProductPrice());
            ps.setInt(7, orderItem.getQuantity());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new SQLException("Item could not be inserted!" + " " + e.getMessage());
        }
    }

    /**
     * Delete item from order
     */
    public static boolean deleteItem(int productId, int orderId) {

        String sql = "DELETE FROM orderItem where productId = ? AND orderId = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No item with this product id found!" + " " + e.getMessage());
        }
    }

    /**
     * Update quantity for a specific order item
     */
    public static boolean updateQuantity(int orderId, int productId, int quantity) throws SQLException {

        String sql = "UPDATE orderItem SET quantity = ? WHERE productId = ? AND orderId = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating quantity for item with product id: " + productId + " " + e.getMessage());
        }
    }

    public static List<OrderItemDto> getAllItemsOrderedForASpecificOrderId(int orderId) {
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        String sql = "SELECT id,orderId,productId,productType,productDescription,productPrice,quantity FROM orderItem WHERE orderId = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet orderItemData = ps.executeQuery()) {

                while (orderItemData.next()) {
                    OrderItemDto orderItemDto = new OrderItemDto(
                            orderItemData.getInt("id"),
                            orderItemData.getInt("orderId"),
                            orderItemData.getInt("productId"),
                            ProductType.valueOf(orderItemData.getString("productType")),
                            orderItemData.getString("productDescription"),
                            orderItemData.getDouble("productPrice"),
                            orderItemData.getInt("quantity")
                    );
                    orderItemDtoList.add(orderItemDto);
                }
            }
            return orderItemDtoList;
        } catch (SQLException e) {
            throw new RuntimeException("No order with this product id found!" + " " + e.getMessage());
        }
    }
}
