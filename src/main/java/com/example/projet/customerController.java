package com.example.projet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class customerController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button encerrarSessaoButton;

    @FXML
    private Button consultarComprasioButton;

    @FXML
    private Text adicionarCoisasText;

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
        }
}

