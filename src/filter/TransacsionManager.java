package filter;

import com.mysql.jdbc.Connection;
import utils.ConnectionUtil;

import java.sql.SQLException;

public class TransacsionManager {
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static void beginTransacsion() {
        try {
            ConnectionUtil.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void rollback() {
        try {
            ConnectionUtil.getConnection().rollback();
            ConnectionUtil.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void commit() {
        try {
            ConnectionUtil.getConnection().commit();
            ConnectionUtil.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
