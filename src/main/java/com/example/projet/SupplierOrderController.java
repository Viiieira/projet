package com.example.projet;

import entities.SupplierOrderEntity;
import entities.UsersEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SupplierOrderController {

    @FXML
    private TableColumn<SupplierOrderEntity, String> EstadoCollum;

    @FXML
    private Button ViewAceptOrderButton;

    @FXML
    private Button createOrderButton;

    @FXML
    private TableColumn<SupplierOrderEntity, Date> dataCollum;

    @FXML
    private TableColumn<SupplierOrderEntity, Integer> idCollumn;

    @FXML
    private TableColumn<SupplierOrderEntity, String> nomeCollum;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<SupplierOrderEntity> table;

    @FXML
    private Button voltarButton;

    @FXML
    public void initialize() {
        idCollumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCollum.setCellValueFactory(new PropertyValueFactory<>("nameOrder"));
        dataCollum.setCellValueFactory(new PropertyValueFactory<>("dateRequested"));
        EstadoCollum.setCellValueFactory(new PropertyValueFactory<>("state"));

        List<SupplierOrderEntity> orders = getSupplierOrdersFromDatabase();

        table.getItems().addAll(orders);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica se foi um clique duplo
                // Obtém o usuário selecionado
                SupplierOrderEntity selectedOrder = table.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    int orderId = selectedOrder.getId();
                    // Abre uma nova janela para exibir os detalhes do usuário
                    openOrderDetailsWindow(orderId);
                }
            }
        });

        ViewAceptOrderButton.setOnAction(e -> {
            openWindow("viewAceptOrder.fxml");
        });

        createOrderButton.setOnAction(e -> {
            openWindow("createOrder.fxml");
        });

        refreshButton.setOnAction(e -> {
            // Limpar os itens existentes na tabela
            table.getItems().clear();

            // Obter a lista atualizada de pedidos do fornecedor do banco de dados
            List<SupplierOrderEntity> updatedOrders = getSupplierOrdersFromDatabase();

            // Adicionar os pedidos atualizados à tabela
            table.getItems().addAll(updatedOrders);
        });

        voltarButton.setOnAction(e -> {
            closeApplication();
        });
    }

    private List<SupplierOrderEntity> getSupplierOrdersFromDatabase() {
        List<SupplierOrderEntity> orders = new ArrayList<>();

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        if (conn != null) {
            try {
                String query = "SELECT * FROM SupplierOrder WHERE state != 'Aceite';";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String nameOrder = resultSet.getString("nameOrder");
                    Date dateRequested = resultSet.getDate("dateRequested");
                    Date dateProvided = resultSet.getDate("dateProvided");
                    String state = resultSet.getString("state");

                    SupplierOrderEntity order = new SupplierOrderEntity(id, nameOrder, dateRequested, dateProvided, state);
                    orders.add(order);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection();
            }
        }
        return orders;
    }

    private void openOrderDetailsWindow(int order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detalhesOrder.fxml"));
            Parent root = loader.load();

            // Obtém o controlador da nova janela
            detalhesOrder detalhesoder = loader.getController();

            // Define os detalhes do pedido no controlador da nova janela
            detalhesoder.setOrderDetails(order);


            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeApplication() {
        Stage currentStage = (Stage) voltarButton.getScene().getWindow();
        currentStage.close();
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
}
