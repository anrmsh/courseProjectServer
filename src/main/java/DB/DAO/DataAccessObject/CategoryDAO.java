package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import DB.DAO.DAO;
import salonOrg.Category;
import salonOrg.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements DAO<Category> {
    @Override
    public List<Category> getAll(){
        ConnectionDB connectionDB = new ConnectionDB();
        List<Category> categories = new ArrayList<Category>();
        String sqlQuery = "SELECT * " + "FROM category ";

        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet res = statement.executeQuery()){

            while (res.next()) {
                Category category = new Category();
                category.setCategoryName(res.getString("category_name"));
                category.setCategoryId(res.getInt("category_id"));

                categories.add(category);
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return categories;

    }
}
