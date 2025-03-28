package ServerWork;

import DB.*;
import salonOrg.*;
import auth.*;
import salonOrg.User;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;


public class Worker implements Runnable {
    protected Socket clientSocket = null;
    ObjectInputStream sois;
    ObjectOutputStream soos;


public Worker(Socket clientSocket) {this.clientSocket = clientSocket;}

    @Override
    public void run() {
    try {
        sois = new ObjectInputStream(clientSocket.getInputStream());
        soos = new ObjectOutputStream(clientSocket.getOutputStream());
while (true) {
    System.out.println("Получение команды от клиента...");
    String choice = sois.readObject().toString();
    System.out.println(choice);
    System.out.println("Команда получена");
    switch (choice) {
        case "registrationUser" -> {
            System.out.println("Запрос к БД на проверку пользователя(таблица user), клиент: " + clientSocket.getInetAddress().toString());

            User user = (User) sois.readObject();
            System.out.println(user.toString());

            ConnectionDB db = new ConnectionDB();
if(!db.isLoginExists(user.getLogin())){
    db.signUpUser(user);
    soos.writeObject("You registered successfully!");
} else{
    soos.writeObject("This user already exists!");
}


            soos.flush();
        }

        case "autorizationUser" -> {
            System.out.println("Выполняется авторизация пользователя....");
            User user = (User) sois.readObject();
            AuthStrategy strategy = null;
            if (user.getLogin() != null && !user.getLogin().isEmpty()) {
                strategy = new LoginAuth();  // Если есть логин
            } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                strategy = new EmailAuth();  // Если есть email
            }
            System.out.println(user.getAccess());

            AuthContext context = new AuthContext(strategy);
            //boolean isAuthenticated = context.authenticateAndGetUser(user);

            String authResult = context.authenticateAndGetUser(user);
            soos.writeObject(authResult);
            soos.flush();


//            if (isAuthenticated) {
//                System.out.println("Пользователь авторизован: " + user.getLogin());
//
//                // Отправляем сообщение об успехе и роль пользователя
//                soos.writeObject("Success");
//                soos.writeObject(user.getRole_id());
//            } else {
//                System.out.println("Ошибка авторизации!");
//                soos.writeObject("Incorrect Data");
//            }
//            soos.flush();
        }


    }
}



    } catch (IOException | ClassNotFoundException | SQLException e) {
        System.out.println("Client disconnected.");
    }
}




}
