package entity;

import models.Customer;
import models.Error;
import models.messages.*;
import org.omg.CORBA.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Consumer;

public class Server {
    private int port;
    private Consumer<String> loggerHandler;
    private boolean status;
    private Vector<CustomerHandler> customerHandlers;

    public Server(int port, Consumer<String> loggerHandler) {
        this.port = port;
        this.loggerHandler = loggerHandler;

        customerHandlers = new Vector<>();
    }

    public void run(){
        if(!status){
            status = true;

            Thread thread = new Thread(this::createThread);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void createThread(){
        try(ServerSocket server = new ServerSocket(port)) {
            loggerHandler.accept("Сервер запущен.");
            Socket socket;
            CustomerHandler customerHandler;

            while (true){
                socket = server.accept();
                customerHandler = new CustomerHandler(socket, loggerHandler, this::inputMessageProcessing);
                customerHandlers.add(customerHandler);
            }
        }
        catch (Exception ex){
            loggerHandler.accept("Ошибка сервера: " + ex.getMessage());
            stop();
        }
        finally {
            loggerHandler.accept("Сервер остановлен.");
        }
    }

    public void stop(){
        loggerHandler.accept("Сервер останавливается.");
        Message logoutMessage = new MessageLogout();
        sendMessageToUsers(logoutMessage, false);
    }

    private void inputMessageProcessing(CustomerHandler customerHandler, Message msg) {
        if(customerHandler.isAlive()){
            switch (msg.getType()){
                case LOGIN:{
                    if(!customerHandler.isAuth()){
                        AuthHandler authHandler = AuthHandler.getInstance();
                        MessageLogin messageLogin = (MessageLogin) msg;
                        Customer customer = authHandler.getCustomer(messageLogin.getLogin(), messageLogin.getPassword());

                        if(customer != null){
                            customerHandler.auth(customer);
                            MessageAuth messageAuth = new MessageAuth(customer);
                            customerHandler.sendMessage(messageAuth);
                        }
                        else {
                            MessageError messageError = new MessageError(Error.ERROR_AUTH_CODE, Error.ERROR_AUTH_TEXT);
                            customerHandler.sendMessage(messageError);
                        }
                    }
                    break;
                }
                case LOGOUT:{
                    if(customerHandler.isAuth()){
                        customerHandler.close();
                        customerHandlers.remove(customerHandler);
                    }
                    break;
                }
                case FILE:{
                    MessageFile messageFile = (MessageFile) msg;

                    try{
                        File file = new File("client/files/" + messageFile.getName());
                        loggerHandler.accept(file.getAbsolutePath());
                        boolean isExist;
                        if(!file.exists()){
                            isExist = file.createNewFile();
                        }

                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(messageFile.getContext());
                        fileOutputStream.close();
                    }
                    catch (IOException ex){
                        loggerHandler.accept(ex.getMessage());
                    }

                    break;
                }

            }
        }
    }

    private void sendMessageToUsers(Message msg, boolean authOnly){
        Iterator<CustomerHandler> userHandlerIterator = customerHandlers.iterator();
        CustomerHandler customerHandler;

        while (userHandlerIterator.hasNext()){
            customerHandler = userHandlerIterator.next();

            if(authOnly && !customerHandler.isAuth()){
                continue;
            }

            customerHandler.sendMessage(msg);
        }
    }
}
