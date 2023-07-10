package com.example.projet;

import entities.UsersEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class createOrder {

    @FXML
    private Button adicionarProdutoButton;

    @FXML
    private ComboBox<String> FornecedorCombobox;

    @FXML
    private Button confirmarButton;

    @FXML
    private TableColumn<TableOrder, String> supplierColumn;

    @FXML
    private Button voltarButton;

    @FXML
    private TableColumn<TableOrder, Integer> amountColumn;

    @FXML
    private TableColumn<TableOrder, String> categoryColumn;

    @FXML
    private TableColumn<TableOrder, String> nomeProdutoColumn;

    @FXML
    private TableView<TableOrder> table;

    private List<TableOrder> tableOrders = new ArrayList<>();

    private dataBaseConnection dbConnection;

    public void addTableOrder(TableOrder tableOrder) {
        tableOrders.add(tableOrder);
        table.getItems().add(tableOrder);
    }

    public static class TableOrder {
        private String nome;
        private int amount;
        private String category;

        public TableOrder(String nome, int amount, String category) {
            this.nome = nome;
            this.amount = amount;
            this.category = category;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    @FXML
    public void initialize() {
        dbConnection = new dataBaseConnection();

        nomeProdutoColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        adicionarProdutoButton.setOnAction(e -> {
            openWindow("adicionarOrder.fxml");
        });

        voltarButton.setOnAction(e -> {
            // Fechar a janela atual
            Stage currentStage = (Stage) voltarButton.getScene().getWindow();
            currentStage.close();
        });

        confirmarButton.setOnAction(e -> {
            System.out.println(java.time.LocalDate.now());

            // Obtém o fornecedor selecionado no ComboBox
            String fornecedor = FornecedorCombobox.getValue();

            // Verifica se um fornecedor foi selecionado
            if (fornecedor == null) {
                System.out.println("Selecione um fornecedor antes de confirmar.");
                return;
            }

            // Consulta para obter o ID do fornecedor pelo nome
            String selectFornecedor = "SELECT id FROM supplier WHERE name = ?";
            int fornecedorId = -1; // Valor inicial inválido para indicar falha na consulta
            dbConnection = new dataBaseConnection();
            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement selectFornecedorStatement = connection.prepareStatement(selectFornecedor)) {
                dbConnection = new dataBaseConnection();
                selectFornecedorStatement.setString(1, fornecedor);
                ResultSet fornecedorResult = selectFornecedorStatement.executeQuery();

                if (fornecedorResult.next()) {
                    fornecedorId = fornecedorResult.getInt("id");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Verifica se o ID do fornecedor foi obtido com sucesso
            if (fornecedorId != -1) {
                // Consulta para obter o próximo ID disponível na tabela de pedidos
                String selectMaxOrderId = "SELECT MAX(idorder) FROM supplierorder";
                int nextOrderId = -1; // Valor inicial inválido para indicar falha na consulta
                dbConnection = new dataBaseConnection();
                try (Connection connection = dbConnection.getConnection();
                     PreparedStatement selectMaxOrderIdStatement = connection.prepareStatement(selectMaxOrderId)) {
                    dbConnection = new dataBaseConnection();
                    ResultSet maxOrderIdResult = selectMaxOrderIdStatement.executeQuery();

                    if (maxOrderIdResult.next()) {
                        nextOrderId = maxOrderIdResult.getInt(1) + 1;
                    } else {
                        nextOrderId = 1;
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Itera sobre a lista de TableOrder
                for (TableOrder order : tableOrders) {
                    // Consulta para obter o ID do produto pelo nome
                    String selectNome = "SELECT id FROM product WHERE name = ?";
                    int productId = -1; // Valor inicial inválido para indicar falha na consulta
                    dbConnection = new dataBaseConnection();
                    try (Connection connection = dbConnection.getConnection();
                         PreparedStatement selectNomeStatement = connection.prepareStatement(selectNome)) {
                        dbConnection = new dataBaseConnection();
                        selectNomeStatement.setString(1, order.getNome());
                        ResultSet nomeResult = selectNomeStatement.executeQuery();

                        if (nomeResult.next()) {
                            productId = nomeResult.getInt("id");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    // Consulta para obter o ID da categoria pelo nome
                    String selectCategoria = "SELECT id FROM productcategory WHERE name = ?";
                    int categoryId = -1; // Valor inicial inválido para indicar falha na consulta
                    dbConnection = new dataBaseConnection();
                    try (Connection connection = dbConnection.getConnection();
                         PreparedStatement selectCategoriaStatement = connection.prepareStatement(selectCategoria)) {
                        dbConnection = new dataBaseConnection();
                        selectCategoriaStatement.setString(1, order.getCategory());
                        ResultSet categoriaResult = selectCategoriaStatement.executeQuery();

                        if (categoriaResult.next()) {
                            categoryId = categoriaResult.getInt("id");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    dbConnection = new dataBaseConnection();
                    // Insere os dados no banco de dados se as consultas foram bem-sucedidas
                    if (productId != -1 && categoryId != -1) {
                        try (Connection connection = dbConnection.getConnection()) {
                            if (!connection.isClosed()) {
                                String insertQuery = "INSERT INTO supplierorderitem (idproduct, amount, idorder, idsupplier) VALUES (?, ?, ?, ?)";
                                dbConnection = new dataBaseConnection();
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                    insertStatement.setInt(1, productId);
                                    insertStatement.setInt(2, order.getAmount());
                                    insertStatement.setInt(3, nextOrderId);
                                    insertStatement.setInt(4, fornecedorId);

                                    insertStatement.executeUpdate();
                                }

                                System.out.println("Dados inseridos no banco de dados com sucesso!");
                            } else {
                                System.out.println("A conexão com o banco de dados está fechada.");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("Falha ao obter o ID do fornecedor selecionado.");
            }
        });


        try (Connection connection = dbConnection.getConnection()) {
            String fornecedorQuery = "SELECT name FROM supplier";
            try (PreparedStatement fornecedorStatement = connection.prepareStatement(fornecedorQuery);
                 ResultSet fornecedorResultSet = fornecedorStatement.executeQuery()) {
                List<String> nomesDosFornecedores = new ArrayList<>();
                while (fornecedorResultSet.next()) {
                    String nomeFornecedor = fornecedorResultSet.getString("name");
                    nomesDosFornecedores.add(nomeFornecedor);
                }
                FornecedorCombobox.getItems().addAll(nomesDosFornecedores);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();

            adicionarOrder controller = loader.getController();
            controller.setParentController(this);

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
