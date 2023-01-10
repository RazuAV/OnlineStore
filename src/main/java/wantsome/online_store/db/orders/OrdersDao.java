package wantsome.online_store.db.orders;

import org.sqlite.SQLiteConfig;
import wantsome.online_store.db.order_item.OrderItemDto;
import wantsome.online_store.db.products.ProductsDto;
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
        ResultSet ordersData;
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ordersData = preparedStatement.executeQuery();
        }
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
    }

    /**
     * Get orders for current client
     */
    public Optional<OrdersDto> getOrdersByClientId(int clientId) throws SQLException {
        Optional<OrdersDto> ordersDtoOptional = Optional.empty();
        String sql = "SELECT orders.id,client_id,fulfill_date,total_price FROM orders,clients WHERE orders.client_id = clients.id AND clients.id = ?";
        ResultSet ordersData;
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, clientId);
            ordersData = preparedStatement.executeQuery();
        }
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
    }

    /**
     * Adding a new order
     */
    public boolean addOrder(OrdersDto order) throws SQLException {
        Optional<OrdersDto> searchByProductId = getOrdersById(order.getId());
        if (searchByProductId.isPresent()) {
            System.out.println("This order already exists");
            return false;
        }
        String sql = "INSERT INTO products VALUES(?,?,?,?)";
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, order.getId());
        preparedStatement.setInt(2, order.getClient_id());
        preparedStatement.setDate(3, (Date) order.getFulfill_date());
        preparedStatement.setDouble(4, order.getTotal_price());
        preparedStatement.executeUpdate();
        return true;
    }

    /**
     * An order is closed when it has a fulfill_date and a total price.
     */

    public boolean closeOrder(OrdersDto ordersDto) throws SQLException {
        Optional<OrdersDto> searchByProductId = getOrdersById(ordersDto.getId());
        if (!searchByProductId.isPresent()) {
            System.out.println("No order with this ID found!");
            return false;
        }
        String sql = "UPDATE orders\n" +
                "SET total_price = (SELECT SUM(price)\n" +
                "FROM products  JOIN order_item WHERE products.id = order_item.product_id AND order_item.order_id = ?),fulfill_date = DATE('now');";
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, ordersDto.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public Optional<OrdersDto> getallClosedByClientId(OrdersDto orders) throws SQLException{
        Optional<OrdersDto> getallClosedByClientId = getOrdersById(orders.getId());

   String sql = "SELECT orders.id,client_id,fulfill_date,total_price FROM orders,clients WHERE orders.client_id = clients.id AND orders.id = ? AND fulfill_date IS NOT NULL AND price IS NOT NULL";
        ResultSet ordersData;
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, orders.getId());
            ordersData = preparedStatement.executeQuery();
        }
        while (ordersData.next()) {
            OrdersDto ordersDto = new OrdersDto(
                    ordersData.getInt("id"),
                    ordersData.getInt("client_id"),
                    ordersData.getDate("fulfill_date"),
                    ordersData.getDouble("total_price")
            );
            getallClosedByClientId = Optional.of(ordersDto);
        }
        return getallClosedByClientId;
    }
}
