package com.example.projet;

import entities.ProductCategoryEntity;
import entities.UsersEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class createProductController {

    @FXML
    private TextField precoField;

    @FXML
    private Button cancelarButton;

    @FXML
    private ComboBox<String> comboCategoria;

    @FXML
    private Button criarButton;

    @FXML
    private TextField nomeField;

    private dataBaseConnection dbConnection;


    @FXML
    public void initialize() {
        dbConnection = new dataBaseConnection();

        // Obtenha os nomes das categorias do banco de dados
        List<String> categorias = getCategotyFromDatabase();

        // Preencha a ComboBox com os nomes das categorias
        comboCategoria.getItems().addAll(categorias);

        criarButton.setOnAction(e ->{
            String name = nomeField.getText();
            Float preco = Float.valueOf(precoField.getText());
            String query = "INSERT INTO product (name, price) VALUES (?, ?); ";

            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setFloat(2, preco); // Corrigido para o índice 2

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

    private List<String> getCategotyFromDatabase() {
        List<String> categories = new ArrayList<>();

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        if (conn != null) {
            try {
                String query = "SELECT * FROM productcategory";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    categories.add(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection();
            }
        }
        return categories;
    }

}

