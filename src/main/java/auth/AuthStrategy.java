package auth;

import salonOrg.User;
import java.sql.SQLException;


public interface AuthStrategy {
    //boolean authentification(User user) throws SQLException, ClassNotFoundException;
    String authenticateAndGetUser(User user) throws SQLException, ClassNotFoundException;
}
