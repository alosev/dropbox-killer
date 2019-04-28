package entity;

import models.Customer;
import models.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
    private String host;
    private int port;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Consumer<String> loggerHandler;
    private Consumer<Message> messageHandler;
    private Customer customer;
    private boolean isAlive;
    private boolean isAuth;

    public Client(String host, int port, Consumer<String> loggerHandler) throws IOException {
        this.host = host;
        this.port = port;
        this.loggerHandler = loggerHandler;
    }

    public void start(){
        if(!isAlive){
            isAlive = true;
            isAuth = false;
            customer = null;

            Thread thread = new Thread(this::createThread);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void auth(Customer customer){
        this.customer = customer;
        isAuth = true;
    }

    private void createThread(){
        try{
            socket = new Socket(host, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            loggerHandler.accept("Подключение установлено.");

            while (isAlive){
                Message msg = Message.deserialization(inputStream);
                if(messageHandler != null){
                    messageHandler.accept(msg);
                }
            }
        }
        catch (Exception ex){
            loggerHandler.accept("Ошибка: " + ex.getMessage());
        }
        finally {
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            loggerHandler.accept("Подключение остановлено.");
        }
    }

    public void sendMessage(Message msg){
        if(isAlive){
            try {
                Message.serialization(msg, outputStream);
            } catch (IOException e) {
                loggerHandler.accept(e.getMessage());
            }
        }
    }

    public void setMessageHandler(Consumer<Message> messageHandler) {
        this.messageHandler = messageHandler;
    }

    public boolean isAuth() {
        return isAuth;
    }
}
