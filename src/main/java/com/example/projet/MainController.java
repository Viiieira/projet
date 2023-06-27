package com.example.projet;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button buttonEntrar;

    @FXML
    private Label labelMensage;

    private dataBaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new dataBaseConnection();

        // Add event listener to the button
        buttonEntrar.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (validateLogin(username, password)) {
                // Perform further actions after successful login
            } else {
                labelMensage.setText("Invalid username or password");
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        try (Connection conn = dbConnection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE name = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                PreparedStatement roleStatement = conn.prepareStatement("SELECT * FROM customer WHERE id = ?");
                roleStatement.setInt(1, id);
                ResultSet roleResultSet = roleStatement.executeQuery();
                if (roleResultSet.next()) {
                    labelMensage.setText("Conectou um customer");

                    // Close the current scene
                    Stage currentStage = (Stage) labelMensage.getScene().getWindow();
                    currentStage.close();

                    // Open a new scene for the customer
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("customer.fxml"));
                    Parent root = loader.load();
                    Scene customerScene = new Scene(root);

                    Stage customerStage = new Stage();
                    customerStage.setScene(customerScene);
                    customerStage.show();

                } else {
                    PreparedStatement managerStatement = conn.prepareStatement("SELECT * FROM manager WHERE id = ?");
                    managerStatement.setInt(1, id);
                    ResultSet managerResultSet = managerStatement.executeQuery();
                    if (managerResultSet.next()) {
                        labelMensage.setText("Conectou um manager");

                        // Close the current scene
                        Stage currentStage = (Stage) labelMensage.getScene().getWindow();
                        currentStage.close();

                        // Open a new scene for the customer
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager.fxml"));
                        Parent root = loader.load();
                        Scene customerScene = new Scene(root);

                        Stage customerStage = new Stage();
                        customerStage.setScene(customerScene);
                        customerStage.show();

                    } else {
                        PreparedStatement adminStatement = conn.prepareStatement("SELECT * FROM admin WHERE id = ?");
                        adminStatement.setInt(1, id);
                        ResultSet adminResultSet = adminStatement.executeQuery();
                        if (adminResultSet.next()) {
                            labelMensage.setText("Conectou um admin");

                            // Close the current scene
                            Stage currentStage = (Stage) labelMensage.getScene().getWindow();
                            currentStage.close();

                            // Open a new scene for the customer
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
                            Parent root = loader.load();
                            Scene customerScene = new Scene(root);

                            Stage customerStage = new Stage();
                            customerStage.setScene(customerScene);
                            customerStage.show();
                        }
                    }
                }
                return true;
            } else {
                labelMensage.setText("Invalid username or password");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            labelMensage.setText("Error connecting to database");
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
