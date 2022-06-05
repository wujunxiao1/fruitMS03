package utils;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    private static ThreadLocal<com.mysql.jdbc.Connection> threadLocal = new ThreadLocal<>();
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/fruitDB?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    public static final String USER = "root";
    public static final String PWD = "020611";

    public static Connection creatConnection() {
        try {
            Class.forName(DRIVER);
            return (Connection) DriverManager.getConnection(URL, USER, PWD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        com.mysql.jdbc.Connection connection = threadLocal.get();
        if (connection == null) {
            connection = creatConnection();
            threadLocal.set(connection);
        }
        return threadLocal.get();
    }

    public static void closeConnection() {
        Connection connection = threadLocal.get();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        threadLocal.set(null);
    }
}
