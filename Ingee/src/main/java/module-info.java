module com.example.ingee {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    //requires com.example.ingee;


    opens Controles to javafx.fxml;
    exports Controles;
}