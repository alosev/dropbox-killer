package controllers;

import entity.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.messages.MessageFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Client client;

    public void btnAuthAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auth.fxml"));
            Parent root = loader.load();
            AuthController authController = (AuthController) loader.getController();
            authController.setClient(client);

            stage.setTitle("JavaFX Autorization");
            stage.setScene(new Scene(root, 400, 200));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnRegistrationAction(ActionEvent actionEvent) {
    }

    public void btnSendFileAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            MessageFile messageFile = new MessageFile(file);
            client.sendMessage(messageFile);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client("localhost", 8196, System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
    }


}
