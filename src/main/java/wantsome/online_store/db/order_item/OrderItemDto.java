package wantsome.online_store.db.order_item;

import wantsome.online_store.db.orders.OrdersDto;

import java.util.Objects;

public class OrderItemDto  {
    private int id;
    private int order_id;
    private int product_id;
    private int quantity;

    public OrderItemDto(int id, int order_id, int product_id, int quantity) {
        this.id = id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDto that = (OrderItemDto) o;
        return id == that.id && order_id == that.order_id && product_id == that.product_id && quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order_id, product_id, quantity);
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "id=" + id +
                ", order_id=" + order_id +
                ", product_id=" + product_id +
                ", quantity=" + quantity +
                '}';
    }
}
