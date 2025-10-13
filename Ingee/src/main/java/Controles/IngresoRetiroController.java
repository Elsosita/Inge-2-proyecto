package Controles;

import Clases.Caja;
import Clases.CajaManager;
import Clases.Empleado;
import Clases.Retiro;
import ClasesDao.CajaDao;
import ClasesDao.EmpleadoDao;
import ClasesDao.RetiroDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class IngresoRetiroController implements Initializable {

    @FXML
    private TextField txtMonto;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private ComboBox<Empleado> comboEmpleado;

    private CajaDao cajaDao;
    private RetiroDao retiroDao;
    private EmpleadoDao empleadoDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Inicializar DAOs
            cajaDao = CajaDao.getInstancia();
            retiroDao = RetiroDao.getInstancia();
            empleadoDao = EmpleadoDao.getInstancia();

            // Cargar empleados en el ComboBox
            List<Empleado> empleados = empleadoDao.listarTodos();
            comboEmpleado.getItems().setAll(empleados);

            // Mostrar nombre del empleado en el combo
            comboEmpleado.setConverter(new StringConverter<>() {
                @Override
                public String toString(Empleado empleado) {
                    return empleado != null ? empleado.getNombreEmpleado() : "";
                }

                @Override
                public Empleado fromString(String s) {
                    return null;
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los empleados.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onAceptar() {
        try {
            // Validaciones
            if (txtMonto.getText().isEmpty() || txtDescripcion.getText().isEmpty() || comboEmpleado.getValue() == null) {
                mostrarAlerta("Error", "Debe completar todos los campos y seleccionar un empleado.", Alert.AlertType.ERROR);
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

            Caja cajaAbierta = CajaManager.getCajaAbierta();
            if (cajaAbierta == null) {
                mostrarAlerta("Error", "No hay caja abierta actualmente.", Alert.AlertType.ERROR);
                return;
            }

            if (monto > cajaAbierta.getMontoefectivo()) {
                mostrarAlerta("Error", "No hay suficiente efectivo para realizar el retiro.", Alert.AlertType.ERROR);
                return;
            }

            Empleado empleadoSeleccionado = comboEmpleado.getValue();
            Retiro retiro = new Retiro(monto, txtDescripcion.getText(), empleadoSeleccionado.getIdEmpleado(), cajaAbierta.getIdCaja());
            retiroDao.insertar(retiro);

            // Actualizar montos en caja
            cajaAbierta.setMontoefectivo(cajaAbierta.getMontoefectivo() - monto);
            cajaAbierta.setMontototal(cajaAbierta.getMontototal() - monto);
            cajaDao.actualizarMontos(cajaAbierta);

            mostrarAlerta("Éxito", "Retiro registrado correctamente.", Alert.AlertType.INFORMATION);
            txtMonto.clear();
            txtDescripcion.clear();
            comboEmpleado.getSelectionModel().clearSelection();

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
