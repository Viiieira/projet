package com.example.projet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class createProductController {

    @FXML
    private Label labelDados;

    @FXML
    private Label NomeErroLabel;

    @FXML
    private Label PrecoLabelError;

    @FXML
    private TextField precoField;

    @FXML
    private Button cancelarButton;

    @FXML
    private ComboBox<String> comboCategoria;
    private int selectedCategoryId;

    @FXML
    private Button criarButton;

    @FXML
    private TextField nomeField;

    private dataBaseConnection dbConnection;


    @FXML
    public void initialize() {
        dbConnection = new dataBaseConnection();

        // Obtenha os nomes das categorias do banco de dados
        List<String> categorias = getCategoryFromDatabase();

        // Preencha a ComboBox com os nomes das categorias
        comboCategoria.getItems().addAll(categorias);

        comboCategoria.setOnAction(e -> {
            String selectedCategory = comboCategoria.getValue();
            selectedCategoryId = getCategoryId(selectedCategory);
        });

        comboCategoria.setValue("Marca Branca");

        criarButton.setOnAction(e -> {
            String nome = nomeField.getText();
            String precoText = precoField.getText();
            String categoria = comboCategoria.getValue();

            if (nome.isEmpty() || precoText.isEmpty() || categoria == null) {
                labelDados.setText("Preencha todos os campos");
                return;
            }

            if (analisarDados(nome, precoText)) {
                float preco = Float.parseFloat(precoText);
                int categoryId = getCategoryId(categoria);
                insertProduct(nome, preco, categoryId);
            }
        });

        cancelarButton.setOnAction(e -> {
            // Feche a janela atual
            Stage currentStage = (Stage) cancelarButton.getScene().getWindow();
            currentStage.close();
        });
    }

    private int getCategoryId(String categoryName) {
        int categoryId = -1; // Valor padrão para indicar que não foi encontrado

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        if (conn != null) {
            try {
                String query = "SELECT id FROM ProductCategory WHERE name = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, categoryName);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    categoryId = resultSet.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection();
            }
        }
        System.out.println(categoryId);
        return categoryId;
    }

    private List<String> getCategoryFromDatabase() {
        List<String> categories = new ArrayList<>();

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        if (conn != null) {
            try {
                String query = "SELECT name FROM ProductCategory";
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

    private boolean analisarDados(String nome, String numero) {
        dbConnection = new dataBaseConnection();

        if (nome.isEmpty() ||  numero.isEmpty()) {
            labelDados.setText("Insira todos os dados");
            return false;
        } else {
            labelDados.setText("");
        }

        try {
            float preco = Float.parseFloat(numero);
            if (preco < 0) {
                PrecoLabelError.setText("Não insira preços negativos");
                return false;
            } else {
                PrecoLabelError.setText("");
            }
        } catch (NumberFormatException ex) {
            PrecoLabelError.setText("Nao insira o preço com letras");
            return false;
        }

        try (Connection connection = dbConnection.getConnection()) {
            // Verificar se o nome já existe
            String namesQuery = "SELECT name FROM product WHERE name = ?";
            PreparedStatement namesStatement = connection.prepareStatement(namesQuery);
            namesStatement.setString(1, nome);
            ResultSet namesResultSet = namesStatement.executeQuery();

            if (namesResultSet.next()) {
                NomeErroLabel.setText("O nome do produto já existe");
                return false;
            } else {
                NomeErroLabel.setText("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true;
    }

    private void insertProduct(String nome, float preco, int categoria) {
        dbConnection = new dataBaseConnection();
        int quantidade = 0;
        String query = "INSERT INTO Product (name, price, category, stock) VALUES (?, ?, ? , ?); ";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            statement.setFloat(2, preco);
            statement.setInt(3, categoria);
            statement.setInt(4, quantidade);

            statement.executeUpdate();

            Stage currentStage = (Stage) criarButton.getScene().getWindow();
            currentStage.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean verificarLetras(String numero) {
        for (int i = 0; i < numero.length(); i++) {
            if (Character.isLetter(numero.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
