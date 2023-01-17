package wantsome.online_store.db.service;

import wantsome.online_store.db.clients.ClientsDao;
import wantsome.online_store.db.clients.ClientsDto;
import wantsome.online_store.db.orderItem.OrderItemDao;
import wantsome.online_store.db.orderItem.OrderItemDto;
import wantsome.online_store.db.orders.OrdersDao;
import wantsome.online_store.db.orders.OrdersDto;
import wantsome.online_store.db.products.ProductsDao;
import wantsome.online_store.db.products.ProductsDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static wantsome.online_store.db.products.ProductType.*;

// Initializes the DB as needed
public class DatabaseManager {


    private static final String CREATE_CLIENTS_SQL = "CREATE TABLE IF NOT EXISTS clients(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email TEXT UNIQUE NOT NULL," +
            "password TEXT NOT NULL," +
            "name TEXT NOT NULL," +
            "address TEXT);";

    /**
     * For productType I used some default types; enums.
     */
    private static final String CREATE_PRODUCTS_SQL = "CREATE TABLE IF NOT EXISTS products(\n" +
            "id INTEGER PRIMARY KEY ," +
            "productType TEXT CHECK (productType IN ('" + PC + "', '" + GAMING + "',  '" + BOOKS + "',  '" + PHONES + "', '" + FASHION + "', '" + GARDEN + "', '" + HOUSE + "', '" + SPORT + "','" + AUTO + "', '" + TOYS + "', '" + COSMETICS + "')) NOT NULL,\n" +
            "description TEXT NOT NULL UNIQUE," +
            "price DOUBLE NOT NULL," +
            "stock INTEGER NOT NULL);";


    private static final String CREATE_ORDERS_SQL = "CREATE TABLE IF NOT EXISTS orders(" +
            "id INTEGER primary key AUTOINCREMENT," +
            "clientId INTEGER references clients(id)," +
            "fullFillDate Date DATE," +
            "totalPrice DOUBLE);";


    private static final String CREATE_orderItem_SQL = "CREATE TABLE orderItem(" +
            "id INTEGER primary key AUTOINCREMENT," +
            "orderId INTEGER references orders(id)," +
            "productId INTEGER references products(id)," +
            "quantity INTEGER);";

    private static void createMissingTables() {

        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(CREATE_CLIENTS_SQL);
            st.execute(CREATE_PRODUCTS_SQL);
            st.execute(CREATE_ORDERS_SQL);
            st.execute(CREATE_orderItem_SQL);

        } catch (SQLException e) {
            throw new RuntimeException("Error creating missing tables: " + e.getMessage());
        }
    }

    public static void createDatabase() throws SQLException {
        try {
            createMissingTables();
            insertSomeClients();
            insertProducts();
            insertSomeOrders();
            insertSomeOrderItem();
        } catch (SQLException e) {
            throw new SQLException("Error creating database: " + " " + e.getMessage());
        }

    }

    /**
     * Methods used for inserting some data when the DB is created.
     */
    public static void insertSomeClients() throws SQLException {
        ClientsDao.addClient(new ClientsDto("email@1.com", "parola1", "nume1", "adresa1"));
        ClientsDao.addClient(new ClientsDto("email@2.com", "parola2", "nume2", "adresa2"));
        ClientsDao.addClient(new ClientsDto("email@3.com", "parola3", "nume3", "adresa3"));
        ClientsDao.addClient(new ClientsDto("email@4.com", "parola4", "nume4", "adresa4"));
    }

    public static void insertProducts() throws SQLException {
        ProductsDao.addProduct(new ProductsDto(1, PC, "MacBook Laptop", 25, 100));
        ProductsDao.addProduct(new ProductsDto(2, GAMING, "Gaming Keyboard", 20, 100));
        ProductsDao.addProduct(new ProductsDto(3, BOOKS, "Dune", 2, 100));
        ProductsDao.addProduct(new ProductsDto(4, PHONES, "Iphone charger", 3, 100));
        ProductsDao.addProduct(new ProductsDto(5, FASHION, "Black dress", 5, 100));
        ProductsDao.addProduct(new ProductsDto(6, GARDEN, "Fake grass", 6, 100));
        ProductsDao.addProduct(new ProductsDto(7, HOUSE, "Vacuum cleaner", 15, 100));
        ProductsDao.addProduct(new ProductsDto(8, SPORT, "Tennis ball", 1, 100));
        ProductsDao.addProduct(new ProductsDto(9, AUTO, "Car tier", 15, 100));
        ProductsDao.addProduct(new ProductsDto(10, TOYS, "Doll", 3, 100));
        ProductsDao.addProduct(new ProductsDto(11, COSMETICS, "Mascara", 8, 100));
    }

    public static void insertSomeOrders() throws SQLException {
        OrdersDao.addOrder(new OrdersDto(1));
        OrdersDao.addOrder(new OrdersDto(2));
        OrdersDao.addOrder(new OrdersDto(3));
    }

    public static void insertSomeOrderItem() throws SQLException {
        OrderItemDao.insertOrderItem(new OrderItemDto(1, 1, 1));
        OrderItemDao.insertOrderItem(new OrderItemDto(1, 2, 1));
        OrderItemDao.insertOrderItem(new OrderItemDto(2, 3, 2));
        OrderItemDao.insertOrderItem(new OrderItemDto(3, 6, 3));
    }
}
