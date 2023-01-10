package wantsome.online_store.db.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Initializes the DB as needed
public class DatabaseManager {


    private static final String CREATE_CLIENTS_SQL = "CREATE TABLE IF NOT EXISTS clients("+
            "id INTEGER primary key,"+
            "email TEXT UNIQUE NOT NULL,"+
            "password TEXT NOT NULL,"+
            "name TEXT NOT NULL,"+
            "address TEXT);";

/*
* For product_type I used some default types; enums.
*
* */
    private static final String CREATE_PRODUCTS_SQL = "CREATE TABLE IF NOT EXISTS products(\n" +
            "id INTEGER primary key," +
            "product_type TEXT CHECK (product_type IN ('PC', 'Gaming', 'Books', 'Phones', 'Fashion', 'Garden', 'House', 'Sport',' Auto', 'Toys', 'Cosmetics')),\n" +
            "description TEXT NOT NULL UNIQUE," +
            "price DOUBLE NOT NULL," +
            "stock INTEGER);";


    private static final String CREATE_ORDERS_SQL = "CREATE TABLE IF NOT EXISTS orders(" +
            "id INTEGER primary key," +
            "client_id INTEGER references clients(id)," +
            "fulfill_date DATE," +
            "total_price DOUBLE);";


    private static final String CREATE_ORDER_ITEM_SQL = "CREATE TABLE order_item(" +
            "id INTEGER primary key," +
            "order_id INTEGER references orders(id)," +
            "product_id INTEGER references products(id)," +
            "quantity INTEGER);";

    private static void createMissingTables() {
        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(CREATE_CLIENTS_SQL);
            st.execute(CREATE_PRODUCTS_SQL);
            st.execute(CREATE_ORDERS_SQL);
            st.execute(CREATE_ORDER_ITEM_SQL);

        } catch (SQLException e) {
            throw new RuntimeException("Error creating missing tables: " + e.getMessage());
        }
    }

    public static void createDatabase() {
        createMissingTables();
    }
}
