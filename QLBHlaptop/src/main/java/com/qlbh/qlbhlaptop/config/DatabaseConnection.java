package com.qlbh.qlbhlaptop.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {
    private static Connection connection = null;

    // Đọc thông tin từ file config.properties
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Không tìm thấy file config.properties");
            }
            props.load(input);
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file cấu hình:");
            e.printStackTrace();
        }
        return props;
    }

    // Lấy kết nối
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Kết nối đến SQL Server thành công!");
            } catch (SQLException e) {
                System.err.println("Lỗi kết nối Database:");
                e.printStackTrace();
            }
        }
        return connection;
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Đã đóng kết nối đến SQL Server.");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối:");
                e.printStackTrace();
            }
        }
        
    }
    
    // Test kết nối
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Test kết nối thành công!");
            closeConnection();
        }
    }
}
