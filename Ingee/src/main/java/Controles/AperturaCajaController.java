package Controles;

import Clases.Caja;
import Clases.CajaManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;

public class AperturaCajaController {

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblHora;

    @FXML
    private TextField txtMontoInicial;

    @FXML
    private Button btnAceptar;

    private final CajaManager cajaManager = new CajaManager();

    private boolean cajaAbierta = false;

    public boolean isCajaAbierta() {
        return cajaAbierta;
    }

    @FXML
    public void initialize() {
        // Muestra fecha y hora automáticamente
        lblFecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblHora.setText(LocalTime.now().withNano(0).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @FXML
    private void onAceptar() {
        try {
            String texto = txtMontoInicial.getText().trim();
            if (texto.isEmpty()) {
                mostrarAlertaMonto();
                return;
            }

            float montoInicial = Float.parseFloat(texto);
            if (montoInicial < 0) {
                mostrarAlertaMonto();
                return;
            }

            cajaManager.abrirCaja(montoInicial);
            cajaAbierta = true; // ✅ Se marcó como abierta
            ((Stage) btnAceptar.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaMonto();
        }
    }

    // Método auxiliar para mostrar alerta
    private void mostrarAlertaMonto() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Monto inválido");
        alert.setHeaderText(null);
        alert.setContentText("Ingrese un monto válido");
        alert.showAndWait();
    }


}
