package DB;

import salonOrg.*;

import java.sql.*;


public class ConnectionDB extends Configs{
    Connection dbConnection;

    public Connection getDBConnection()
            throws ClassNotFoundException, SQLException{
        String  connectionString = "jdbc:mysql://" + dbHost+":"
                + dbPort + "/" + dbName + "?verifyServerCertificate=false"+
                "&useSSL=false"+
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp"+
                "&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public boolean isLoginExists(String login){
        String query= "SELECT COUNT(*) FROM user WHERE login = ?";
        try(Connection connection = getDBConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
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

    public void signUpUser(User obj) throws SQLException, ClassNotFoundException {



//            String insert = "INSERT INTO " + Const.USER_TABLE+"("+
//                    Const.USER_LOGIN+","+Const.USER_PASSWORD+","+
//                    Const.USER_FIRSTNAME+","+Const.USER_LASTNAME+","
//                    +Const.USER_EMAIL+","+Const.USER_ROLEid+")"
//                    +" VALUES(?,?,?,?,?,?)";

        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USER_LOGIN + "," + Const.USER_PASSWORD + "," +
                Const.USER_FIRSTNAME + "," + Const.USER_LASTNAME + "," +
                Const.USER_EMAIL + "," + Const.USER_ROLEid + ") " +
                "VALUES(?,?,?,?,?, (SELECT role_id FROM role WHERE role_name = ?))";

            try{
                PreparedStatement prStmt = getDBConnection().prepareStatement(insert);
                prStmt.setString(1,obj.getLogin());
                prStmt.setString(2, obj.getPassword());
                prStmt.setString(3, obj.getFirstName());
                prStmt.setString(4,obj.getLastName());
                prStmt.setString(5,obj.getEmail());
                prStmt.setString(6, String.valueOf(obj.getRoleName()));
                //prStmt.setString(6, String.valueOf(obj.getRole_id()));

                prStmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



    }



}
