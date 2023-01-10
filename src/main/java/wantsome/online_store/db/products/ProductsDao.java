package wantsome.online_store.db.products;

import org.sqlite.SQLiteConfig;
import wantsome.online_store.db.orders.OrdersDto;
import wantsome.online_store.db.service.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsDao {
/**
 * Get products by Id and Description to veirify if the product exists or not.
 * */
    public Optional<ProductsDto> getProductsById(int id) throws SQLException {
        Optional<ProductsDto> productsDtoOptional = Optional.empty();
        String sql = "SELECT id,product_type,description,price,stock FROM products WHERE id = ?";
        ResultSet productsData;
        try(PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            productsData = preparedStatement.executeQuery();
        }
        while (productsData.next()){
            ProductsDto productsDto = new ProductsDto(
                    productsData.getInt("id"),
                    productsData.getString("product_type"),
                    productsData.getString("description"),
                    productsData.getDouble("price"),
                    productsData.getInt("stock")
            );
            productsDtoOptional = Optional.of(productsDto);
        }
        return productsDtoOptional;
    }
    public Optional<ProductsDto> getProductsByDescription(String description) throws SQLException {
        Optional<ProductsDto> productsDtoOptional = Optional.empty();
        String sql = "SELECT id,product_type,description,price,stock FROM products WHERE description = ?";
        ResultSet productsData;
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, description);
            productsData = preparedStatement.executeQuery();
        }
        while (productsData.next()) {
            ProductsDto productsDto = new ProductsDto(
                    productsData.getInt("id"),
                    productsData.getString("product_type"),
                    productsData.getString("description"),
                    productsData.getDouble("price"),
                    productsData.getInt("stock")
            );
            productsDtoOptional = Optional.of(productsDto);
        }
        return productsDtoOptional;
    }

    /**
     * Adding a new product
     * */

    public boolean addProduct (ProductsDto product) throws SQLException{
        Optional<ProductsDto> searchById = getProductsById(product.getId());
        if(!searchById.isEmpty()){
            return false;
        }
        Optional<ProductsDto> searchByDescription = getProductsByDescription(product.getDescription());
        if(searchByDescription.isPresent()){
            System.out.println("This product already exists");
            return false;
        }
        String sql = "INSERT INTO products VALUES(?,?,?,?,?)";
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1,product.getId());
        preparedStatement.setString(2,product.getProduct_type());
        preparedStatement.setString(3,product.getDescription());
        preparedStatement.setDouble(4, product.getPrice());
        preparedStatement.setInt(5,product.getStock());
        preparedStatement.executeUpdate();
        return true;
    }
    /**
     * Get all produts.
     * */

    public List<ProductsDto> getAllProducts() throws SQLException {
        List<ProductsDto> allProductsList = new ArrayList<>();
        String sql = "SELECT id,product_type,description,price,stock FROM products";
        Statement statement = ConnectionManager.getConnection().createStatement();
        ResultSet allProducts = statement.executeQuery(sql);

        while (allProducts.next()) {
            ProductsDto currentProduct = new ProductsDto(
                    allProducts.getInt("id"),
                    allProducts.getString("product_type"),
                    allProducts.getString("description"),
                    allProducts.getDouble("price"),
                    allProducts.getInt("stock")
            );
            allProductsList.add(currentProduct);
        }
        return allProductsList;
    }
    /**
     * Get products by their type.
     * */
    public Optional<ProductsDto> getProductsByProductType(String product_type) throws SQLException {
        Optional<ProductsDto> productsDtoOptional = Optional.empty();
        String sql = "SELECT id,product_type,description,price,stock FROM products WHERE product_type = ?";
        ResultSet productsData;
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, product_type);
            productsData = preparedStatement.executeQuery();
        }
        while (productsData.next()) {
            ProductsDto productsDto = new ProductsDto(
                    productsData.getInt("id"),
                    productsData.getString("product_type"),
                    productsData.getString("description"),
                    productsData.getDouble("price"),
                    productsData.getInt("stock")
            );
            productsDtoOptional = Optional.of(productsDto);
        }
        return productsDtoOptional;
}

/**
 * Updating the stock for an existing product.
 * */
    public boolean updateStock(ProductsDto productsDto, int stock) throws SQLException{
        Optional<ProductsDto> searchByProductId = getProductsById(productsDto.getId());
        if(searchByProductId.isEmpty()){
            System.out.println("Product with this ID was not found!");
            return false;
        }
        String sql = "UPDATE orders SET stock = ? WHERE id = ?";
        try{
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1,stock);
            preparedStatement.setInt(2,productsDto.getId());
            return  preparedStatement.executeUpdate() > 0;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Error updating stock");
        }
    }

}
