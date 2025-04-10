package ServerWork;

import DB.DAO.DataAccessObject.*;
import Enums.ResponseStatus;
import com.google.gson.Gson;
import DB.*;
import Enums.RequestType;
import TCP.Request;
import TCP.Response;
import com.google.gson.reflect.TypeToken;
import salonOrg.*;
import auth.*;
import salonOrg.User;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Worker implements Runnable {
    protected Socket clientSocket = null;
    ObjectInputStream sois;
    ObjectOutputStream soos;
    private Request request;
    private Response response;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;


public Worker(Socket clientSocket) {this.clientSocket = clientSocket;this.gson = new Gson(); // Инициализация Gson
    this.response = new Response(); this.request = new Request();}


    @Override
    public void run() {
    try {
        sois = new ObjectInputStream(clientSocket.getInputStream());
        soos = new ObjectOutputStream(clientSocket.getOutputStream());
while (true) {
    System.out.println("Получение команды от клиента...");


    Request request = (Request) sois.readObject();
    System.out.println(request.getRequestType());
    System.out.println("Команда получена");
    switch (request.getRequestType()){
        case REGISTRATION -> {
            System.out.println("Запрос к БД на проверку пользователя(таблица user), клиент: " + clientSocket.getInetAddress().toString());
            String message = request.getRequestMessage();
            User newUser = new Gson().fromJson(message, User.class);
            System.out.println(newUser.toString());

            ConnectionDB db = new ConnectionDB();

            if(!db.isLoginExists(newUser.getLogin())){
                db.signUpUser(newUser);
                response.setResponseMessage("You registered successfully");
                response.setResponseStatus(ResponseStatus.OK);
            }else {
                response.setResponseMessage("This user already exists!");
                response.setResponseStatus(ResponseStatus.OK);
            }
            soos.writeObject(response);
            soos.flush();

        }

        case ADD_NEW_PRODUCT -> {
            System.out.println("Запрос к БД на добавление товара");
            String message = request.getRequestMessage();
            Product newProduct = new Gson().fromJson(message, Product.class);
            System.out.println(newProduct.toString());

            ProductDAO productDAO = new ProductDAO();

            Response response = new Response();
            if(!productDAO.isNameProductExists(newProduct.getProductName())){
                productDAO.addNewProduct(newProduct);
                response.setResponseMessage("Товар успешно зарегистрирован");
                response.setResponseStatus(ResponseStatus.OK);
            }else {
                response.setResponseMessage("Такое название товара уже существует!");
                response.setResponseStatus(ResponseStatus.ERROR);
            }
            soos.writeObject(response);
            soos.flush();
        }

        case AUTHORIZATION -> {
            System.out.println("Выполняется авторизация пользователя....");
            String message = request.getRequestMessage();
            User user = gson.fromJson(message, User.class);
            AuthStrategy strategy = null;
            if (user.getLogin() != null && !user.getLogin().isEmpty()) {
                strategy = new LoginAuth();  // Если есть логин
            } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                strategy = new EmailAuth();  // Если есть email
            }
            System.out.println(user.getAccess());

            AuthContext context = new AuthContext(strategy);


            String authResult = context.authenticateAndGetUser(user);
            System.out.println(authResult);
            soos.writeObject(authResult);
            soos.flush();

        }
        case GET_ALL_USERS -> {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAll();

            String jsonUsers = new Gson().toJson(users);
            Response response = new Response(ResponseStatus.OK, "Список пользователей получен", jsonUsers);
            soos.writeObject(response);
            soos.flush();
        }

        case UPDATE_USER_ACCESS -> {
            UserDAO userDAO = new UserDAO();
            //Request receivedRequest = new Gson().fromJson(request.getRequestMessage(), Request.class);
            String message = request.getRequestMessage();
            List<User> userToUpdate =  new Gson().fromJson(message, new TypeToken<List<User>>(){}.getType());
            boolean updateResult = userDAO.updateUsersAccess(userToUpdate);

            Response response;
            if (updateResult) {
                response = new Response(ResponseStatus.OK, "Доступ пользователей успешно обновлен", null);
            } else {
                response = new Response(ResponseStatus.ERROR, "Ошибка при обновлении доступа", null);
            }

            soos.writeObject(response);
            soos.flush();



        }

        case DELETE_USER -> {
            String message = request.getRequestMessage();
            User deluser = gson.fromJson(message, User.class);
            UserDAO userDAO = new UserDAO();
            userDAO.deleteUser(deluser.getLogin());

            String responseDelUser = new Gson().toJson(deluser);
            Response response = new Response(ResponseStatus.OK, "Пользователь удален", responseDelUser);
            soos.writeObject(response);
            soos.flush();
        }

        case ADMIN_VIEW_CATALOG -> {
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAll();

            String jsonProducts = new Gson().toJson(products);
            Response response = new Response(ResponseStatus.OK, "Список товаров", jsonProducts);
            soos.writeObject(response);
            soos.flush();
        }

        case GET_ALL_CATEGORIES -> {
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> products = categoryDAO.getAll();

            String jsonCategories = new Gson().toJson(products);
            Response response = new Response(ResponseStatus.OK, "Список категорий", jsonCategories);
            soos.writeObject(response);
            soos.flush();
        }

        case FILTER_PRODUCTS -> {
            Map<String, Object> filterData = new Gson().fromJson(request.getRequestMessage(),Map.class);
            List<Double> categoryIdsRaw = (List<Double>) filterData.get("categoryIds");
            List<Integer> categoryIds = new ArrayList<>();

            for (Double d : categoryIdsRaw) {
                categoryIds.add(d.intValue());
            }

            Double minPrice = filterData.get("minPrice") != null ? ((Number) filterData.get("minPrice")).doubleValue() : null;
            Double maxPrice = filterData.get("maxPrice") != null ? ((Number) filterData.get("maxPrice")).doubleValue() : null;

            ProductDAO productDAO = new ProductDAO();
            List<Product> filteredData = productDAO.getFilteredProducts(categoryIds, minPrice, maxPrice);
            String jsonFilteredData = new Gson().toJson(filteredData);
            Response response = new Response(ResponseStatus.OK,"Отфильтрованные данные", jsonFilteredData);
            soos.writeObject(response);
            soos.flush();
        }

        case SEARCH_PRODUCTS ->{
            String searchText = new Gson().fromJson(request.getRequestMessage(), String.class);
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.searchProducts(searchText.trim());

            System.out.println(products);
            Response response = new Response();
            if(!products.isEmpty()){
                String jsonProducts = new Gson().toJson(products);
                response.setResponseStatus(ResponseStatus.OK);
                response.setResponseMessage("Список товаров");
                response.setResponseData(jsonProducts);
            } else{
                response.setResponseStatus(ResponseStatus.ERROR);
                response.setResponseMessage("Ничего не найдено");
            }

            soos.writeObject(response);
            soos.flush();
        }

        case ADD_TO_CART -> {

            Product product = new Gson().fromJson(request.getRequestMessage(), Product.class);
            String userLogin = request.getUserLogin();

            CartDAO cartDAO = new CartDAO();

            soos.writeObject(cartDAO.addToCart(product, userLogin));
            soos.flush();

        }

        case VIEW_CART -> {
            String userLogin = request.getUserLogin();
            CartItemsDAO cartItemsDAO = new CartItemsDAO();
            List<CartItems> myCart = cartItemsDAO.getCartItemsByUserId(userLogin);
            String jsonData = new Gson().toJson(myCart);
            Response response = new Response(ResponseStatus.OK,"Моя корзина", jsonData);
            soos.writeObject(response);
            soos.flush();

        }

        case REMOVE_FROM_CART -> {
            String userLogin = request.getUserLogin();
            int cartItemID = Integer.parseInt(request.getRequestMessage());

            CartItemsDAO cartItemsDAO = new CartItemsDAO();

            boolean isRemovedItem = cartItemsDAO.deleteCartItem(cartItemID);
            Response response = new Response();

            if(isRemovedItem){
                response.setResponseStatus(ResponseStatus.OK);
                response.setResponseMessage("Товар успешно удалён из корзины");

            } else {
                response.setResponseStatus(ResponseStatus.ERROR);
                response.setResponseMessage("Ошибка удаления товара из корзины");

            }
            soos.writeObject(response);
            soos.flush();
        }

        case GET_ALL_PAYMENT_METHODS -> {
            PaymentDAO paymentDAO = new PaymentDAO();
            List<Payment> products = paymentDAO.getAll();

            String jsonCategories = new Gson().toJson(products);
            Response response = new Response(ResponseStatus.OK, "Список способов оплаты", jsonCategories);
            soos.writeObject(response);
            soos.flush();
        }

        case PLACE_ORDER -> {
            String user_login = request.getUserLogin();
            Order order = new Gson().fromJson(request.getRequestMessage(), Order.class);

            System.out.println("Получен запрос на создание заказа для пользователя: " + user_login);

            OrderDAO orderDAO = new OrderDAO();
            int orderID = orderDAO.saveOrder(order, user_login);
            if(orderID!=1){

                response.setResponseStatus(ResponseStatus.OK);
                response.setResponseMessage("Данные добавлены в БД order и order_item");
                response.setResponseData(new Gson().toJson(orderID));
            } else {
                response.setResponseStatus(ResponseStatus.ERROR);
                response.setResponseMessage("Ошибка добавления данных в БД order и order_item");
            }
            soos.writeObject(response);
            soos.flush();
            System.out.println("data Is send");

        }
        case VIEW_MY_ORDERS -> {
            String userLogin = request.getUserLogin();
            OrderDAO orderDAO = new OrderDAO();
            List<Order> myOrders = orderDAO.getOrdersByUserId(userLogin);
            String jsonData = new Gson().toJson(myOrders);
            Response response = new Response(ResponseStatus.OK,"Мои заказы", jsonData);
            soos.writeObject(response);
            soos.flush();
        }
        case VIEW_ORDER_DETAILS -> {
            String userLogin = request.getUserLogin();
            Order order = new Gson().fromJson(request.getRequestMessage(), Order.class);
            OrderDAO orderDAO = new OrderDAO();
            List<OrderItems> myOrderDetail = orderDAO.getOrderDetail(order, userLogin);
            String jsonData = new Gson().toJson(myOrderDetail);
            Response response = new Response(ResponseStatus.OK,"Мои заказы", jsonData);
            soos.writeObject(response);
            soos.flush();

        }
        case SALES_LINE_CHART -> {
            OrderDAO orderDAO = new OrderDAO();
            Map<String, Double> revenueMap = orderDAO.getMonthlyRevenue();
            response.setResponseStatus(ResponseStatus.OK);
            response.setResponseData(new Gson().toJson(revenueMap));
        }

        case SALES_BAR_CHART -> {
            OrderDAO orderDAO = new OrderDAO();
            Map<String, Integer> orderCountMap = orderDAO.getMonthlyOrderCount();
            response.setResponseStatus(ResponseStatus.OK);
            response.setResponseData(new Gson().toJson(orderCountMap));
        }
    }




}



    } catch (IOException | ClassNotFoundException | SQLException e) {
        System.out.println("Client disconnected.");
    }
}




}
