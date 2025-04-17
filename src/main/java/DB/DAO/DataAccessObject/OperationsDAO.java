package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import Enums.ResponseStatus;
import TCP.Response;
import com.google.gson.Gson;
import salonOrg.OperationAccounter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OperationsDAO {
    public Response getOperationsForAccounter(String startDate, String endDate) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        Response response = new Response();
        List<OperationAccounter> operations = new ArrayList<>();
        try (Connection conn = connectionDB.getDBConnection()) {
            if(startDate == "" && endDate == "") {
                String sql = """
                    SELECT order_date, total_amount 
                    FROM `order` 
                    """;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        operations.add(new OperationAccounter(rs.getDouble("total_amount"),"Доход",rs.getString("order_date")));
                    }

                }
                String sqlQuery = """
                        SELECT date_restock, (stock_quantity * p.price) AS expense
                        FROM warehouse JOIN product p USING(product_id)
                    """;

                PreparedStatement stmt = conn.prepareStatement(sqlQuery);
                ResultSet rs2 = stmt.executeQuery();
                while (rs2.next()) {
                    operations.add(new OperationAccounter(rs2.getDouble("expense"),"Расход",rs2.getString("date_restock")));
                }
            } else{
                String sql = """
                    SELECT order_date, total_amount 
                    FROM `order` 
                    WHERE order_date BETWEEN ? AND ?
                    """;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setDate(1, Date.valueOf(startDate));
                    stmt.setDate(2, Date.valueOf(endDate));

                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        operations.add(new OperationAccounter(rs.getDouble("total_amount"),"Доход",rs.getString("order_date")));
                    }

                }
                String sqlQuery = """
            SELECT date_restock, (stock_quantity * p.price) AS expense
            FROM warehouse JOIN product p USING(product_id)
            WHERE date_restock BETWEEN ? AND ?
        """;

                PreparedStatement stmt = conn.prepareStatement(sqlQuery);
                stmt.setDate(1, Date.valueOf(startDate));
                stmt.setDate(2, Date.valueOf(endDate));
                ResultSet rs2 = stmt.executeQuery();
                while (rs2.next()) {
                    operations.add(new OperationAccounter(rs2.getDouble("expense"),"Расход",rs2.getString("date_restock")));
                }
            }

            Collections.sort(operations, Comparator.comparing(OperationAccounter::getDateOfOperation));

        }
        if (operations.isEmpty()) {
            response.setResponseStatus(ResponseStatus.ERROR);
            response.setResponseMessage("Ошибка!");

        }
        else {
            response.setResponseStatus(ResponseStatus.OK);
            response.setResponseMessage("Операции получены");
            String data = new Gson().toJson(operations);
            response.setResponseData(data);
        }

        return response;
    }
}
