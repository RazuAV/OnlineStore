package wantsome.online_store.db.products;

import java.util.Objects;

public class ProductsDto {
    private int id;
    private ProductType productType;
    private String description;
    private double price;
    private int stock;

    public ProductsDto(int id, ProductType productType, String description, double price, int stock) {
        this.id = id;
        this.productType = productType;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductType getproductType() {
        return productType;
    }

    public void setproductType(ProductType productType) {
        this.productType = productType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsDto that = (ProductsDto) o;
        return id == that.id && Double.compare(that.price, price) == 0 && stock == that.stock && Objects.equals(productType, that.productType) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productType, description, price, stock);
    }

    @Override
    public String toString() {
        return "ProductsDto{ " +
                "id=" + id +
                ", productType='" + productType + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock= " + stock +
                '}';
    }
}
