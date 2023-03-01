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
    public static Optional<OrdersDto> getOrdersById(int id) throws SQLException {
        Optional<OrdersDto> ordersDtoOptional = Optional.empty();
        String sql = "SELECT id,clientId,fullFillDate,totalPrice FROM orders WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet ordersData = ps.executeQuery()) {

                while (ordersData.next()) {
                    OrdersDto ordersDto = new OrdersDto(
                            ordersData.getInt("id"),
                            ordersData.getInt("clientId"),
                            ordersData.getDate("fullFillDate"),
                            ordersData.getDouble("totalPrice")
                    );
                    ordersDtoOptional = Optional.of(ordersDto);
                }
                return ordersDtoOptional;
            } catch (SQLException e) {
                throw new RuntimeException("No order with such id!" + " " + e.getMessage());
            }
        }
    }

    /**
     * Get current orders for  client
     * Current orders are those which don't have a fulfilled date;
     */
    public static Optional<OrdersDto> getCurrentForClient(int clientId) throws SQLException {
        Optional<OrdersDto> ordersDtoOptional = Optional.empty();
        String sql = "SELECT orders.id,clientId,fullFillDate,totalPrice" +
                " FROM orders,clients WHERE orders.clientId = clients.id " +
                "AND clients.id = ? AND fullFillDate IS NULL;";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);

            try (ResultSet ordersData = ps.executeQuery()) {
                while (ordersData.next()) {
                    OrdersDto ordersDto = new OrdersDto(
                            ordersData.getInt("id"),
                            ordersData.getInt("clientId"),
                            ordersData.getDate("fullFillDate"),
                            ordersData.getDouble("totalPrice")
                    );
                    ordersDtoOptional = Optional.of(ordersDto);
                }
                return ordersDtoOptional;
            }
        } catch (SQLException e) {
            throw new RuntimeException("No orders for this clientId!" + " " + e.getMessage());
        }
    }

    /**
     * Adding a new order
     */
    public static boolean addOrder(OrdersDto order) throws SQLException {

        String sql = "INSERT INTO orders VALUES(?,?,?,?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(2, order.getclientId());
            ps.setDate(3, (Date) order.getfullFillDate());
            ps.setDouble(4, order.gettotalPrice());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert new order!" + " " + e.getMessage());
        }
    }

    /**
     * Helping method to verify if an order was already closed.
     */
    public static Date searchForDate(int orderId) throws SQLException {

        Date date;
        String sql = " SELECT fullFillDate from orders where id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                date = rs.getDate("fullFillDate");
            }
            return date;
        } catch (SQLException e) {
            throw new SQLException("Error getting date for this orderId!" + " " + e.getMessage());
        }
    }

    /**
     * An order is closed when it has a fullFillDate and a total price.
     */

    public static boolean closeOrder(int orderId) throws SQLException {
        Optional<OrdersDto> searchByProductId = getOrdersById(orderId);

        if (!searchByProductId.isPresent()) {
            System.out.println("No order with this ID found!");
            return false;
        }
        if (searchForDate(orderId) != null) {
            System.out.println("Order was already closed");
            return false;
        }
        String sql = "UPDATE orders " +
                "SET fullFillDate = DATE('now'), totalPrice = (SELECT SUM(products.price * orderItem.quantity) FROM orderItem " +
                "JOIN products on orderItem.productId = products.id " +
                "WHERE orderItem.orderId = orders.id) " +
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
    public static List<OrdersDto> getallClosedByClientId(int clientId) throws SQLException {
        String sql = "SELECT id, clientId, fullFillDate, totalPrice\n" +
                "FROM orders \n" +
                "WHERE fullFillDate IS NOT NULL AND clientId = ? ;";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);

            try (ResultSet ordersData = ps.executeQuery()) {
                List<OrdersDto> ordersClosedByClientId = new ArrayList<>();

                while (ordersData.next()) {
                    OrdersDto ordersDto = new OrdersDto(
                            ordersData.getInt("id"),
                            ordersData.getInt("clientId"),
                            ordersData.getDate("fullFillDate"),
                            ordersData.getDouble("totalPrice")
                    );
                    ordersClosedByClientId.add(ordersDto);
                }
                return ordersClosedByClientId;
            }
        } catch (SQLException e) {
            throw new RuntimeException("No orders closed for this ID" + " " + e.getMessage());
        }
    }
}