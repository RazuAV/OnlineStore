package wantsome.online_store.db.orders;


import wantsome.online_store.db.service.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdersDao {
    /**
     * Get orders by their ID
     */
    public Optional<OrdersDto> getOrdersById(int id) throws SQLException {
        Optional<OrdersDto> ordersDtoOptional = Optional.empty();
        String sql = "SELECT id,client_id,fulfill_date,total_price FROM orders WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet ordersData = ps.executeQuery()) {

                while (ordersData.next()) {
                    OrdersDto ordersDto = new OrdersDto(
                            ordersData.getInt("id"),
                            ordersData.getInt("client_id"),
                            ordersData.getDate("fulfill_date"),
                            ordersData.getDouble("total_price")
                    );
                    ordersDtoOptional = Optional.of(ordersDto);
                }
                return ordersDtoOptional;
            } catch (SQLException e) {
                throw new RuntimeException("No order with such id!" + e.getMessage());
            }
        }
    }

    /**
     * Get current orders for  client
     * Current orders are those which don't have a fulfilled date;
     */
    public List<OrdersDto> getCurrentForClient(int clientId) throws SQLException {
        String sql = "SELECT orders.id,client_id,fulfill_date,total_price" +
                " FROM orders,clients WHERE orders.client_id = clients.id " +
                "AND clients.id = ? AND fulfill_date IS NULL;";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            try (ResultSet ordersData = ps.executeQuery()) {
                List<OrdersDto> currentOrdersForClient = new ArrayList<>();
                while (ordersData.next()) {
                    OrdersDto ordersDto = new OrdersDto(
                            ordersData.getInt("id"),
                            ordersData.getInt("client_id"),
                            ordersData.getDate("fulfill_date"),
                            ordersData.getDouble("total_price")
                    );
                    currentOrdersForClient.add(ordersDto);
                }
                return currentOrdersForClient;
            }
        } catch (SQLException e) {
            throw new RuntimeException("No orders for this clientId!" + e.getMessage());
        }
    }

    /**
     * Adding a new order
     */
    public boolean addOrder(OrdersDto order) throws SQLException {
        String sql = "INSERT INTO orders VALUES(?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(2, order.getClient_id());
            ps.setDate(3, (Date) order.getFulfill_date());
            ps.setDouble(4, order.getTotal_price());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert new order!" + e.getMessage());
        }
    }

    /**
     * An order is closed when it has a fulfill_date and a total price.
     */

    public boolean closeOrder(int orderId) throws SQLException {
        Optional<OrdersDto> searchByProductId = getOrdersById(orderId);
        if (!searchByProductId.isPresent()) {
            System.out.println("No order with this ID found!");
            return false;
        }

        String sql = "UPDATE orders " +
                "SET fulfill_date = DATE('now'), total_price = (SELECT SUM(products.price * order_item.quantity) FROM order_item " +
                "JOIN products on order_item.product_id = products.id " +
                "WHERE order_item.order_id = orders.id) " +
                "WHERE orders.id = ?  ";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Order was not closed!" + e.getMessage());
        }
    }

    /**
     * List of closed orders for a specific client id
     */
    public List<OrdersDto> getallClosedByClientId(int clientId) throws SQLException {
        String sql = "SELECT id, client_id, fulfill_date, total_price\n" +
                "FROM orders \n" +
                "WHERE fulfill_date IS NOT NULL AND client_id = ? ;";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            try (ResultSet ordersData = ps.executeQuery()) {
                List<OrdersDto> ordersClosedByClientId = new ArrayList<>();
                while (ordersData.next()) {
                    OrdersDto ordersDto = new OrdersDto(
                            ordersData.getInt("id"),
                            ordersData.getInt("client_id"),
                            ordersData.getDate("fulfill_date"),
                            ordersData.getDouble("total_price")
                    );
                    ordersClosedByClientId.add(ordersDto);
                }
                return ordersClosedByClientId;
            }
        } catch (SQLException e) {
            throw new RuntimeException("No orders closed for this ID" + e.getMessage());
        }
    }
}