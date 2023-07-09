package com.example.projet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class createCategoryController {
        @FXML
        private Label EroorLabel;
        @FXML
        private Button cancelarButton;

        @FXML
        private Button criarButton;

        @FXML
        private TextField nomeField;

        private dataBaseConnection dbConnection;

        @FXML
        public void initialize() {
                criarButton.setOnAction(e -> {
                        dbConnection = new dataBaseConnection();
                        String name = nomeField.getText();
                        String queryCheck = "SELECT * FROM productcategory WHERE name = ?;";
                        String queryInsert = "INSERT INTO productcategory (name) VALUES (?); ";

                        try (Connection connection = dbConnection.getConnection();
                             PreparedStatement statementCheck = connection.prepareStatement(queryCheck);
                             PreparedStatement statementInsert = connection.prepareStatement(queryInsert)) {

                                statementCheck.setString(1, name);
                                ResultSet resultSet = statementCheck.executeQuery();

                                if (resultSet.next()) {
                                        EroorLabel.setText("A categoria já existe!");
                                } else {
                                        dbConnection = new dataBaseConnection();

                                        statementInsert.setString(1, name);
                                        statementInsert.executeUpdate();
                                        Stage currentStage = (Stage) criarButton.getScene().getWindow();
                                        currentStage.close();
                                }
                        } catch (SQLException ex) {
                                ex.printStackTrace();
                        }
                });

                cancelarButton.setOnAction(e -> {
                        // Fechar a janela atual
                        Stage currentStage = (Stage) cancelarButton.getScene().getWindow();
                        currentStage.close();
                });
        }
}

