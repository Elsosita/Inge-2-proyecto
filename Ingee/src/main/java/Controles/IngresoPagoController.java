package Controles;

import Clases.*;
import Clases.CajaManager;
import ClasesDao.PagoDao;
import ClasesDao.TrabajoDao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class    IngresoPagoController {

    @FXML
    private TextField txtMonto;

    @FXML
    private ComboBox<String> comboTipoPago;

    @FXML
    private ComboBox<Trabajo> comboTrabajo;

    private PagoDao pagoDao;
    private TrabajoDao trabajoDao;

    @FXML
    public void initialize() throws SQLException {
        pagoDao = new PagoDao();
        trabajoDao = new TrabajoDao();

        comboTipoPago.setItems(FXCollections.observableArrayList("EFECTIVO", "DIGITAL"));

        try {
            comboTrabajo.setItems(FXCollections.observableArrayList(trabajoDao.obtenerTrabajosSinPago()));
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los trabajos pendientes.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        txtMonto.setEditable(false);

        comboTrabajo.setOnAction(event -> {
            Trabajo trabajoSeleccionado = comboTrabajo.getValue();
            if (trabajoSeleccionado != null) {
                txtMonto.setText(String.valueOf(trabajoSeleccionado.getMonto()));
            } else {
                txtMonto.clear();
            }
        });
    }

    @FXML
    private void registrarPago() {
        try {
            // Validaciones
            if (comboTipoPago.getValue() == null) {
                mostrarAlerta("Falta tipo de pago", "Seleccione el tipo de pago.", Alert.AlertType.WARNING);
                return;
            }

            if (txtMonto.getText().isEmpty()) {
                mostrarAlerta("Falta monto", "Ingrese el monto del pago.", Alert.AlertType.WARNING);
                return;
            }

            float monto;
            try {
                monto = Float.parseFloat(txtMonto.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Monto inválido", "Ingrese un valor numérico válido.", Alert.AlertType.WARNING);
                return;
            }

            Trabajo trabajoSeleccionado = comboTrabajo.getValue();
            if (trabajoSeleccionado == null) {
                mostrarAlerta("Falta trabajo", "Seleccione un trabajo para asociar el pago.", Alert.AlertType.WARNING);
                return;
            }

            Caja cajaAbierta = CajaManager.getCajaAbierta();
            if (cajaAbierta == null) {
                mostrarAlerta("Error", "No hay una caja abierta actualmente.", Alert.AlertType.ERROR);
                return;
            }

            Pago.Tipo tipo = Pago.Tipo.valueOf(comboTipoPago.getValue());
            Pago nuevoPago = new Pago(tipo, monto, trabajoSeleccionado, cajaAbierta);

            CajaManager.getInstancia().registrarPago(nuevoPago);


            mostrarAlerta("Éxito", "Pago registrado correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error al registrar el pago.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtMonto.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
