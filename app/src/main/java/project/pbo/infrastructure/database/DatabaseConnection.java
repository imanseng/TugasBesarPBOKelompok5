package project.pbo.infrastructure.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection {
    Connection connect() throws SQLException;
}
