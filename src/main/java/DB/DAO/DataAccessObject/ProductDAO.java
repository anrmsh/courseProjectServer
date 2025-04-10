package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import DB.DAO.DAO;
import salonOrg.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDAO implements DAO<Product> {
    @Override
    public List<Product> getAll(){
        ConnectionDB connectionDB = new ConnectionDB();
        List<Product> products = new ArrayList<Product>();
        String sqlQuery = "SELECT pr.product_name, pr.price, pr.cost,wh.stock_quantity, c.category_name " +
                "FROM product pr JOIN category c ON pr.category_id=c.category_id "
                + "JOIN warehouse wh ON pr.product_id=wh.product_id";



        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet res = statement.executeQuery()){

            while (res.next()) {
                Product product = new Product();
                product.setProductName(res.getString("product_name"));
                product.setSellPrice(res.getDouble("price"));
                product.setCost(res.getDouble("cost"));
                product.setCategory(res.getString("category_name"));
                product.setQuantity(res.getInt("stock_quantity"));

                products.add(product);
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return products;

    }

    public List<Product> getFilteredProducts(List<Integer> categoryIds, Double minPrice, Double maxPrice) {
        ConnectionDB connectionDB = new ConnectionDB();
        List<Product> products = new ArrayList<Product>();
        String sqlQuery = "SELECT pr.product_name, pr.price, pr.cost, c.category_name "
                +"FROM product pr " +
                "JOIN category c ON pr.category_id=c.category_id "+
                "WHERE 1=1";

        List<Object> parameters = new ArrayList<>();

        if(categoryIds != null && !categoryIds.isEmpty()) {
            String placeholders="";
            for(int i = 0; i < categoryIds.size(); i++) {
                placeholders +="?";
                if(i< categoryIds.size()-1){
                    placeholders += ", ";
                }
            }
            sqlQuery += " AND c.category_id IN ("+placeholders+")";
            parameters.addAll(categoryIds);
        }

        if (minPrice != null) {
            sqlQuery += " AND pr.price >= ?";
            parameters.add(minPrice);
        }

        // Фильтр по максимальной цене
        if (maxPrice != null) {
            sqlQuery += " AND pr.price <= ?";
            parameters.add(maxPrice);
        }

        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery)
            ){

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            ResultSet res = statement.executeQuery();
            while (res.next()) {
                Product product = new Product();
                product.setProductName(res.getString("product_name"));
                product.setSellPrice(res.getDouble("price"));
                product.setCost(res.getDouble("cost"));
                product.setCategory(res.getString("category_name"));

                products.add(product);
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        System.out.println("Категории: " + categoryIds);
        System.out.println("Мин: " + minPrice + ", Макс: " + maxPrice);

        System.out.println("SQL: " + sqlQuery);
        System.out.println("Params: " + parameters);


        return products;
    }



    public List<Product> searchProducts(String searchText) {
        ConnectionDB connectionDB = new ConnectionDB();
        List<Product> products = new ArrayList<Product>();
        String sqlQuery = "SELECT pr.product_name, pr.price, pr.cost, c.category_name " +
                "FROM product pr JOIN category c ON pr.category_id=c.category_id " +
                "WHERE LOWER(pr.product_name) LIKE ?";


        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery)){

            statement.setString(1, searchText.toLowerCase()+"%");
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                Product product = new Product();
                product.setProductName(res.getString("product_name"));
                product.setSellPrice(res.getDouble("price"));
                product.setCost(res.getDouble("cost"));
                product.setCategory(res.getString("category_name"));

                products.add(product);
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return products;
    }

    public boolean isNameProductExists(String nameProduct){
        String query= "SELECT COUNT(*) FROM product WHERE product_name = ?";
        ConnectionDB connectionDB = new ConnectionDB();
        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nameProduct);
            ResultSet res = statement.executeQuery();

            if(res.next()){
                return res.getInt(1) > 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }




        return false;
    }

    public void addNewProduct(Product obj) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sqlQuery = "INSERT INTO product " +
                "(product_name, price, category_id, cost) " +
                "VALUES (?,?, (SELECT category_id FROM category WHERE category_name = ?) ,?)";

        String insertWarehouseSQL = "INSERT INTO warehouse (stock_quantity, product_id) VALUES (?, (SELECT product_id FROM product WHERE product_name = ?))";

        PreparedStatement prStmt = null;
        Connection connection = null;
        try {
            connection = connectionDB.getDBConnection();
            prStmt = connection.prepareStatement(sqlQuery);
            prStmt.setString(1, obj.getProductName());
            prStmt.setDouble(2, obj.getSellPrice());
            prStmt.setString(3, obj.getCategory());
            prStmt.setDouble(4, obj.getCost());

            int rowsAffected = prStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully.");
            }
            int quantity = 0;
            try (PreparedStatement warehouseStatement = connection.prepareStatement(insertWarehouseSQL)) {
                warehouseStatement.setInt(1,quantity); // Количество на складе
                warehouseStatement.setString(2, obj.getProductName()); // product_id

                warehouseStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

            if (prStmt != null) {
                try {
                    prStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}
