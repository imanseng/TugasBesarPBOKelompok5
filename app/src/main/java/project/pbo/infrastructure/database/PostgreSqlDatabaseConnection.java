package project.pbo.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import project.pbo.infrastructure.config.Config;

public class PostgreSqlDatabaseConnection implements DatabaseConnection {
    @Override
    public Connection connect() throws SQLException {
        String url = Config.get(
                "db.url",
                "jdbc:postgresql://localhost:5432/rental_db"
        );

        String username = Config.get("db.username", "rental_app");
        String password = Config.get("db.password", "rental_password");

        return DriverManager.getConnection(url, username, password);
    }
}