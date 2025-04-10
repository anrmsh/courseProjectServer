package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import Enums.ResponseStatus;
import TCP.Response;
import salonOrg.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAO {

   public Response addToCart(Product product, String userLogin) {
       ConnectionDB connectionDB = new ConnectionDB();



       try (Connection conn = connectionDB.getDBConnection()) {

           PreparedStatement psCheckUser = conn.prepareStatement("SELECT user_id FROM user WHERE user_id = (SELECT user_id FROM user WHERE login = ?)");
           psCheckUser.setString(1, userLogin);
           ResultSet rsUser = psCheckUser.executeQuery();


           if (!rsUser.next()) {
               return new Response(ResponseStatus.ERROR, "Пользователь не найден");
           }
           int userId = rsUser.getInt("user_id");
           PreparedStatement psProduct = conn.prepareStatement("SELECT product_id FROM product WHERE product_name = ?");
           psProduct.setString(1, product.getProductName());
           ResultSet rsProduct = psProduct.executeQuery();

           if (!rsProduct.next()) {
               return new Response(ResponseStatus.ERROR, "Товар не найден");
           }

           int productId = rsProduct.getInt("product_id");

           int cartId = -1;
           PreparedStatement psCart = conn.prepareStatement("SELECT cart_id FROM cart WHERE user_id = ?");
           psCart.setInt(1, userId);
           ResultSet rsCart = psCart.executeQuery();

           if (rsCart.next()) {
               cartId = rsCart.getInt("cart_id");
           } else {
               PreparedStatement psCreateCart = conn.prepareStatement("INSERT INTO cart(user_id) VALUES(?)");
               psCreateCart.setInt(1, userId);
               psCreateCart.executeUpdate();
           }

           PreparedStatement psCartItem = conn.prepareStatement(
                   "INSERT INTO cart_item(product_id, cart_id, quantity_product) VALUES (?, ?, ?)");
           psCartItem.setInt(1, productId);
           psCartItem.setInt(2, cartId);
           psCartItem.setInt(3, product.getQuantity());
           psCartItem.executeUpdate();

           PreparedStatement psUpdateStock = conn.prepareStatement("UPDATE warehouse SET stock_quantity = stock_quantity - ? WHERE product_id = ?");
           psUpdateStock.setInt(1, product.getQuantity());
           psUpdateStock.setInt(2, productId);
           psUpdateStock.executeUpdate();

           return new Response(ResponseStatus.OK, "Добавлено в корзину");

       } catch (SQLException e) {
           e.printStackTrace();
           return new Response(ResponseStatus.ERROR, "Ошибка при добавлении в корзину");
       } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
   }
}
