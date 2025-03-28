package auth;

import salonOrg.User;
import java.sql.SQLException;
public class AuthContext {
    private AuthStrategy strategy;

    public AuthContext(AuthStrategy strategy) {
        this.strategy = strategy;
    }

//    public boolean authentification(User user) throws SQLException, ClassNotFoundException {
//        return strategy.authentification(user);
//
//    }

//    public boolean authenticate(User user) {
//        if (strategy == null) {
//            throw new IllegalStateException("Стратегия аутентификации не установлена");
//        }
//        return strategy.authenticateAndGetUser(user);
//    }

    public String authenticateAndGetUser (User user) throws SQLException, ClassNotFoundException{
        return strategy.authenticateAndGetUser(user);
    }
    // Устанавливает новую стратегию аутентификации
    public void setStrategy(AuthStrategy strategy) {
        this.strategy = strategy;
    }

}
