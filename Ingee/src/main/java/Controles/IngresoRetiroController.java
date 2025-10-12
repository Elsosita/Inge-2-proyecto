package Controles;

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

    private final RetiroDao retiroDao = new RetiroDao();

    public IngresoRetiroController() throws SQLException {
    }

    @FXML
    private void onAceptar() {
        try {
            // Validaciones básicas
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

            // 🔹 Datos fijos por ahora (puedes reemplazarlos con el usuario o caja actual)
            int codigoEmpleado = 1;
            int codigoCaja = 1;

            // Crear el objeto Retiro
            Retiro retiro = new Retiro(monto, txtDescripcion.getText(), codigoEmpleado, codigoCaja);

            // Guardar en la BD
            retiroDao.insertar(retiro);

            mostrarAlerta("Éxito", "Retiro registrado correctamente.", Alert.AlertType.INFORMATION);

            // Limpiar campos después de guardar
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
