package vn.codegym.qlbanhang.database;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@Setter
public class DatabaseConnection {
    private final static String URL = "jdbc:mysql://172.17.0.2:3306/";
    private final static String SCHEMA = "sales";
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "Mysql@123";

    private final Connection connection;
    private String schemaName;
    private final static DatabaseConnection instance = new DatabaseConnection();

    private DatabaseConnection() {
        this.connection = initConnection();
        this.schemaName = SCHEMA;
    }

    public static DatabaseConnection getInstance() {
        return instance;
    }

    private static Connection initConnection() {
        Connection connection;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL + SCHEMA, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
