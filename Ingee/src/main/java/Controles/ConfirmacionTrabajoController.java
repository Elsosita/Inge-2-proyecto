package Controles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ConfirmacionTrabajoController {

    @FXML
    private Button btnAceptar;

    @FXML
    public void initialize() {

    }

    @FXML
    private void onAceptar() {

        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
