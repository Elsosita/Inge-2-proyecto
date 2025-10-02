package Controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class    HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void abrirPanelCliente(ActionEvent event) throws IOException {
        // Cargar el FXML de la segunda ventana
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/Segunda.fxml"));
        Scene escenaCliente = new Scene(loader.load());

        // Crear un nuevo Stage (ventana independiente)
        Stage nuevaVentana = new Stage();
        nuevaVentana.setScene(escenaCliente);
        nuevaVentana.setTitle("Panel Cliente");
        nuevaVentana.show();
    }
}
