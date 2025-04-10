package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import DB.DAO.DAO;
import salonOrg.CartItems;
import salonOrg.CartItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemsDAO {


    public List<CartItems> getCartItemsByUserId(String login) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = """
        SELECT ci.cart_item_id, ci.quantity_product, p.product_name, p.price, ci.cart_id
        FROM cart_item ci
        JOIN product p ON ci.product_id = p.product_id
        JOIN cart c ON ci.cart_id = c.cart_id
        WHERE c.user_id = (SELECT user_id FROM user WHERE login = ?)
    """;
        Connection connection = connectionDB.getDBConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, login);
        ResultSet rs = stmt.executeQuery();

        List<CartItems> items = new ArrayList<>();
        while (rs.next()) {
            CartItems item = new CartItems();
            item.setCartItemId(rs.getInt("cart_item_id"));
            item.setQuantity(rs.getInt("quantity_product"));
            item.setProductName(rs.getString("product_name"));
            item.setProductPrice(rs.getDouble("price"));
            item.setCartId(rs.getInt("cart_id"));

            items.add(item);
        }
        return items;
    }

    public boolean deleteCartItem(int cartItemID) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String query = "DELETE FROM cart_item WHERE cart_item_id = ?";
        String findProductQuery = "SELECT product_id, quantity_product FROM cart_item WHERE cart_item_id = ?";
        String updateQuantityWarehouseQuery = "UPDATE warehouse SET stock_quantity = stock_quantity + ? WHERE product_id = ?";

        try (Connection connection = connectionDB.getDBConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        PreparedStatement updateStmt = connection.prepareStatement(updateQuantityWarehouseQuery);
        PreparedStatement findStmt = connection.prepareStatement(findProductQuery)) {

            findStmt.setInt(1, cartItemID);
            ResultSet rs = findStmt.executeQuery();

            if(rs.next()) {
                int productID = rs.getInt("product_id");
                int quantityProduct = rs.getInt("quantity_product");

                stmt.setInt(1, cartItemID);
                int rowsAffected = stmt.executeUpdate();

                if(rowsAffected > 0) {
                    updateStmt.setInt(1, quantityProduct);
                    updateStmt.setInt(2, productID);
                    updateStmt.executeUpdate();

                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
}
