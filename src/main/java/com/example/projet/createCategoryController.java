package com.example.projet;

import entities.UsersEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class createCategoryController {
        @FXML
        private Button cancelarButton;

        @FXML
        private Button criarButton;

        @FXML
        private TextField nomeField;

        private dataBaseConnection dbConnection;

        @FXML
        public void initialize() {
                dbConnection = new dataBaseConnection();

                criarButton.setOnAction(e ->{
                        String name = nomeField.getText();
                        String query = "INSERT INTO productcategory (name) VALUES (?); ";

                        try (Connection connection = dbConnection.getConnection();
                             PreparedStatement statement = connection.prepareStatement(query)) {
                                statement.setString(1, name);

                                statement.executeUpdate();

                                Stage currentStage = (Stage) criarButton.getScene().getWindow();
                                currentStage.close();

                        } catch (SQLException ex) {
                                ex.printStackTrace();
                        }
                });

                cancelarButton.setOnAction(e -> {
                        // Close the current window
                        Stage currentStage = (Stage) cancelarButton.getScene().getWindow();
                        currentStage.close();
                });
        }
}
