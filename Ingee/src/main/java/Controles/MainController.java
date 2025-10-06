package Controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    @FXML
    private TabPane tabPane;

    @FXML
    void mostrarIngresarTrabajo() {
        abrirPestaña("Ingresar Trabajo", "/com/tuapp/vista/TrabajoView.fxml");
    }

    @FXML
    void mostrarIngresarPago() {
        abrirPestaña("Ingresar Pago", "/com/tuapp/vista/PagoView.fxml");
    }

    @FXML
    void mostrarManejoCaja() {
        abrirPestaña("Manejo de Caja", "/com/tuapp/vista/CajaView.fxml");
    }

    @FXML
    void mostrarFacturados() {
        abrirPestaña("Trabajos Facturados", "/com/tuapp/vista/FacturadosView.fxml");
    }

    private void abrirPestaña(String titulo, String rutaFXML) {
        // Revisar si la pestaña ya existe
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(titulo)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            AnchorPane vista = loader.load();

            Tab nuevaTab = new Tab(titulo, vista);
            nuevaTab.setClosable(true);
            tabPane.getTabs().add(nuevaTab);
            tabPane.getSelectionModel().select(nuevaTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
