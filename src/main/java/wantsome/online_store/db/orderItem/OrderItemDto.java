package wantsome.online_store.db.orderItem;


import wantsome.online_store.db.products.ProductType;
import wantsome.online_store.db.products.ProductsDto;

import java.util.Objects;

public class OrderItemDto {
    private int id;
    private int orderId;
    private int productId;
    private ProductType productType;
    private String productDescription;
    private double productPrice;
    private int quantity;

    public OrderItemDto(int id, int orderId, int productId, ProductType productType, String productDescription, double productPrice, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productType = productType;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }
    public OrderItemDto(int orderId, int productId, ProductType productType, String productDescription, double productPrice, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productType = productType;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
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
        return id == that.id && orderId == that.orderId && productId == that.productId && Double.compare(that.productPrice, productPrice) == 0 && quantity == that.quantity && productType == that.productType && Objects.equals(productDescription, that.productDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, productId, productType, productDescription, productPrice, quantity);
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", productType=" + productType +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", quantity=" + quantity +
                '}';
    }
}
