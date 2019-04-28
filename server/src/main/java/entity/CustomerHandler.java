package entity;

import models.Customer;
import models.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CustomerHandler {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Customer customer;
    private Consumer<String> loggerHandler;
    private BiConsumer<CustomerHandler, Message> messageHandler;
    private boolean isAlive;
    private boolean isAuth;

    public CustomerHandler(Socket socket, Consumer<String> loggerHandler, BiConsumer<CustomerHandler, Message> messageHandler) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.loggerHandler = loggerHandler;
        this.messageHandler = messageHandler;
        this.isAlive = true;
        this.isAuth = false;

        init();
    }

    private void init() {
        Thread thread = new Thread(this::createThread);
        thread.setDaemon(true);
        thread.start();
    }

    public void auth(Customer customer){
        loggerHandler.accept("Авторизовался пользователь: " + customer.getLogin());
        this.isAuth = true;
        this.customer = customer;
    }

    public void close(){
        isAlive = false;
        isAuth = false;
    }

    private void createThread(){
        try{
            loggerHandler.accept("Клиент подключился.");

            while (isAlive){
                Message msg = Message.deserialization(inputStream);
                messageHandler.accept(this, msg);
            }
        }
        catch (Exception ex){
            loggerHandler.accept("Ошибка клиента: " + ex.getMessage());
        }
        finally {
            loggerHandler.accept("Клиент отключился.");

            try {
                socket.close();
            } catch (IOException e) {
                loggerHandler.accept("Ошибка отключения клиента: " + e.getMessage());
            }
        }
    }

    public void sendMessage(Message msg){
        try {
            Message.serialization(msg, outputStream);
        } catch (IOException e) {
            loggerHandler.accept(e.getMessage());
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isAuth() {
        return isAuth;
    }
}
