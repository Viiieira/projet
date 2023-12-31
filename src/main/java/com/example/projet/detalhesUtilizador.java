package com.example.projet;

import entities.SupplierEntity;
import entities.UsersEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class detalhesUtilizador {
    @FXML
    private Button sairButton;

    @FXML
    private Label nomeLabelError;

    @FXML
    private Label emailLabelError;

    @FXML
    private Label nifLabelError;

    @FXML
    private Label phoneLabelError;

    @FXML
    private PasswordField confirmPassField;

    @FXML
    private Label EmptyLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField NifField;

    @FXML
    private TextField telefoneField;

    private UsersEntity user;
    private boolean isEditMode = false;
    private dataBaseConnection dbConnection;
    private String initialNome;
    private String initialEmail;
    private String initialNif;
    private String initialTelefone;

    public void setUserDetails(UsersEntity user) {
        dbConnection = new dataBaseConnection();
        this.user = user;
        initialNome = user.getName();
        initialEmail = user.getEmail();
        initialNif = user.getNif();
        initialTelefone = user.getPhone();
        nomeField.setText(initialNome);
        emailField.setText(initialEmail);
        NifField.setText(initialNif);
        telefoneField.setText(initialTelefone);

        // Atualizar os valores iniciais sempre que um novo usuário for definido
        initialNome = user.getName();
        initialEmail = user.getEmail();
        initialNif = user.getNif();
        initialTelefone = user.getPhone();

        // Desabilitar edição dos campos de texto
        setFieldsEditable(false);
    }

    @FXML
    public void initialize() {
        sairButton.setOnAction(e -> {
            confirmExit();
        });

        deleteButton.setOnAction(e -> {
            dbConnection = new dataBaseConnection();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Excluir Usuário");
            alert.setContentText("Tem certeza de que deseja excluir o usuário?");

            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(user.getName());
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String deleteUserQuery = "DELETE FROM users WHERE id = ?";
                String deleteAdminQuery = "DELETE FROM admin WHERE id = ?";
                String deleteManagerQuery = "DELETE FROM manager WHERE id = ?";
                String deleteCustomerQuery = "DELETE FROM customer WHERE id = ?";

                try (Connection connection = dbConnection.getConnection();
                     PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserQuery);
                     PreparedStatement deleteAdminStatement = connection.prepareStatement(deleteAdminQuery);
                     PreparedStatement deleteManagerStatement = connection.prepareStatement(deleteManagerQuery);
                     PreparedStatement deleteCustomerStatement = connection.prepareStatement(deleteCustomerQuery)) {
                    // Set the parameter values
                    deleteUserStatement.setInt(1, user.getId());
                    deleteAdminStatement.setInt(1, user.getId());
                    deleteManagerStatement.setInt(1, user.getId());
                    deleteCustomerStatement.setInt(1, user.getId());

                    // Execute the delete statements
                    deleteAdminStatement.executeUpdate();
                    deleteManagerStatement.executeUpdate();
                    deleteCustomerStatement.executeUpdate();
                    deleteUserStatement.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                Stage currentStage = (Stage) deleteButton.getScene().getWindow();
                currentStage.close();
            }
        });

        editButton.setOnAction(e -> {
            if (isEditMode) {
                dbConnection = new dataBaseConnection();
                // Salvar alterações
                if (saveChanges()) {

                    setFieldsEditable(false);
                    isEditMode = false;
                    editButton.setText("Editar");
                }
            } else {
                dbConnection = new dataBaseConnection();
                // Habilitar edição dos campos de texto
                setFieldsEditable(true);
                isEditMode = true;
                editButton.setText("Salvar");
            }
        });
    }

    private void setFieldsEditable(boolean editable) {
        nomeField.setEditable(editable);
        emailField.setEditable(editable);
        NifField.setEditable(editable);
        telefoneField.setEditable(editable);

        // Aplicar estilo aos campos de texto
        String opacity = editable ? "1" : "0.5";
        nomeField.setStyle("-fx-opacity: " + opacity + ";");
        emailField.setStyle("-fx-opacity: " + opacity + ";");
        NifField.setStyle("-fx-opacity: " + opacity + ";");
        telefoneField.setStyle("-fx-opacity: " + opacity + ";");
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

    private boolean saveChanges() {
        dbConnection = new dataBaseConnection();
        // Obter os novos valores dos campos de texto
        String novoNome = nomeField.getText();
        String novoEmail = emailField.getText();
        String novoNif = NifField.getText();
        String novoTelefone = telefoneField.getText();

        // Verificar o que foi alterado
        boolean nomeChanged = !novoNome.equals(initialNome);
        boolean emailChanged = !novoEmail.equals(initialEmail);
        boolean nifChanged = !novoNif.equals(initialNif);
        boolean telefoneChanged = !novoTelefone.equals(initialTelefone);

        // Analisar os dados
        if (AnalisarDados(novoNome, novoEmail, novoTelefone, novoNif)) {
            dbConnection = new dataBaseConnection();
            // Atualizar os valores no objeto usuário
            user.setName(novoNome);
            user.setEmail(novoEmail);
            user.setNif(novoNif);
            user.setPhone(novoTelefone);

            // Atualizar os campos de texto
            initialNome = novoNome;
            initialEmail = novoEmail;
            initialNif = novoNif;
            initialTelefone = novoTelefone;

            nomeField.setText(initialNome);
            emailField.setText(initialEmail);
            NifField.setText(initialNif);
            telefoneField.setText(initialTelefone);

            // Desabilitar edição dos campos de texto
            setFieldsEditable(false);

            // Atualizar os valores na base de dados
            String updateUserQuery = "UPDATE users SET name = ?, email = ?, nif = ?, phone = ? WHERE id = ?";
            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery)) {
                // Set the parameter values
                updateUserStatement.setString(1, novoNome);
                updateUserStatement.setString(2, novoEmail);
                updateUserStatement.setString(3, novoNif);
                updateUserStatement.setString(4, novoTelefone);
                updateUserStatement.setInt(5, user.getId());

                // Executar a atualização
                updateUserStatement.executeUpdate();

                // Exibir informações sobre as alterações
                if (nomeChanged) {
                    System.out.println("Nome alterado: " + initialNome + " -> " + novoNome);
                }
                if (emailChanged) {
                    System.out.println("Email alterado: " + initialEmail + " -> " + novoEmail);
                }
                if (nifChanged) {
                    System.out.println("NIF alterado: " + initialNif + " -> " + novoNif);
                }
                if (telefoneChanged) {
                    System.out.println("Telefone alterado: " + initialTelefone + " -> " + novoTelefone);
                }

                return true;  // Indica que as alterações foram salvas com sucesso
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return false;
    }

    private void closeApplication() {
        Stage currentStage = (Stage) sairButton.getScene().getWindow();
        currentStage.close();
    }

    private boolean AnalisarDados(String name, String email, String phone, String nif){
        dbConnection = new dataBaseConnection();
        boolean isValid = true;

        String novoNome = nomeField.getText();
        String novoEmail = emailField.getText();
        String novoNif = NifField.getText();
        String novoTelefone = telefoneField.getText();

        // Verificar o que foi alterado
        boolean nomeChanged = !novoNome.equals(initialNome);
        boolean emailChanged = !novoEmail.equals(initialEmail);
        boolean nifChanged = !novoNif.equals(initialNif);
        boolean telefoneChanged = !novoTelefone.equals(initialTelefone);

        // Verificar se todos os campos estão preenchidos
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || nif.isEmpty()) {
            isValid = false;
            EmptyLabel.setText("Insira todos os dados");
        }else{
            EmptyLabel.setText("");
        }

        try (Connection connection = dbConnection.getConnection()) {
            if(nomeChanged){
                    // Verificar se o nome já existe
                    String namesQuery = "SELECT name FROM users WHERE name = ?";
                    PreparedStatement namesStatement = connection.prepareStatement(namesQuery);
                    namesStatement.setString(1, name);
                    ResultSet namesResultSet = namesStatement.executeQuery();

                    if (namesResultSet.next()) {
                        isValid = false;
                        nomeLabelError.setText("O nome já existe");
                    } else {
                        nomeLabelError.setText("");
                    }
                }

            if(emailChanged){
                // Verificar se o e-mail já existe
                String emailQuery = "SELECT email FROM users WHERE email = ?";
                PreparedStatement emailStatement = connection.prepareStatement(emailQuery);
                emailStatement.setString(1, email);
                ResultSet emailResultSet = emailStatement.executeQuery();

                if (emailResultSet.next()) {
                    isValid = false;
                    emailLabelError.setText("O mail já existe");
                }else{
                    emailLabelError.setText("");
                }
            }

            if (telefoneChanged) {
                // Verificar se o número de telefone já existe
                String phoneQuery = "SELECT phone FROM users WHERE phone = ?";
                PreparedStatement phoneStatement = connection.prepareStatement(phoneQuery);
                phoneStatement.setString(1, phone);
                ResultSet phoneResultSet = phoneStatement.executeQuery();

                boolean containsNonDigits = false;
                for (char c : phone.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        containsNonDigits = true;
                        break;
                    }
                }
                if (containsNonDigits) {
                    isValid = false;
                    phoneLabelError.setText("Insira apenas numeros");
                } else if (phone.length() != 9) {
                    isValid = false;
                    phoneLabelError.setText("Insira os 9 dígitos");
                } else if (phoneResultSet.next()) {
                    isValid = false;
                    phoneLabelError.setText("Esse número já existe");
                } else {
                    phoneLabelError.setText("");
                }
            }


            if (nifChanged) {
                // Verificar se o NIF já existe
                String nifQuery = "SELECT nif FROM users WHERE nif = ?";
                PreparedStatement nifStatement = connection.prepareStatement(nifQuery);
                nifStatement.setString(1, nif);
                ResultSet nifResultSet = nifStatement.executeQuery();

                boolean containsNonDigits = false;
                for (char c : nif.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        containsNonDigits = true;
                        break;
                    }
                }

                if (containsNonDigits) {
                    isValid = false;
                    nifLabelError.setText("Insira apenas dígitos");
                } else if (nif.length() != 9) {
                    isValid = false;
                    nifLabelError.setText("Insira os 9 dígitos");
                } else if (nifResultSet.next()) {
                    isValid = false;
                    nifLabelError.setText("Esse NIF já existe");
                } else {
                    nifLabelError.setText("");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            isValid = false;
        }
        return isValid;
    }

}
