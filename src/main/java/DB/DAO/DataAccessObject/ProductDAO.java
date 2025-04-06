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
        String sqlQuery = "SELECT pr.product_name, pr.price, pr.cost, c.category_name " +
                "FROM product pr JOIN category c ON pr.category_id=c.category_id";

        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet res = statement.executeQuery()){

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
}
