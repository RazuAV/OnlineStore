package wantsome.online_store.db.service;


import org.apache.commons.dbcp2.BasicDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

//Class which control how the application connects to the DB
public class ConnectionManager {
    private static final String DB_FILE = "C:\\Users\\avarv\\OneDrive\\Desktop\\onlineStoreProject\\onlineStore"; // path to the orginal db

    private static final BasicDataSource dataSource = new BasicDataSource();

    //set up the dataSource
    static{
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:" + DB_FILE);
        dataSource.setInitialSize(10); // initial/minimum size of pool
        //settings for db
        dataSource.setConnectionProperties("foreign_keys=true;date_class=text;date_string_format=yyyy-MM-dd;");
    }
//Allows to change the name of database file, useful for example for tests
    public static void setDbFile(String dbFile) {
        System.out.println("Using custom SQLite db file: " + new File(dbFile).getAbsolutePath());
        try {
            dataSource.close();
            dataSource.setUrl("jdbc:sqlite:" + dbFile);
            dataSource.start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//Provides a connection to the db, taken from the connection pool.
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}


