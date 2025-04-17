package DB.DAO.DataAccessObject;

import DB.ConnectionDB;
import DB.DAO.DAO;
import salonOrg.Category;
import salonOrg.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements DAO<Payment> {
//    @Override
//    public List<Payment> getAll(){
//        ConnectionDB connectionDB = new ConnectionDB();
//        List<Payment> paymentMethods = new ArrayList<Payment>();
//        String sqlQuery = "SELECT * " + "FROM payment ";
//
//        try(Connection connection = connectionDB.getDBConnection();
//            PreparedStatement statement = connection.prepareStatement(sqlQuery);
//            ResultSet res = statement.executeQuery()) {
//
//            while (res.next()) {
//                Payment payment = new Payment();
//                payment.setPaymentId(res.getInt("payment_id"));
//                payment.setPaymentMethod(res.getString("payment_method"));
//
//                paymentMethods.add(payment);
//            }
//
//        } catch (SQLException | ClassNotFoundException e){
//            e.printStackTrace();
//        }
//
//        return paymentMethods;
//
//    }

    public List<Payment> getAll(){
        ConnectionDB connectionDB = new ConnectionDB();
        List<Payment> paymentMethods = new ArrayList<Payment>();
        String sqlQuery = "SELECT payment_id, payment_method " + "FROM `payment` ";

        try(Connection connection = connectionDB.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet res = statement.executeQuery()) {

            Payment payment = new Payment();
            while (res.next()) {

                payment.setPaymentId(res.getInt("payment_id"));
                payment.setPaymentMethod(res.getString("payment_method"));

                paymentMethods.add(payment);
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return paymentMethods;

    }





}
