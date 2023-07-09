package com.example.projet;

import entities.SupplierOrderEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class detalhesOrder {

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
        QuantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));


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

        public OrderTable (String fornecedor, String nomeProduto, int quantidade) {
            this.fornecedor = fornecedor;
            this.nomeProduto = nomeProduto;
            this.quantidade = quantidade;
        }

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

        String query = "SELECT p.name AS product_name, p.stock, s.name AS supplier_name\n" +
                "FROM SupplierOrderItem soi\n" +
                "JOIN Product p ON soi.idProduct = p.id\n" +
                "JOIN SupplierOrder so ON soi.idOrder = so.id\n" +
                "JOIN Supplier s ON so.idSupplier = s.id\n" +
                "WHERE so.id = '" + idSupplierOrder + "';";

        if (conn != null) {
            try {
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    // Obtenha os dados do resultado e crie um objeto Table
                    String fornecedor = resultSet.getString("supplier_name");
                    String nomeProduto = resultSet.getString("product_name");
                    int quantidade = resultSet.getInt("stock");

                    OrderTable  table = new OrderTable (fornecedor, nomeProduto, quantidade);
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
}
