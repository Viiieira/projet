package com.example.projet;

import entities.SupplierOrderEntity;
import jakarta.persistence.criteria.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class detalhesOrder {

    @FXML
    private Button AcceptButton;

    @FXML
    private TableColumn<OrderTable, String> CategoriaCollumn;
    @FXML
    private TableColumn<OrderTable, String> FornecedorColumn;

    @FXML
    private TableColumn<OrderTable, String> ProdutoColumn;

    @FXML
    private TableColumn<OrderTable, Integer> QuantidadeColumn;

    @FXML
    private Button sairBottun;

    @FXML
    private TableView<OrderTable> table;

    private int orderDetails;

    @FXML
    public void initialize() {
        FornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        ProdutoColumn.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        CategoriaCollumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        QuantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));


        AcceptButton.setOnAction(e -> {
            OrderTable selectedOrder = table.getSelectionModel().getSelectedItem();
            int selectedOrderId = getOrderDetailsFromDatabase().indexOf(selectedOrder) + 1;

            openWindow("acceptOrder.fxml", selectedOrderId);
        });

        sairBottun.setOnAction(e -> {
            // Fechar a janela atual
            Stage currentStage = (Stage) sairBottun.getScene().getWindow();
            currentStage.close();
        });

        // Obter os dados da consulta SQL
        List<OrderTable > orderDetails = getOrderDetailsFromDatabase();

        // Preencher a tabela com os dados
        table.getItems().addAll(orderDetails);
    }

    public class OrderTable  {
        private String fornecedor;
        private String nomeProduto;
        private int quantidade;
        private String categoria;
        public OrderTable (String fornecedor, String nomeProduto, String categoria,int quantidade) {
            this.fornecedor = fornecedor;
            this.nomeProduto = nomeProduto;
            this.categoria = categoria;
            this.quantidade = quantidade;
        }
        public String getCategoria(){return categoria;}
        public void setCategoria(){this.categoria = categoria;}
        public String getFornecedor() {
            return fornecedor;
        }
        public void setFornecedor(String fornecedor) {
            this.fornecedor = fornecedor;
        }
        public String getNomeProduto() {
            return nomeProduto;
        }
        public void setNomeProduto(String nomeProduto) {
            this.nomeProduto = nomeProduto;
        }
        public int getQuantidade() {
            return quantidade;
        }
        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }
    }

    private List<OrderTable > getOrderDetailsFromDatabase() {
        int idSupplierOrder = orderDetails + 1;
        System.out.println(idSupplierOrder);

        List<OrderTable > orderDetails = new ArrayList<>();

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        String query = "SELECT\n" +
                "    p.name AS product_name,\n" +
                "    soi.amount AS amount,\n" +
                "    pc.name AS category_name,\n" +
                "    s.name AS supplier_name\n" +
                "FROM\n" +
                "    SupplierOrderItem soi\n" +
                "    JOIN Product p ON p.id = soi.idProduct\n" +
                "    JOIN ProductCategory pc ON pc.id = p.category\n" +
                "    JOIN Supplier s ON s.id = soi.idSupllier\n" +
                "\tJOIN SupplierOrder so ON so.id = soi.idOrder  \n" +
                "\twhere so.id = 1;\n";

        if (conn != null) {
            try {
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    // Obtenha os dados do resultado e crie um objeto Table
                    String fornecedor = resultSet.getString("supplier_name");
                    String nomeProduto = resultSet.getString("product_name");
                    String categoria = resultSet.getString("category_name");
                    int quantidade = resultSet.getInt("amount");

                    OrderTable  table = new OrderTable (fornecedor, nomeProduto, categoria, quantidade);
                    orderDetails.add(table);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection();
            }
        }

        return orderDetails;
    }
    public void setOrderDetails(int order) {

    }

    private void openWindow(String fxmlFile, int selectedOrderId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();

            // Obter a instância da classe acceptOrder
            acceptOrder acceptOrderController = loader.getController();
            acceptOrderController.setSelectedOrderId(selectedOrderId);

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
