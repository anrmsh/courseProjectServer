package auth;

import DB.ConnectionDB;
import salonOrg.User;
import DB.Const;

import java.sql.*;

import static DB.Const.USER_TABLE;

//public class EmailAuth implements AuthStrategy {
//    @Override
//    public boolean authentification(User user) throws SQLException, ClassNotFoundException {
//        ConnectionDB db = new ConnectionDB();
//        Connection connection = db.getDBConnection();
//        String query = "SELECT * FROM " + USER_TABLE + " WHERE email = ? AND password = ?";
//        PreparedStatement stmt = connection.prepareStatement(query);
//        stmt.setString(1, user.getEmail());
//        stmt.setString(2, user.getPassword());
//
//        ResultSet rs = stmt.executeQuery();
//        boolean isAuthenticated = rs.next(); // Проверяем, найден ли пользователь
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return isAuthenticated;
//    }
//}

public class EmailAuth implements AuthStrategy {

    @Override
    public String authenticateAndGetUser(User user) {
        String query = """
            SELECT u.login, u.access, r.role_name 
            FROM user u
            JOIN role r ON u.role_id = r.role_id
            WHERE u.email = ? AND u.password = ?""";

        try (Connection connection = new ConnectionDB().getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user.setAccess(resultSet.getInt("access"));
                user.setRoleName(resultSet.getString("role_name"));

                if (user.getAccess() == 0) {
                    return "Access Denied";
                }

                return user.getRoleName();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return "Incorrect Data";
    }
}
