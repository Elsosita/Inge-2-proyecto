package Controles;

import Clases.Caja;
import Clases.CajaManager;
import ClasesDao.CajaDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CerrarCajaController {

    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtEfectivo;

    @FXML
    private TextField txtDigital;

    private Caja cajaAbierta;
    private CajaDao cajaDao;

    @FXML
    public void initialize() {
        try {
            cajaDao = CajaDao.getInstancia();
            cajaAbierta = CajaManager.getCajaAbierta();

            if (cajaAbierta != null) {
                txtTotal.setText(String.format("%.2f", cajaAbierta.getMontototal()));
                txtEfectivo.setText(String.format("%.2f", cajaAbierta.getMontoefectivo()));
                txtDigital.setText(String.format("%.2f", cajaAbierta.getMontodigital()));
            } else {
                mostrarAlerta("Error", "No hay una caja abierta actualmente.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAceptar() {
        try {
            if (cajaAbierta == null) {
                mostrarAlerta("Error", "No hay una caja abierta.", Alert.AlertType.ERROR);
                return;
            }

            cajaDao.cerrarCaja(cajaAbierta); // este método lo implementamos abajo
            CajaManager cajaManager = CajaManager.getInstancia();
            cajaManager.cerrarCaja();


            mostrarAlerta("Éxito", "La caja se ha cerrado correctamente.", Alert.AlertType.INFORMATION);

            Stage stage = (Stage) txtTotal.getScene().getWindow();
            stage.close();
            javafx.application.Platform.exit();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cerrar la caja.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
