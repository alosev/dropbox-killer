package controllers;

import entity.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.messages.Message;
import models.messages.MessageAuth;
import models.messages.MessageError;
import models.messages.MessageLogin;

public class AuthController {
    @FXML
    TextField login;

    @FXML
    PasswordField password;

    @FXML
    VBox globParent;

    private Client client;

    public void btnAuthAction(ActionEvent actionEvent) {
        String login = this.login.getText();
        String password = this.password.getText();

        MessageLogin messageLogin = new MessageLogin(login, password);
        client.sendMessage(messageLogin);
    }

    public void setClient(Client client){
        this.client = client;
        client.setMessageHandler(this::messageProcessing);
    }

    private void messageProcessing(Message msg){
        switch (msg.getType()){
            case AUTH:{
                MessageAuth messageAuth = (MessageAuth)msg;
                client.auth(messageAuth.getCredentials());
                client.setMessageHandler(null);
                globParent.getScene().getWindow().hide();
                break;
            }
            case ERROR:{
                MessageError messageError = (MessageError) msg;
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, messageError.getText());
                    alert.show();
                });
                break;
            }
        }
    }

}
