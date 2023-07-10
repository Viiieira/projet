module com.example.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.persistence;
    requires lombok;
    requires java.desktop;


    opens com.example.projet to javafx.fxml;
    exports com.example.projet;
    exports entities;
    opens entities to javafx.fxml;
}