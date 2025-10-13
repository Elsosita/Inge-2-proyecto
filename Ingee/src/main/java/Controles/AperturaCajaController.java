package Controles;

import Clases.Caja;
import Clases.CajaManager;
import ClasesDao.CajaDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;

import static java.util.logging.Level.WARNING;

public class AperturaCajaController {

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblHora;

    @FXML
    private TextField txtMontoInicial;

    @FXML
    private Button btnAceptar;

    private final CajaManager cajaManager = CajaManager.getInstancia();

    private boolean cajaAbierta = false;

    public boolean isCajaAbierta() {
        return cajaAbierta;
    }

    @FXML
    public void initialize() {

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

            CajaManager cajaManager = CajaManager.getInstancia();
            Caja caja = new Caja();
            caja.setMontototal(montoInicial);
            caja.setMontoefectivo(montoInicial);
            caja.setMontodigital(0);
            caja.setFecha(LocalDate.now());
            caja.setHora(LocalTime.now());
            caja.setEstado(Caja.Estado.ABIERTA);

            CajaDao cajaDao = CajaDao.getInstancia();
            cajaDao.abrirCaja(caja);

            // 🔥 Guarda la caja abierta para toda la sesión
            CajaManager.setCajaAbierta(caja);

            ((Stage) btnAceptar.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaMonto();
        }
    }


    private void mostrarAlertaMonto() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Monto inválido");
        alert.setHeaderText(null);
        alert.setContentText("Ingrese un monto válido");
        alert.showAndWait();
    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        switch (tipo) {
            case INFORMATION -> alerta.setGraphic(new Label("✅"));
            case WARNING -> alerta.setGraphic(new Label("⚠️"));
            case ERROR -> alerta.setGraphic(new Label("❌"));
        }

        alerta.showAndWait();
    }


}
