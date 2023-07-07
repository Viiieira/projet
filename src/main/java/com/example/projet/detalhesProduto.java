package com.example.projet;

import entities.ProductEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public class detalhesProduto {
    @FXML
    private TextField NomeField;

    @FXML
    private ComboBox<String> CategoriaComboBox;

    @FXML
    private TextField PrecoField;

    @FXML
    private TextField StockField;

    @FXML
    private Label nomeError;

    @FXML
    private Label CategoriaError;

    @FXML
    private Label precoError;

    @FXML
    private Label StockError;

    @FXML
    private Label EmptyError;

    @FXML
    private Button apagarButton;

    @FXML
    private Button editarButton;

    @FXML
    private Button sairButton;

    private ProductEntity product;
    private dataBaseConnection dbConnection;
    private String initialNome;
    private String initialCategoria;
    private BigInteger initialPreco;
    private String initialStock;

    private boolean isEditMode = false;

    public void setProductDetails(ProductEntity product) {
        dbConnection = new dataBaseConnection();
        this.product = product;

        // Atualize os campos de texto com os dados do produto
        NomeField.setText(product.getName());
        CategoriaComboBox.setValue(categoriaAtual(product.getCategory()));
        PrecoField.setText(product.getPrice().toString());
        StockField.setText(String.valueOf(product.getStock()));

        initialNome = product.getName();
        initialCategoria = String.valueOf(product.getCategory());
        initialPreco = product.getPrice();
        initialStock = String.valueOf(product.getStock());

        setFieldsEditable(false);
    }
    private String categoriaAtual(int id) {
        String SelectNome = "SELECT name from productcategory where id = ?;";
        String nome = null;

        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement namesStatement = connection.prepareStatement(SelectNome);
            namesStatement.setInt(1, id);
            ResultSet resultSet = namesStatement.executeQuery();

            while (resultSet.next()) {
                nome = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nome;
    }

    @FXML
    public void initialize() {
        dbConnection = new dataBaseConnection();
        sairButton.setOnAction(e -> {
            confirmExit();
        });

        apagarButton.setOnAction(e -> {
            dbConnection = new dataBaseConnection();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Excluir Produto");
            alert.setContentText("Tem certeza de que deseja excluir o produto?");

            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(product.getName());
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String deleteProductQuery = "DELETE FROM product WHERE id = ?";

                try (Connection connection = dbConnection.getConnection();
                     PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductQuery)) {
                    // Set the parameter values
                    deleteProductStatement.setInt(1, product.getId());

                    // Execute the delete statements
                    deleteProductStatement.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                Stage currentStage = (Stage) apagarButton.getScene().getWindow();
                currentStage.close();
            }
        });

        editarButton.setOnAction(e -> {
            if (isEditMode) {
                dbConnection = new dataBaseConnection();
                // Salvar alterações
                if (saveChanges()) {
                    setFieldsEditable(false);
                    isEditMode = false;
                    editarButton.setText("Editar");
                }
            } else {
                dbConnection = new dataBaseConnection();
                // Habilitar edição dos campos de texto
                setFieldsEditable(true);
                isEditMode = true;
                editarButton.setText("Salvar");
            }
        });

        // Preencher a ComboBox com as categorias existentes
        fillCategoryComboBox();
    }

    private void setFieldsEditable(boolean editable) {
        NomeField.setEditable(editable);
        CategoriaComboBox.setDisable(editable);
        PrecoField.setEditable(editable);
        StockField.setEditable(editable);

        // Aplicar estilo aos campos de texto
        String opacity = editable ? "1" : "0.5";
        NomeField.setStyle("-fx-opacity: " + opacity + ";");
        CategoriaComboBox.setStyle("-fx-opacity: " + opacity + ";");
        PrecoField.setStyle("-fx-opacity: " + opacity + ";");
        StockField.setStyle("-fx-opacity: " + opacity + ";");
    }

    private void confirmExit() {
        if (isEditMode) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Saída");
            alert.setHeaderText("Deseja salvar as alterações?");
            alert.setContentText("Se você sair agora, as alterações não serão salvas.");

            ButtonType saveButton = new ButtonType("Salvar");
            ButtonType exitButton = new ButtonType("Sair sem salvar");

            alert.getButtonTypes().setAll(saveButton, exitButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == saveButton) {
                if (saveChanges()) {
                    closeApplication();
                }
            } else {
                closeApplication();
            }
        } else {
            closeApplication();
        }
    }

    private void fillCategoryComboBox() {
        String selectCategoriesQuery = "SELECT name FROM ProductCategory";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement selectCategoriesStatement = connection.prepareStatement(selectCategoriesQuery);
             ResultSet resultSet = selectCategoriesStatement.executeQuery()) {

            List<String> categories = new ArrayList<>();
            while (resultSet.next()) {
                String categoryName = resultSet.getString("name");
                categories.add(categoryName);
            }

            CategoriaComboBox.getItems().addAll(categories);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    private boolean saveChanges() {
        // Get the new values from the text fields
        String novoNome = NomeField.getText();
        String novoCategoria = CategoriaComboBox.getValue();
        String novoPrecoText = PrecoField.getText();
        BigInteger novoPreco = new BigInteger(novoPrecoText);
        String novoStock = StockField.getText();

        // Validate the entered data
        if (!AnalisarDados(novoNome, novoCategoria, novoPrecoText, novoStock)) {
            return false;  // Data is not valid, return false
        }

        try {
            Connection connection = dbConnection.getConnection();
            connection.setAutoCommit(false);

            try {
                // Update the product in the database
                String updateProductQuery = "UPDATE product SET name = ?, category = ?, price = ?, stock = ? WHERE id = ?";
                try (PreparedStatement updateProductStatement = connection.prepareStatement(updateProductQuery)) {
                    // Retrieve the category ID
                    int novoId = getCategoriaId(connection, novoCategoria);

                    // Set the parameter values
                    updateProductStatement.setString(1, novoNome);
                    updateProductStatement.setInt(2, novoId);
                    updateProductStatement.setString(3, novoPrecoText);
                    updateProductStatement.setString(4, novoStock);
                    updateProductStatement.setInt(5, product.getId());

                    // Execute the update statement
                    updateProductStatement.executeUpdate();

                    // Commit the transaction
                    connection.commit();

                    // Update the initial values
                    initialNome = novoNome;
                    initialCategoria = novoCategoria;
                    initialPreco = novoPreco;
                    initialStock = novoStock;

                    return true;  // Changes saved successfully
                }
            } finally {
                // Close the connection
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private int getCategoriaId(Connection connection, String categoria) throws SQLException {
        String selectIdQuery = "SELECT id FROM productcategory WHERE name = ?";
        try (PreparedStatement selectIdStatement = connection.prepareStatement(selectIdQuery)) {
            selectIdStatement.setString(1, categoria);
            ResultSet resultSet = selectIdStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new IllegalArgumentException("Invalid category: " + categoria);
            }
        }
    }

    private void closeApplication() {
        Stage currentStage = (Stage) sairButton.getScene().getWindow();
        currentStage.close();
    }

    private boolean AnalisarDados(String name, String category, String price, String stock) {
        dbConnection = new dataBaseConnection();
        boolean isValid = true;

        String novoNome = NomeField.getText();
        String novoCategoria = CategoriaComboBox.getValue();
        String novoPreco = PrecoField.getText();
        String novoStock = StockField.getText();

        // Verificar o que foi alterado
        boolean nomeChanged = !novoNome.equals(initialNome);
        boolean categoriaChanged = !novoCategoria.equals(initialCategoria);
        boolean precoChanged = !novoPreco.equals(initialPreco);
        boolean stockChanged = !novoStock.equals(initialStock);

        // Verificar se todos os campos estão preenchidos
        if (name.isEmpty() || category.isEmpty() || price.isEmpty() || stock.isEmpty()) {
            isValid = false;
            EmptyError.setText("Insira todos os dados");
        } else {
            EmptyError.setText("");
        }

        try (Connection connection = dbConnection.getConnection()) {
            // Verificar a existência da categoria
            String selectCategoryQuery = "SELECT id FROM ProductCategory WHERE name = ?";
            try (PreparedStatement selectCategoryStatement = connection.prepareStatement(selectCategoryQuery)) {
                selectCategoryStatement.setString(1, category);
                ResultSet resultSet = selectCategoryStatement.executeQuery();

                if (!resultSet.next()) {
                    isValid = false;
                    CategoriaError.setText("Categoria inválida");
                } else {
                    CategoriaError.setText("");
                }
            }

            // Verificar o formato e validade do preço
            try {
                new BigInteger(price);
                precoError.setText("");
            } catch (NumberFormatException ex) {
                isValid = false;
                precoError.setText("Preço inválido");
            }

            // Verificar o formato e validade do estoque
            try {
                Integer.parseInt(stock);
                StockError.setText("");
            } catch (NumberFormatException ex) {
                isValid = false;
                StockError.setText("Estoque inválido");
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return isValid;
    }
}
