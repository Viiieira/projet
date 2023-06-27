package com.example.projet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class confirmCreateAccount {

    adminController adminControll = new adminController();

    @FXML
    private Button fecharButton;


    @FXML
    public void initialize() {
        // Add event listener to the button
        fecharButton.setOnAction(e -> {
            Stage currentStage = (Stage) fecharButton.getScene().getWindow();
            currentStage.close();
           // adminControll.refreshTable();
        });
    }


}
