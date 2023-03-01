package wantsome.online_store.db.products;

import wantsome.online_store.db.service.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsDao {
    /**
     * Get products by Id and Description to verify if the product exists or not.
     */
    public static Optional<ProductsDto> getProductsById(int id) throws SQLException {
        Optional<ProductsDto> productsDtoOptional = Optional.empty();
        String sql = "SELECT id,productType,description,price,stock FROM products WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet productsData = ps.executeQuery()) {

                while (productsData.next()) {
                    ProductsDto productsDto = new ProductsDto(
                            productsData.getInt("id"),
                            ProductType.valueOf(productsData.getString("productType")),
                            productsData.getString("description"),
                            productsData.getDouble("price"),
                            productsData.getInt("stock"));
                    productsDtoOptional = Optional.of(productsDto);
                }
                return productsDtoOptional;
            } catch (SQLException e) {
                throw new RuntimeException("Failed getting products by id: " + id + " " + e.getMessage());
            }
        }
    }

    public static Optional<ProductsDto> getProductsByDescription(String description) throws SQLException {
        Optional<ProductsDto> productsDtoOptional = Optional.empty();
        String sql = "SELECT id,productType,description,price,stock FROM products WHERE description = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);

            try (ResultSet productsData = ps.executeQuery()) {

                while (productsData.next()) {
                    ProductsDto productsDto = new ProductsDto(
                            productsData.getInt("id"),
                            ProductType.valueOf(productsData.getString("productType")),
                            productsData.getString("description"),
                            productsData.getDouble("price"),
                            productsData.getInt("stock"));
                    productsDtoOptional = Optional.of(productsDto);
                }
                return productsDtoOptional;
            } catch (SQLException e) {
                throw new RuntimeException("Failed getting products by description: " + description + " " + e.getMessage());
            }
        }
    }

    /**
     * Adding a new product
     */

    public static boolean addProduct(ProductsDto product) throws SQLException {
        Optional<ProductsDto> searchById = getProductsById(product.getId());

        if (!searchById.isEmpty()) {
            System.out.println("This id is already registered!");
            return false;
        }

        Optional<ProductsDto> searchByDescription = getProductsByDescription(product.getDescription());

        if (searchByDescription.isPresent()) {
            System.out.println("This product already exists");
            return false;
        }
        String sql = "INSERT INTO products VALUES(?,?,?,?,?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getId());
            ps.setString(2, product.getproductType().name());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add product " + " " + e.getMessage());
        }
    }

    /**
     * Get all produtcs.
     */

    public static List<ProductsDto> getAllProducts() throws SQLException {

        String sql = "SELECT id,productType,description,price,stock FROM products";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet allProducts = ps.executeQuery()) {

            List<ProductsDto> allProductsList = new ArrayList<>();

            while (allProducts.next()) {
                ProductsDto currentProduct = new ProductsDto(
                        allProducts.getInt("id"),
                        ProductType.valueOf(allProducts.getString("productType")),
                        allProducts.getString("description"),
                        allProducts.getDouble("price"),
                        allProducts.getInt("stock")
                );
                allProductsList.add(currentProduct);
            }
            return allProductsList;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get list of products" + " " + e.getMessage());
        }
    }

    /**
     * Get products by their type.
     */
    public static List<ProductsDto> getProductsByProductType(ProductType productType) throws SQLException {

        String sql = "SELECT id,productType,description,price,stock FROM products WHERE productType = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productType.name());

            try (ResultSet allProductsByType = ps.executeQuery()) {
                List<ProductsDto> allProductsByTypeList = new ArrayList<>();

                while (allProductsByType.next()) {
                    ProductsDto currentProduct = new ProductsDto(
                            allProductsByType.getInt("id"),
                            ProductType.valueOf(allProductsByType.getString("productType")),
                            allProductsByType.getString("description"),
                            allProductsByType.getDouble("price"),
                            allProductsByType.getInt("stock")
                    );
                    allProductsByTypeList.add(currentProduct);
                }
                return allProductsByTypeList;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get list of products" + " " + e.getMessage());
        }
    }

    /**
     * Updating the stock for an existing product.
     * <p>
     * This is done by verifying the orderItem in database, and if some products are ordered, the stock is updated by extracting the
     * total products ordered from the iniliam stock ammount.
     */
    public static boolean updateStock(int id, int stock) throws SQLException {
        Optional<ProductsDto> searchByProductId = getProductsById(id);

        if (searchByProductId.isEmpty()) {
            System.out.println("Product with this ID was not found!");
            return false;
        }
        int updatedStock = searchByProductId.get().getStock();
        if (stock < 0) {
            updatedStock++;
        } else if (stock > 0) {
            updatedStock--;
        }

        String sql = "UPDATE products SET stock = ? WHERE id =  ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, updatedStock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating stock!" + " " + e.getMessage());
        }
    }

}
