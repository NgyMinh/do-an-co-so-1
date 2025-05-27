package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuizApp;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa"; // Tên người dùng SQL Server
    private static final String PASSWORD = "123456789"; // Mật khẩu SQL Server

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}