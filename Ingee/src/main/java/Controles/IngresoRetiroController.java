package Controles;

import Clases.Caja;
import Clases.CajaManager;
import ClasesDao.CajaDao;
import Clases.Retiro;
import ClasesDao.RetiroDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class IngresoRetiroController {

    @FXML
    private TextField txtMonto;

    @FXML
    private TextArea txtDescripcion;

    private CajaDao cajaDao;
    private RetiroDao retiroDao;

    public IngresoRetiroController() {
        try {
            retiroDao = RetiroDao.getInstancia();
            cajaDao = CajaDao.getInstancia();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onAceptar() {
        try {
            // 🔹 Validaciones básicas
            if (txtMonto.getText().isEmpty() || txtDescripcion.getText().isEmpty()) {
                mostrarAlerta("Error", "Debe completar todos los campos.", Alert.AlertType.ERROR);
                return;
            }

            float monto;
            try {
                monto = Float.parseFloat(txtMonto.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El monto debe ser un número válido.", Alert.AlertType.ERROR);
                return;
            }

            if (monto <= 0) {
                mostrarAlerta("Error", "El monto debe ser mayor a 0.", Alert.AlertType.ERROR);
                return;
            }

            // 🔹 Obtener la caja abierta actual
            Caja cajaAbierta = CajaManager.getCajaAbierta();
            if (cajaAbierta == null) {
                mostrarAlerta("Error", "No hay una caja abierta actualmente.", Alert.AlertType.ERROR);
                return;
            }

            // 🔹 Verificar que haya suficiente efectivo
            if (monto > cajaAbierta.getMontoefectivo()) {
                mostrarAlerta("Error", "El monto a retirar supera el efectivo disponible ("
                        + cajaAbierta.getMontoefectivo() + ").", Alert.AlertType.ERROR);
                return;
            }

            // 🔹 Crear el retiro
            Retiro retiro = new Retiro(
                    monto,
                    txtDescripcion.getText(),
                    1, // o el ID real del empleado logueado
                    cajaAbierta.getIdCaja()
            );

            // 🔹 Guardar en BD
            retiroDao.insertar(retiro);

            // 🔹 Actualizar montos en la caja
            cajaAbierta.setMontoefectivo(cajaAbierta.getMontoefectivo() - monto);
            cajaAbierta.setMontototal(cajaAbierta.getMontototal() - monto);

            cajaDao.actualizarMontos(cajaAbierta);

            mostrarAlerta("Éxito", "Retiro registrado y caja actualizada correctamente.", Alert.AlertType.INFORMATION);

            // 🔹 Limpiar campos
            txtMonto.clear();
            txtDescripcion.clear();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error al registrar el retiro.", Alert.AlertType.ERROR);
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
