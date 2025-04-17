package DB.DAO.DataAccessObject;

import DB.ConnectionDB;

import java.sql.*;
import java.time.LocalDate;

public class FinancialReportDAO {

    public double getRevenue() throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        try (Connection conn = connectionDB.getDBConnection()) {
            String query = "SELECT SUM(total_amount) as total_revenue FROM `order`";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getDouble("total_revenue" ) : 0.0;
            }
        }
    }

    public double getExpenses() throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        try (Connection conn = connectionDB.getDBConnection()) {
            String sql = """
            SELECT SUM(w.stock_quantity * p.cost) as total_expense
            FROM warehouse w
            JOIN product p ON w.product_id = p.product_id
        """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getDouble("total_expense") : 0.0;
            }
        }
    }
}
