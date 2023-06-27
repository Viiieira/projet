package com.example.projet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class createAccountController {
    @FXML
    private Label labelDados;
    @FXML
    private Button cancelField;
    @FXML
    private ComboBox<String> combo;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField emailField;
    @FXML
    private Label labelConfirmPassla;
    @FXML
    private Label labelemail;
    @FXML
    private Label labelnif;

    @FXML
    private Label labelnome;

    @FXML
    private Label labelPassword;

    @FXML
    private Label labeltelemovel;

    @FXML
    private TextField nifField;

    @FXML
    private TextField nomeField;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField telemovelField;

    @FXML
    private PasswordField ConfirmPassField;

    @FXML
    private Label labelpassword;
    private dataBaseConnection dbConnection;

    @FXML
    public String Select(ActionEvent event) {
        String s = combo.getSelectionModel().getSelectedItem().toString();
        System.out.println(s);
        return s;
    }

    @FXML
    public void initialize() {
        dbConnection = new dataBaseConnection();

        ObservableList<String> list = FXCollections.observableArrayList("admin", "customer", "manager");
        combo.setItems(list);

        combo.setValue("customer");

        cancelField.setOnAction(e -> {
            Stage currentStage = (Stage) cancelField.getScene().getWindow();
            currentStage.close();
        });

        confirmButton.setOnAction(e -> {
            String email = emailField.getText();
            String nif = nifField.getText();
            String nome = nomeField.getText();
            String password = passField.getText();
            String telemovel = telemovelField.getText();
            String utilizador = Select(null);

            if (AnalisarDados(nome, email, telemovel, nif, password, utilizador)) {
                dbConnection = new dataBaseConnection();
                insertUser(nome, email, telemovel, nif, password, utilizador);
            }
        });
    }

    private boolean AnalisarDados(String name, String email, String phone, String nif, String password, String utilizador) {
        dbConnection = new dataBaseConnection();

        boolean isValid = true;
        String confirmarPassword = ConfirmPassField.getText();

        // Verificar se todos os campos estão preenchidos
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || nif.isEmpty() || password.isEmpty() || utilizador.isEmpty()) {
            isValid = false;
            labelDados.setText("Insira todos os dados");
            return isValid;
        } else {
            labelDados.setText("");
        }

        if (!password.equals(confirmarPassword)) {
            isValid = false;
            labelpassword.setText("As senhas não coincidem");
            ConfirmPassField.setText("");
            passField.setText("");
        } else {
            labelpassword.setText("");
        }

        try (Connection connection = dbConnection.getConnection()) {
            // Verificar se o nome já existe
            String namesQuery = "SELECT name FROM users WHERE name = ?";
            PreparedStatement namesStatement = connection.prepareStatement(namesQuery);
            namesStatement.setString(1, name);
            ResultSet namesResultSet = namesStatement.executeQuery();

            if (namesResultSet.next()) {
                isValid = false;
                labelnome.setText("O nome já existe");
            } else {
                labelnome.setText("");
            }

            // Verificar se o e-mail já existe
            String emailQuery = "SELECT email FROM users WHERE email = ?";
            PreparedStatement emailStatement = connection.prepareStatement(emailQuery);
            emailStatement.setString(1, email);
            ResultSet emailResultSet = emailStatement.executeQuery();

            if (!email.contains("@") || !email.contains(".") || email.indexOf("@") > email.indexOf(".")) {
                isValid = false;
                labelemail.setText("Insira um email válido");
            } else if (emailResultSet.next()) {
                isValid = false;
                labelemail.setText("O e-mail já existe");
            } else {
                labelemail.setText("");
            }

            // Verificar se o número de telefone já existe
            String phoneQuery = "SELECT phone FROM users WHERE phone = ?";
            PreparedStatement phoneStatement = connection.prepareStatement(phoneQuery);
            phoneStatement.setString(1, phone);
            ResultSet phoneResultSet = phoneStatement.executeQuery();

            boolean containsNonDigits = false;
            for (char c : phone.toCharArray()) {
                if (!Character.isDigit(c)) {
                    containsNonDigits = true;
                    break;
                }
            }
            if (containsNonDigits) {
                isValid = false;
                labeltelemovel.setText("Insira apenas numeros");
            }else if (phone.length() != 9) {
                isValid = false;
                labeltelemovel.setText("Insira 9 dígitos");
            } else if (phoneResultSet.next()) {
                isValid = false;
                labeltelemovel.setText("Este número já existe");
            } else {
                labeltelemovel.setText("");
            }

            // Verificar se o NIF já existe
            String nifQuery = "SELECT nif FROM users WHERE nif = ?";
            PreparedStatement nifStatement = connection.prepareStatement(nifQuery);
            nifStatement.setString(1, nif);
            ResultSet nifResultSet = nifStatement.executeQuery();

            boolean containsNonDigitsNif = false;
            for (char c : nif.toCharArray()) {
                if (!Character.isDigit(c)) {
                    containsNonDigitsNif = true;
                    break;
                }
            }
            if (containsNonDigits) {
                isValid = false;
                labelnif.setText("Insira apenas numeros");
            } else if (nif.length() != 9) {
                isValid = false;
                labelnif.setText("Insira 9 dígitos");
            } else if (nifResultSet.next()) {
                isValid = false;
                labelnif.setText("Este NIF já existe");
            } else {
                labelnif.setText("");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            isValid = false;
        }

        return isValid;
    }

    private void insertUser(String name, String email, String phone, String nif, String password, String utilizador) {
        dbConnection = new dataBaseConnection();
        String query = "INSERT INTO users (name, email, phone, nif, password) VALUES (?, ?, ?, ?, ?); " +
                "INSERT INTO " + utilizador + "(id) VALUES (LASTVAL());";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, nif);
            statement.setString(5, password);

            statement.executeUpdate();

            Stage currentStage = (Stage) cancelField.getScene().getWindow();
            currentStage.close();

            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmCreateAccount.fxml"));
            Parent root = loader.load();
            Scene confirmScene = new Scene(root);

            Stage confirmStage = new Stage();
            confirmStage.setScene(confirmScene);
            confirmStage.show();
            */

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       /* } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }
}
