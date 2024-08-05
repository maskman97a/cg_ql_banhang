package vn.codegym.qlbanhang.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final static String URL = "jdbc:mysql://localhost:3306/iNotes";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "123456a@";

    public DatabaseConnection() {

    }

    public static Connection getConnection() {
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
