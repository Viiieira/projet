package com.example.projet;

import entities.SupplierEntity;
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
import java.util.List;

public class SupplierController {

    @FXML
    private Button adicionarFornecedorButton;

    @FXML
    private Button atualizarbutton;

    @FXML
    private TableView<SupplierEntity> supplierTable;
    @FXML
    private TableColumn<SupplierEntity, String> emailCollun;

    @FXML
    private TableColumn<SupplierEntity, String> idCollun;

    @FXML
    private TableColumn<SupplierEntity, String> nameCollum;

    @FXML
    private Button sairButton;

    @FXML
    private TableColumn<SupplierEntity, String> telefoneCollun;

    @FXML
    public void initialize(){
        idCollun.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCollum.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCollun.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefoneCollun.setCellValueFactory(new PropertyValueFactory<>("phone"));

        List<SupplierEntity> suplliers = getSupllierFromDatabase();

        supplierTable.getItems().addAll(suplliers);

        adicionarFornecedorButton.setOnAction(e ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("createSupllier.fxml"));
                AnchorPane root = loader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException a) {
                a.printStackTrace();
            }
        });

        atualizarbutton.setOnAction(e -> {
            // Clear the existing items in the table
            supplierTable.getItems().clear();

            // Get the updated list of users from the database
            List<SupplierEntity> Updatesuplliers = getSupllierFromDatabase();

            // Add the updated users to the table
            supplierTable.getItems().addAll(Updatesuplliers);
        });

        sairButton.setOnAction(e -> {
            Stage currentStage = (Stage) sairButton.getScene().getWindow();
            currentStage.close();
        });

        supplierTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica se foi um clique duplo
                // Obtém o usuário selecionado
                SupplierEntity selectedSupllier = supplierTable.getSelectionModel().getSelectedItem();
                if (selectedSupllier != null) {
                    // Abre uma nova janela para exibir os detalhes do usuário
                    openUserDetailsWindow(selectedSupllier);
                }
            }
        });
    }

    private List<SupplierEntity> getSupllierFromDatabase() {
        List<SupplierEntity> suplliers = new ArrayList<>();

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        if (conn != null) {
            try {
                String query = "SELECT * FROM supplier";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");

                    SupplierEntity supllier = new SupplierEntity(id, name, email, phone);
                    suplliers.add(supllier);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection();
            }
        }
        return suplliers;
    }

    private void openUserDetailsWindow(SupplierEntity supllier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detalhesSupplier.fxml"));
            Parent root = loader.load();

            // Obtém o controlador da nova janela
            detalhesSupllier detalhessupllier = loader.getController();

            // Define os detalhes do usuário no controlador da nova janela
            detalhessupllier.setSupplierDetails(supllier);

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        // Limpa os itens existentes na tabela
        supplierTable.getItems().clear();

        // Obtém a lista atualizada de usuários do banco de dados
        List<SupplierEntity> Updatesuplliers = getSupllierFromDatabase();

        // Adiciona os usuários atualizados à tabela
        supplierTable.getItems().addAll(Updatesuplliers);
    }
}
