package com.example.projet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class managerController {
    @FXML
    private Button encerrarSessaoButton;
    @FXML
    private Button produtosButton;
    @FXML
    private Button SupplierOrder;

    @FXML
    public void initialize() {
        // Add event listener to the button
        encerrarSessaoButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                AnchorPane root = loader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show(); // Open the new window

                // Close the current window
                Stage currentStage = (Stage) encerrarSessaoButton.getScene().getWindow();
                currentStage.close();
            } catch (IOException a) {
                a.printStackTrace();
            }
        });

        SupplierOrder.setOnAction(e ->{
            openWindow("SupplierOrderController.fxml");
        });

        produtosButton.setOnMouseClicked(event ->{
            openWindow("produtos.fxml");
        });
    }

    private void openWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

