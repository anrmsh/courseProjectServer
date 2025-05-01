package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import salonOrg.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderDAO {

    public int saveOrder(Order order, String userLogin) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();

        try (Connection conn = connectionDB.getDBConnection()) {

            // Получаем ID пользователя
            String userSQL = "SELECT user_id FROM user WHERE login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(userSQL)) {
                stmt.setString(1, userLogin);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) return -1;

                int userID = rs.getInt("user_id");

                // Вставка заказа
                String orderSQL = "INSERT INTO `order` (user_id, total_amount, payment_id) VALUES (?, ?,(SELECT payment_id FROM payment WHERE payment_method = ?))";
                try (PreparedStatement psOrder = conn.prepareStatement(orderSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psOrder.setInt(1, userID);
                    psOrder.setDouble(2, order.getTotalAmount());
                    psOrder.setString(3, order.getPaymentMethod());

                    int rows = psOrder.executeUpdate();
                    if (rows == 0) return -1;

                    try (ResultSet keys = psOrder.getGeneratedKeys()) {
                        if (keys.next()) {
                            int orderID = keys.getInt(1);

                            // Вставка позиций заказа
                            String itemSQL = "INSERT INTO order_item (order_id, product_id, quantity) " +
                                    "VALUES (?, (SELECT product_id FROM product WHERE product_name = ?), ?)";
                            try (PreparedStatement psItem = conn.prepareStatement(itemSQL)) {
                                for (OrderItems item : order.getOrderItems()) {
                                    psItem.setInt(1, orderID);
                                    psItem.setString(2, item.getProduct().getProductName());
                                    psItem.setInt(3, item.getQuantity());
                                    psItem.addBatch();
                                }
                                psItem.executeBatch();
                            }

                            String deleteCartItemSQL = "DELETE FROM cart_item " +
                                    "WHERE product_id IN (SELECT product_id FROM product WHERE product_name = ?) " +
                                    "AND quantity_product = ?";

                            try(PreparedStatement psDeleteCartItem = conn.prepareStatement(deleteCartItemSQL)) {
                                for(OrderItems item:order.getOrderItems()) {
                                    psDeleteCartItem.setString(1, item.getProduct().getProductName());
                                    psDeleteCartItem.setInt(2, item.getQuantity());
                                    psDeleteCartItem.executeUpdate();
                                }
                            }

                            return orderID;
                        }
                    }
                }
            }
        }
        return -1;
    }


    public List<Order> getOrdersByUserId(String login) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = """
        SELECT o.order_id, p.payment_method, o.order_date, o.total_amount, os.order_state_name
        FROM `order` o
        INNER JOIN payment p ON p.payment_id = o.payment_id
        INNER JOIN order_state os ON o.order_state_id = os.order_state_id
        WHERE o.user_id = (SELECT user_id FROM `user` WHERE login = ?)
    """;
        Connection connection = connectionDB.getDBConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, login);
        ResultSet rs = stmt.executeQuery();

        List<Order> items = new ArrayList<>();
        OrderState orderState = new OrderState();
        Payment payment = new Payment();
        User user = new User();
        user.setLogin(login);
        while (rs.next()) {
            Order item = new Order();
            item.setOrderId(rs.getInt("order_id"));
            orderState.setOrderStateName((rs.getString("order_state_name")));
            item.setOrderState(orderState);
            payment.setPaymentMethod((rs.getString("payment_method")));
            item.setPayment(payment);
            item.setTotalAmount(rs.getDouble("total_amount"));
            item.setUser(user);
            item.setOrderDate((rs.getString("order_date")));

            items.add(item);
        }
        return items;
    }


    public List<Order> getAllOrders() throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = """
        SELECT o.order_id, p.payment_method, o.order_date, o.total_amount, os.order_state_name
        FROM `order` o
        INNER JOIN payment p ON p.payment_id = o.payment_id
        INNER JOIN order_state os ON o.order_state_id = os.order_state_id
    """;
        Connection connection = connectionDB.getDBConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<Order> items = new ArrayList<>();

        while (rs.next()) {
            OrderState orderState = new OrderState();
            Payment payment = new Payment();
            Order item = new Order();
            item.setOrderId(rs.getInt("order_id"));
            orderState.setOrderStateName((rs.getString("order_state_name")));
            item.setOrderState(orderState);
            payment.setPaymentMethod((rs.getString("payment_method")));
            item.setPayment(payment);
            item.setTotalAmount(rs.getDouble("total_amount"));
            item.setOrderDate((rs.getString("order_date")));

            items.add(item);
        }
        return items;
    }





    public List<OrderItems> getOrderDetail(Order order, String login) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = """
        SELECT p.product_name, oi.quantity, p.price
        FROM `order_item` oi
        INNER JOIN `product` p ON p.product_id = oi.product_id
        INNER JOIN `order` o ON o.order_id = oi.order_id
        WHERE o.user_id = (SELECT user_id FROM `user` WHERE login = ?)
        AND oi.order_id = ?
    """;
        Connection connection = connectionDB.getDBConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, login);
        stmt.setInt(2, order.getOrderId());
        ResultSet rs = stmt.executeQuery();

        List<OrderItems> items = new ArrayList<>();

        while (rs.next()) {
            Product product = new Product();
            OrderItems item = new OrderItems();
            item.setOrder(order);
            product.setProductName(rs.getString("product_name"));
            product.setSellPrice(rs.getDouble("price"));
            item.setProduct(product);
            item.setQuantity(rs.getInt("quantity"));


            items.add(item);
        }

        return items;
    }

    public Map<String, Integer> getMonthlyOrderCount() throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = """
        SELECT DATE_FORMAT(order_date, '%M %Y') AS month, COUNT(*) AS order_count
        FROM `order`
        GROUP BY month
        ORDER BY month
    """;

        Map<String, Integer> result = new HashMap<>();
        try (Connection conn = connectionDB.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                int count = rs.getInt("order_count");
                result.put(month, count);
            }
        }
        return result;
    }

    public Map<String, Double> getMonthlyRevenue() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = """
        SELECT DATE_FORMAT(order_date, '%M %Y') AS month, SUM(total_amount) AS total_revenue
        FROM `order`
        GROUP BY month
        ORDER BY month
    """;

        Map<String, Double> result = new HashMap<>();
        try (Connection conn = connectionDB.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                double revenue = rs.getDouble("total_revenue");
                result.put(month, revenue);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<OrderState> getAllStatusOrder(){
        ConnectionDB connectionDB = new ConnectionDB();
        List<OrderState> orderStates = new ArrayList<OrderState>();
        String sqlQuery = "SELECT * " + "FROM order_state ";

        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet res = statement.executeQuery()){

            while (res.next()) {
                OrderState status = new OrderState();
                status.setOrderStateId(res.getInt("order_state_id"));
                status.setOrderStateName(res.getString("order_state_name"));

                orderStates.add(status);
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return orderStates;

    }

    public boolean updateOrderState(List<Order> modifiedOrders){
        ConnectionDB connectionDB = new ConnectionDB();
        String sqlQuery = "UPDATE `order` SET order_state_id = (SELECT order_state_id FROM `order_state` WHERE order_state_name = ?) WHERE order_id = ?";
        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            for (Order order : modifiedOrders) {
                statement.setString(1, order.getOrderState().getOrderStateName());
                statement.setInt(2, order.getOrderId());

                int result = statement.executeUpdate();

                if(result==0){
                    return false;
                }

            }
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }




}
