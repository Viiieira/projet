package com.example.projet;

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

import com.example.projet.createOrder.TableOrder;

public class adicionarOrder {

    @FXML
    private TextField amountField;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> categoriaComboBox;

    @FXML
    private Button confirmarButton;


    @FXML
    private ComboBox<String> produtoComboBox;

    private createOrder parentController;

    private dataBaseConnection dbConnection;

    @FXML
    private void initialize() {
        dbConnection = new dataBaseConnection();

        categoriaComboBox.setDisable(false);
        produtoComboBox.setDisable(true);
        amountField.setDisable(true);

        categoriaComboBox.setOnAction(e -> {
            String categoriaComboBoxValue = categoriaComboBox.getValue();
            if (categoriaComboBoxValue != null) {
                produtoComboBox.setDisable(false);
                try {
                    loadProductsByCategory(categoriaComboBoxValue);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                produtoComboBox.setDisable(true);
                produtoComboBox.getItems().clear();
            }
        });

        produtoComboBox.setOnAction(e -> {
            String produtoComboBoxValue = produtoComboBox.getValue();
            if (produtoComboBoxValue != null) {
                amountField.setDisable(false);
            } else {
                amountField.setDisable(true);
            }
        });

        confirmarButton.setOnAction(e -> {
            String produtoComboBoxValue = produtoComboBox.getValue();
            int amountFieldValue = Integer.parseInt(amountField.getText());
            String categoriaComboBoxValue = categoriaComboBox.getValue();

            if (categoriaComboBoxValue != null && produtoComboBoxValue != null) {
                TableOrder tableOrder = new TableOrder(produtoComboBoxValue, amountFieldValue, categoriaComboBoxValue);

                parentController.addTableOrder(tableOrder);

                Stage currentStage = (Stage) confirmarButton.getScene().getWindow();
                currentStage.close();
            }
        });

        try (Connection connection = dbConnection.getConnection()) {
            String categoriaQuery = "SELECT name FROM ProductCategory";
            try (PreparedStatement categoriaStatement = connection.prepareStatement(categoriaQuery);
                 ResultSet categoriaResultSet = categoriaStatement.executeQuery()) {
                List<String> nomesDasCategorias = new ArrayList<>();
                while (categoriaResultSet.next()) {
                    String nomeCategoria = categoriaResultSet.getString("name");
                    nomesDasCategorias.add(nomeCategoria);
                }
                categoriaComboBox.getItems().addAll(nomesDasCategorias);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setParentController(createOrder parentController) {
        this.parentController = parentController;
    }

    private void loadProductsByCategory(String categoryName) throws SQLException {
        dbConnection = new dataBaseConnection();
        produtoComboBox.getItems().clear();
        String produtoQuery = "SELECT p.name " +
                "FROM Product p " +
                "JOIN ProductCategory pc ON pc.id = p.category " +
                "WHERE pc.name = ?";
        dbConnection = new dataBaseConnection();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement produtoStatement = connection.prepareStatement(produtoQuery)) {
            produtoStatement.setString(1, categoryName);
            try (ResultSet produtoResultSet = produtoStatement.executeQuery()) {
                List<String> nomesDosProdutos = new ArrayList<>();
                while (produtoResultSet.next()) {
                    String nomeProduto = produtoResultSet.getString("name");
                    nomesDosProdutos.add(nomeProduto);
                }
                produtoComboBox.getItems().addAll(nomesDosProdutos);
            }
        }
    }
}
