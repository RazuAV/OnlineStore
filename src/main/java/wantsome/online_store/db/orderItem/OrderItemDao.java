package wantsome.online_store.db.orderItem;

import wantsome.online_store.db.service.ConnectionManager;


import java.sql.*;
import java.util.Optional;

public class OrderItemDao {
    /**
     * Get items ordered for a specific id
     */
    public static Optional<OrderItemDto> getOrderItemById(int id) throws SQLException {
        Optional<OrderItemDto> orderItemDtoOptional = Optional.empty();

        String sql = "SELECT id,orderId,productId,quantity FROM orderItem WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet orderItemData = ps.executeQuery()) {

                while (orderItemData.next()) {
                    OrderItemDto orderItemDto = new OrderItemDto(
                            orderItemData.getInt("id"),
                            orderItemData.getInt("orderId"),
                            orderItemData.getInt("productId"),
                            orderItemData.getInt("quantity")
                    );
                    orderItemDtoOptional = Optional.of(orderItemDto);
                }
            }
            return orderItemDtoOptional;
        } catch (SQLException e) {
            throw new RuntimeException("No order with this id found!" + " " + e.getMessage());
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
            ps.setInt(1, orderItemDto.getproductId());

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

        if (getStockForAProduct(orderItem) < orderItem.getQuantity() || getStockForAProduct(orderItem) == 0) {
            System.out.println("Not enough products in stock!");
            return false;
        }

        String sql = "INSERT INTO orderItem VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(2, orderItem.getorderId());
            ps.setInt(3, orderItem.getproductId());
            ps.setInt(4, orderItem.getQuantity());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new SQLException("Item could not be inserted!" + " " + e.getMessage());
        }
    }

    /**
     * Delete item from order
     */
    public static boolean deleteItem(int id) {

        String sql = "DELETE FROM orderItem where id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No item with this id found!" + " " + e.getMessage());
        }
    }

    /**
     * Update quantity for a specific order item
     */
    public static boolean updateQuantity(int id, int quantity) throws SQLException {

        String sql = "UPDATE orderItem SET quantity = ? WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating quantity for item" + id + " " + e.getMessage());
        }
    }
}
