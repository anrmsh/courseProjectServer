package DB.DAO.DataAccessObject;

import DB.ConnectionDB;

import java.sql.*;
import java.time.LocalDate;

public class ReportDAO {


    public int countAllOrders(LocalDate startDate, LocalDate endDate) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        try (Connection conn = connectionDB.getDBConnection()) {
            String query = "SELECT COUNT(*) FROM `order` WHERE order_date BETWEEN ? AND ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDate(1, Date.valueOf(startDate));
                stmt.setDate(2, Date.valueOf(endDate));
//                stmt.setString(1, String.valueOf(startDate));
//                stmt.setString(2, String.valueOf(endDate));
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int countCompletedOrders(LocalDate startDate, LocalDate endDate) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        try (Connection conn = connectionDB.getDBConnection()) {
            String sql = "SELECT COUNT(*) FROM `order` WHERE order_date BETWEEN ? AND ? AND order_state_id = 2";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(startDate));
                stmt.setDate(2, Date.valueOf(endDate));
//                stmt.setString(1, String.valueOf(startDate));
//                stmt.setString(2, String.valueOf(endDate));
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int countTotalProductsSold(LocalDate startDate, LocalDate endDate) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        try (Connection conn = connectionDB.getDBConnection()) {
            String sql = """
                    SELECT SUM(oi.quantity)
                    FROM `order` o
                    INNER JOIN order_item oi ON o.order_id = oi.order_id
                    WHERE o.order_date BETWEEN ? AND ?
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(startDate));
                stmt.setDate(2, Date.valueOf(endDate));
//                stmt.setString(1, String.valueOf(startDate));
//                stmt.setString(2, String.valueOf(endDate));
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public double sumTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        try (Connection conn = connectionDB.getDBConnection()) {
            String sql= """
                    SELECT SUM(total_amount)
                    FROM `order`
                    WHERE order_date BETWEEN ? AND ?
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(startDate));
                stmt.setDate(2, Date.valueOf(endDate));
//                stmt.setString(1, String.valueOf(startDate));
//                stmt.setString(2, String.valueOf(endDate));
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getDouble(1) : 0;
            }
        }
    }

    public String getTopSellingProduct(LocalDate startDate, LocalDate endDate) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        try (Connection conn = connectionDB.getDBConnection()) {
            String sql = """
                    SELECT p.product_name
                    FROM `order` o
                    JOIN order_item oi ON o.order_id = oi.order_id
                    JOIN product p ON oi.product_id = p.product_id
                    WHERE o.order_date BETWEEN ? AND ?
                    GROUP BY p.product_name
                    ORDER BY SUM(oi.quantity) DESC
                    LIMIT 1
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(startDate));
                stmt.setDate(2, Date.valueOf(endDate));
//                stmt.setString(1, String.valueOf(startDate));
//                stmt.setString(2, String.valueOf(endDate));
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getString("product_name") : null;
            }
        }
    }

}
