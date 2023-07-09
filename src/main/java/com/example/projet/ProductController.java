package com.example.projet;

import entities.ProductEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductController {

    @FXML
    private Button refresh;
    @FXML
    private Button addCategory;
    @FXML
    private Button addProduto;
    @FXML
    private TableColumn<ProductEntity, String> tableCategoria;
    @FXML
    private TableColumn<ProductEntity, Integer> tableId;
    @FXML
    private TableColumn<ProductEntity, String> tableNome;
    @FXML
    private TableColumn<ProductEntity, Float> tablePreco;
    @FXML
    private TableView<ProductEntity> tableProduct;
    @FXML
    private TableColumn<ProductEntity, Integer> tableQuantidade;
    @FXML
    private Button voltarButton;

    private final ObservableList<ProductEntity> products = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCategoria.setCellValueFactory(new PropertyValueFactory<>("category"));
        tablePreco.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableQuantidade.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tableProduct.setItems(products);

        // Load initial products from database
        loadProductsFromDatabase();

        addProduto.setOnAction(e -> {
            openWindow("createProduct.fxml");
        });

        addCategory.setOnAction(e -> {
            openWindow("createCategory.fxml");
        });


        refresh.setOnAction(e -> {
            // Clear the existing items in the table
            products.clear();

            // Load products from the database
            loadProductsFromDatabase();
        });

        voltarButton.setOnAction(e -> {
            Stage currentStage = (Stage) voltarButton.getScene().getWindow();
            currentStage.close();
        });

        tableProduct.setOnMouseClicked(this::handleDoubleClick);
    }

    private void loadProductsFromDatabase() {
        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        if (conn != null) {
            try {
                String query = "SELECT * FROM product";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int category = resultSet.getInt("category");
                    int priceInt = resultSet.getInt("price");
                    BigInteger price = BigInteger.valueOf(priceInt);
                    int stock = resultSet.getInt("stock");

                    ProductEntity product = new ProductEntity(id, name, category, price, stock);
                    products.add(product);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                dbConnection.closeConnection();
            }
        }
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

    private void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ProductEntity selectedProduct = tableProduct.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                openProductDetailsWindow(selectedProduct);
            }
        }
    }

    private void openProductDetailsWindow(ProductEntity product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detalhesProduto.fxml"));
            Parent root = loader.load();

            detalhesProduto detalhesProdutoController = loader.getController();
            detalhesProdutoController.setProductDetails(product);

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
