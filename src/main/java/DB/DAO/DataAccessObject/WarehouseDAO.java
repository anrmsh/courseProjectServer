package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import Enums.ResponseStatus;
import TCP.Response;
import salonOrg.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarehouseDAO {
    public Response addToWarehouse(Product product) {
        ConnectionDB connectionDB = new ConnectionDB();



        try (Connection conn = connectionDB.getDBConnection()) {

            PreparedStatement psProduct = conn.prepareStatement("SELECT product_id FROM product WHERE product_name = ?");
            psProduct.setString(1, product.getProductName());
            ResultSet rsProduct = psProduct.executeQuery();

            if (!rsProduct.next()) {
                return new Response(ResponseStatus.ERROR, "Товар не найден");
            }

            int productId = rsProduct.getInt("product_id");


            PreparedStatement psWarehouse = conn.prepareStatement(
                    "UPDATE warehouse " +
                            "SET stock_quantity = stock_quantity + ? " +
                            "WHERE product_id = ?");
            psWarehouse.setInt(1, product.getQuantity());
            psWarehouse.setInt(2, productId);
            psWarehouse.executeUpdate();

            return new Response(ResponseStatus.OK, "Товар успешно закуплен на склад!");

        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(ResponseStatus.ERROR, "Ошибка при закупке товара на склад");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}
