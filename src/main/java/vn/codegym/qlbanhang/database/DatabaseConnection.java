package vn.codegym.qlbanhang.database;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.config.PropertiesConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@Setter
public class DatabaseConnection {
    private final static String URL = PropertiesConfig.getProperty("datasource.jdbcUrl");
    private final static String SCHEMA = PropertiesConfig.getProperty("datasource.db_name");
    private final static String DRIVER = PropertiesConfig.getProperty("datasource.driver-class-name");
    private final static String USERNAME = PropertiesConfig.getProperty("datasource.username");
    private final static String PASSWORD = PropertiesConfig.getProperty("datasource.password");

    private final Connection connection;
    private String schemaName;
    @Getter
    private final static DatabaseConnection instance = new DatabaseConnection();

    private DatabaseConnection() {
        this.connection = initConnection();
        this.schemaName = SCHEMA;
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
