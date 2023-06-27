package com.example.projet;

import entities.ProductEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class detalhesProduto {
    @FXML
    private TextField NomeField;

    @FXML
    private TextField CategoriaField;

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

    public void setProductDetails(ProductEntity product) {
        this.product = product;

        // Atualize os campos de texto com os dados do produto
        NomeField.setText(product.getName());
        CategoriaField.setText(String.valueOf(product.getCategory()));
        PrecoField.setText(String.valueOf(product.getPrice()));
        StockField.setText(String.valueOf(product.getStock()));
    }

    // Restante do código da classe detalhesProduto...
    // Implemente os métodos para manipular os eventos dos botões e outras funcionalidades

    @FXML
    private void handleDeleteButton() {
        // Lógica para lidar com o evento do botão "Apagar"
    }

    @FXML
    private void handleEditButton() {
        // Lógica para lidar com o evento do botão "Editar"
    }

    @FXML
    private void handleSairButton() {
        // Lógica para lidar com o evento do botão "Sair"
    }

    // Outros métodos conforme necessário para manipular a lógica relacionada aos produtos

}
