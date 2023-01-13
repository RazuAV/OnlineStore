package wantsome.online_store.db.order_item;

import wantsome.online_store.db.service.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class OrderItemDao {
    /**
     * Get items ordered for a specific id
     */
    public Optional<OrderItemDto> getOrderItemById(int id) throws SQLException {
        Optional<OrderItemDto> orderItemDtoOptional = Optional.empty();

        String sql = "SELECT id,order_id,product_id,quantity FROM order_item WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet orderItemData = ps.executeQuery()) {

                while (orderItemData.next()) {
                    OrderItemDto orderItemDto = new OrderItemDto(
                            orderItemData.getInt("id"),
                            orderItemData.getInt("order_id"),
                            orderItemData.getInt("product_id"),
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
     * Add a new order item
     */
    public boolean insertOrderItem(OrderItemDto orderItem) throws SQLException {
        Optional<OrderItemDto> getOrderItemById = getOrderItemById(orderItem.getId());

        if (getOrderItemById.isPresent()) {
            return false;
        }

        String sql = "INSERT INTO order_item VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItem.getId());
            ps.setInt(2, orderItem.getOrder_id());
            ps.setInt(3, orderItem.getProduct_id());
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
    public boolean deleteItem(int id) throws SQLException {

        String sql = "DELETE FROM order_item where id = ?";

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
    public boolean updateQuantity(int id, int quantity) throws SQLException {

        String sql = "UPDATE order_item SET quantity = ? WHERE id = ?";

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
