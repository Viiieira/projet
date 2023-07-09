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

public class createSupllierController {

    @FXML
    private Label NomeErroLabel;

    @FXML
    private Label emailLabelError;

    @FXML
    private Label PhoneLabelError;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button criarButton;

    @FXML
    private TextField emailField;

    @FXML
    private Label labelDados;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField phoneField;

    private dataBaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new dataBaseConnection();

        criarButton.setOnAction(e -> {
            String email = emailField.getText();
            String nome = nomeField.getText();
            String telemovel = phoneField.getText();


            if (AnalisarDados(nome, email, telemovel)) {
                dbConnection = new dataBaseConnection();
                insertSupllier(nome, email, telemovel);
            }
        });
    }

    private boolean AnalisarDados(String name, String email, String phone) {
        dbConnection = new dataBaseConnection();

        boolean isValid = true;

        // Verificar se todos os campos estão preenchidos
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            isValid = false;
            labelDados.setText("Insira todos os dados");
            return isValid;
        } else {
            labelDados.setText("");
        }


        try (Connection connection = dbConnection.getConnection()) {
            // Verificar se o nome já existe
            String namesQuery = "SELECT name FROM supplier WHERE name = ?";
            PreparedStatement namesStatement = connection.prepareStatement(namesQuery);
            namesStatement.setString(1, name);
            ResultSet namesResultSet = namesStatement.executeQuery();

            if (namesResultSet.next()) {
                isValid = false;
                NomeErroLabel.setText("O nome já existe");
            } else {
                NomeErroLabel.setText("");
            }

            // Verificar se o e-mail já existe
            String emailQuery = "SELECT email FROM supplier WHERE email = ?";
            PreparedStatement emailStatement = connection.prepareStatement(emailQuery);
            emailStatement.setString(1, email);
            ResultSet emailResultSet = emailStatement.executeQuery();

            if (!email.contains("@") || !email.contains(".") || email.indexOf("@") > email.indexOf(".")) {
                isValid = false;
                emailLabelError.setText("Insira um email válido");
            } else if (emailResultSet.next()) {
                isValid = false;
                emailLabelError.setText("O e-mail já existe");
            } else {
                emailLabelError.setText("");
            }

            // Verificar se o número de telefone já existe
            String phoneQuery = "SELECT phone FROM supplier WHERE phone = ?";
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
                PhoneLabelError.setText("Insira apenas numeros");
            }else if (phone.length() != 9) {
                isValid = false;
                PhoneLabelError.setText("Insira 9 dígitos");
            } else if (phoneResultSet.next()) {
                isValid = false;
                PhoneLabelError.setText("Este número já existe");
            } else {
                PhoneLabelError.setText("");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            isValid = false;
        }

        return isValid;
    }

    private void insertSupllier(String name, String email, String phone) {
        dbConnection = new dataBaseConnection();
        String query = "INSERT INTO supplier (id, name, email, phone) VALUES (DEFAULT, ?, ?, ?);";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);

            statement.executeUpdate();

            Stage currentStage = (Stage) cancelarButton.getScene().getWindow();
            currentStage.close();


        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
