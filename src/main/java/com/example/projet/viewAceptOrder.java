package com.example.projet;

import entities.SupplierOrderEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class viewAceptOrder {

    @FXML
    private TableColumn<SupplierOrderEntity, String> DataEntregueColumn;

    @FXML
    private TableColumn<SupplierOrderEntity, String> DataPedidoColumn;

    @FXML
    private TableColumn<SupplierOrderEntity, String> NomePedidoColumn;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<SupplierOrderEntity, String> estadoColumn;

    @FXML
    private TableColumn<SupplierOrderEntity, Integer> idCollumn;

    @FXML
    private TableView<SupplierOrderEntity> table;

    @FXML
    public void initialize() {
        idCollumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NomePedidoColumn.setCellValueFactory(new PropertyValueFactory<>("nameOrder"));
        DataPedidoColumn.setCellValueFactory(new PropertyValueFactory<>("dateRequested"));
        DataEntregueColumn.setCellValueFactory(new PropertyValueFactory<>("dateProvided"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        List<SupplierOrderEntity> orders = getSupplierOrdersFromDatabase();

        table.getItems().addAll(orders);

        backButton.setOnAction(e -> {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();
        });
    }

    private List<SupplierOrderEntity> getSupplierOrdersFromDatabase() {
        List<SupplierOrderEntity> orders = new ArrayList<>();

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        if (conn != null) {
            try {
                String query = "SELECT * FROM SupplierOrder WHERE state = 'Aceite';";
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
}