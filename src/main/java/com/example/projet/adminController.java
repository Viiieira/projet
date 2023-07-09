package com.example.projet;

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

public class adminController {
    @FXML
    private Button produtosButton;
    @FXML
    private Button FornecedorButton;
    @FXML
    private Button refresh;
    @FXML
    private Button encerrarSessaoButton;
    @FXML
    private Button criarContaButton;
    @FXML
    private TableView<UsersEntity> userTable;
    @FXML
    private TableColumn<UsersEntity, String> tableEmail;
    @FXML
    private TableColumn<UsersEntity, Integer> tableId;
    @FXML
    private TableColumn<UsersEntity, String> tableNif;
    @FXML
    private TableColumn<UsersEntity, String> tableNome;
    @FXML
    private TableColumn<UsersEntity, String> tableTelefone;

    @FXML
    private Button SupplierOrder;

    @FXML
    public void initialize() {
        tableEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        tableNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableTelefone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Get the list of users from the database
        List<UsersEntity> users = getUsersFromDatabase();

        // Add the users to the table
        userTable.getItems().addAll(users);

        // Add event listener to the button
        criarContaButton.setOnAction(e ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("createAccount.fxml"));
                AnchorPane root = loader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show(); // Open the new window


            } catch (IOException a) {
                a.printStackTrace();
            }
        });

        FornecedorButton.setOnAction(e ->{
            openWindow("SupplierController.fxml");
        });

        SupplierOrder.setOnAction(e ->{
            openWindow("SupplierOrderController.fxml");
        });

        refresh.setOnAction(e -> {
            // Clear the existing items in the table
            userTable.getItems().clear();

            // Get the updated list of users from the database
            List<UsersEntity> updatedUsers = getUsersFromDatabase();

            // Add the updated users to the table
            userTable.getItems().addAll(updatedUsers);
        });

        encerrarSessaoButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                AnchorPane root = loader.load();

                Scene scene = new Scene(root);

                String cssPath = Main.class.getResource("styles/main.css").toExternalForm();
                scene.getStylesheets().add(cssPath); // Add CSS file

                Stage newStage = new Stage();
                newStage.setTitle("Login!");
                newStage.setScene(scene);
                newStage.setResizable(false);
                newStage.show();

                // Close the current window
                Stage currentStage = (Stage) encerrarSessaoButton.getScene().getWindow();
                currentStage.close();
            } catch (IOException a) {
                a.printStackTrace();
            }
        });


        userTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica se foi um clique duplo
                // Obtém o usuário selecionado
                UsersEntity selectedUser = userTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    // Abre uma nova janela para exibir os detalhes do usuário
                    openUserDetailsWindow(selectedUser);
                }
            }
        });

        produtosButton.setOnMouseClicked(event ->{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("produtos.fxml"));
            AnchorPane root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show(); // Open the new window
        });
    }

    private List<UsersEntity> getUsersFromDatabase() {
        List<UsersEntity> users = new ArrayList<>();

        dataBaseConnection dbConnection = new dataBaseConnection();
        Connection conn = dbConnection.getConnection();

        String query = "SELECT * FROM users";

        if (conn != null) {
            try {

                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String email = resultSet.getString("email");
                    String nif = resultSet.getString("nif");
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");

                    UsersEntity user = new UsersEntity(id, email, nif, name, phone);
                    users.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection();
            }
        }
        return users;
    }

    private void openUserDetailsWindow(UsersEntity user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detalhesUtilizador.fxml"));
            Parent root = loader.load();

            // Obtém o controlador da nova janela
            detalhesUtilizador detalhesutilizador = loader.getController();

            // Define os detalhes do usuário no controlador da nova janela
            detalhesutilizador.setUserDetails(user);

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        // Limpa os itens existentes na tabela
        userTable.getItems().clear();

        // Obtém a lista atualizada de usuários do banco de dados
        List<UsersEntity> updatedUsers = getUsersFromDatabase();

        // Adiciona os usuários atualizados à tabela
        userTable.getItems().addAll(updatedUsers);
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
