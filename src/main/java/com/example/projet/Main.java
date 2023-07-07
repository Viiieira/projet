package com.example.projet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {
    private dataBaseConnection dbConnection;
    @Override
    public void start(Stage stage) throws IOException {
        dbConnection = new dataBaseConnection();

        // Verificar se a tabela 'users' existe no banco de dados
        if (isTableExist("users")) {
            // Se a tabela não existir, criar um novo usuário admin
            createAdminUser();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        String cssPath = Objects.requireNonNull(Main.class.getResource("styles/main.css")).toExternalForm();
        scene.getStylesheets().add(cssPath);

        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private boolean isTableExist(String tableName) {
        int i = 0;
        String CountUser = "SELECT COUNT(*) AS name FROM Users;";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(CountUser);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                i = resultSet.getInt("name");
                System.out.println(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }

        if(i == 0){
            return true;
        }
        return false;
    }

    private void createAdminUser() {
        dbConnection = new dataBaseConnection();
        String insertAdmin = "INSERT INTO Users (name, email, phone, nif, password)\n" +
                "VALUES ('a', 'a@admin.com', '964159753', '741369852', 'a');\n" +
                "INSERT INTO admin (id)\n" +
                "VALUES (LASTVAL());" +
                "INSERT INTO productcategory (name) VALUES ('Marca Branca');";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statementInsert = connection.prepareStatement(insertAdmin)) {
            statementInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
