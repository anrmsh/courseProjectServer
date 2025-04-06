package DB.DAO.DataAccessObject;

import DB.DAO.DAO;
import DB.DAO.SQLinterfeces.SQLUser;
import salonOrg.User;
import DB.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User>, SQLUser {

    @Override
    public List<User> getAll(){
        ConnectionDB connectionDB = new ConnectionDB();
        List<User> users = new ArrayList<User>();
        String sqlQuery = "SELECT u.login, u.password, u.first_name, u.last_name, u.email, u.access, r.role_name " +
                "FROM user u JOIN role r ON u.role_id = r.role_id";

        try(Connection connection = connectionDB.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet res = statement.executeQuery()){

            while (res.next()) {
                User user = new User();
                user.setLogin(res.getString("login"));
                user.setPassword(res.getString("password"));
                user.setFirstName(res.getString("first_name"));
                user.setLastName(res.getString("last_name"));
                user.setEmail(res.getString("email"));
                user.setAccess(res.getInt("access"));
                user.setRoleName(res.getString("role_name"));
                users.add(user);
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

return users;

    }

 public boolean updateUsersAccess(List<User> modifiedUsers){
        ConnectionDB connectionDB = new ConnectionDB();
        String sqlQuery = "UPDATE user SET access = ? WHERE login = ?";
        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            for (User user : modifiedUsers) {
                statement.setInt(1, user.getAccess());
                statement.setString(2, user.getLogin());

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

    public void deleteUser(String login) throws SQLException, ClassNotFoundException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "DELETE FROM user WHERE login = ?";
        try (Connection connection = connectionDB.getDBConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.executeUpdate();

            System.out.println("Пользователь с ID " + login + " удален.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
