package entity;

import models.Customer;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class AuthHandler {
    private static AuthHandler instance;

    private Map<String, User> users;

    private AuthHandler() {
        users = new HashMap<>();
    }

    public static AuthHandler getInstance(){
        if(instance == null){
            instance = new AuthHandler();
        }

        return instance;
    }

    public Customer getCustomer(String login, String password){
        User user = users.getOrDefault(login, null);

        if(user != null && checkPassword(password, user.passwordHash)){
            Customer customer = new Customer(user.id, user.login, user.name);
            return customer;
        }

        return null;
    }

    private boolean checkPassword(String password, String hash){
        String passwordHash = getHashString(password);
        return passwordHash.equals(hash);
    }

    private String getHashString(String s){
        String hash;

        // TODO: какая-то логика хеширования павролей. Bcrypt
        hash = s;

        return hash;
    }
}
