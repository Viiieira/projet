package com.example.projet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class acceptOrder {

    private int selectedOrderId;

    public void setSelectedOrderId(int orderId) {
        this.selectedOrderId = orderId + 1;
    }

    @FXML
    private Button CancelarButton;

    @FXML
    private Button confirmarButton;

    private dataBaseConnection dbConnection;

    @FXML
    public void initialize() {
        confirmarButton.setOnAction(e -> {
            dbConnection = new dataBaseConnection();
            LocalDate currentDate = LocalDate.now();

            String queryUpdate = "UPDATE SupplierOrder " +
                    "SET dateprovided = ?, state = 'Aceite' " +
                    "WHERE id = ?";

            String queryUpdate2 = "UPDATE Product p " +
                    "SET stock = p.stock + subquery.total_amount " +
                    "FROM ( " +
                    "    SELECT " +
                    "        p.id AS product_id, " +
                    "        SUM(soi.amount) AS total_amount " +
                    "    FROM " +
                    "        SupplierOrderItem soi " +
                    "        JOIN SupplierOrder so ON so.id = soi.idOrder " +
                    "        JOIN Product p ON p.id = soi.idProduct " +
                    "    WHERE " +
                    "        so.id = ? " +
                    "    GROUP BY " +
                    "        p.id " +
                    ") AS subquery " +
                    "WHERE p.id = subquery.product_id;";

            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(queryUpdate)) {
                statement.setDate(1, java.sql.Date.valueOf(currentDate));
                statement.setInt(2, selectedOrderId);
                statement.executeUpdate();

                try (PreparedStatement statement2 = connection.prepareStatement(queryUpdate2)) {
                    statement2.setInt(1, selectedOrderId);
                    statement2.executeUpdate();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            Stage currentStage = (Stage) confirmarButton.getScene().getWindow();
            currentStage.close();
        });



        CancelarButton.setOnAction(e -> {
            Stage currentStage = (Stage) CancelarButton.getScene().getWindow();
            currentStage.close();
        });

    }

}