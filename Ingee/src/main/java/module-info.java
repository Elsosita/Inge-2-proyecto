module com.example.ingee {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires java.desktop;
    //requires com.example.ingee;

    // Abrir y exportar el paquete donde están tus clases y controladores
    opens Controles to javafx.fxml, javafx.graphics;
    opens Clases to javafx.base;
    exports Controles;
    opens ClasesDao to javafx.base;
    opens Managers to javafx.base;
}
