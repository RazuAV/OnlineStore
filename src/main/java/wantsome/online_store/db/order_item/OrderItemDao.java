package wantsome.online_store.db.order_item;

import org.sqlite.SQLiteConfig;
import wantsome.online_store.db.service.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class OrderItemDao {

    public Optional<OrderItemDto> getOrderItemById(int id) throws SQLException {
        Optional<OrderItemDto> orderItemDtoOptional = Optional.empty();
        String sql = "SELECT id,order_id,product_id,quantity FROM order_item WHERE id = ?";
        ResultSet orderItemData;
        try(PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            orderItemData = preparedStatement.executeQuery();
        }
        while (orderItemData.next()){
            OrderItemDto orderItemDto = new OrderItemDto(
                    orderItemData.getInt("id"),
                    orderItemData.getInt("order_id"),
                    orderItemData.getInt("product_id"),
                    orderItemData.getInt("quantity")
            );
            orderItemDtoOptional = Optional.of(orderItemDto);
        }
        return orderItemDtoOptional;
    }
    public boolean insertOrderItem(OrderItemDto orderItem) throws SQLException{
        Optional<OrderItemDto> getOrderItemById = getOrderItemById(orderItem.getId());
        if(getOrderItemById.isPresent()){
            return false;
        }
       String sql = "INSERT INTO order_item(?, ?, ?, ?)" +
        "SELECT order_item.id, order_item.order_id, order_item.product_id, order_item.quantity"+
        "FROM order_item"+
        "JOIN orders ON orders.id = order_item.order_id"+
        "JOIN products ON products.id = order_item.product_id";

        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1,orderItem.getId());
        preparedStatement.setInt(2,orderItem.getOrder_id());
        preparedStatement.setInt(3,orderItem.getProduct_id());
        preparedStatement.setInt(4,orderItem.getQuantity());
        preparedStatement.executeUpdate();
        return true;
    }
    public boolean deleteItem(OrderItemDto orderItemDto) throws SQLException{
        Optional<OrderItemDto> getOrderItemById = getOrderItemById(orderItemDto.getId());
        if(getOrderItemById.isEmpty()){
            return false;
        }
        String sql = "DELETE FROM order_item where id = ?";
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1,orderItemDto.getId());
        preparedStatement.executeUpdate();
        return true;
    }
    public boolean updateQuantity(OrderItemDto orderItemDto, int quantity ) throws SQLException{
        Optional<OrderItemDto> getOrderItemById = getOrderItemById(orderItemDto.getId());
        if(getOrderItemById.isEmpty()){
            return false;
        }
        String sql = "UPDATE order_item SET quantity = ? WHERE id = ?";
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1,quantity);
        preparedStatement.setInt(2,orderItemDto.getId());
        return true;
    }
}
