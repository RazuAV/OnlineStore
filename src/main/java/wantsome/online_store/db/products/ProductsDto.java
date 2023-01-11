package wantsome.online_store.db.products;

import java.util.Objects;

public class ProductsDto {
    private int id;
    private String product_type;
    private String description;
    private double price;
    private int stock;

    public ProductsDto(int id, String product_type, String description, double price, int stock) {
        this.id = id;
        this.product_type = product_type;
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

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
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
        return id == that.id && Double.compare(that.price, price) == 0 && stock == that.stock && Objects.equals(product_type, that.product_type) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product_type, description, price, stock);
    }

    @Override
    public String toString() {
        return "ProductsDto{ " +
                "id=" + id +
                ", product_type='" + product_type + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
