package Controles;

import Clases.Trabajo;
import ClasesDao.TrabajoDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModificarTrabajoController {

    @FXML private TextField txtId;
    @FXML private TextField txtCliente;
    @FXML private TextField txtVehiculo;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cbEstadoPago;
    @FXML private ComboBox<String> cbEstadoFacturacion;

    private Trabajo trabajo;
    private TrabajoDao trabajoDao;

    public ModificarTrabajoController() {
        try {
            trabajoDao = new TrabajoDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
        cargarDatos();
    }

    private void cargarDatos() {
        txtId.setText(String.valueOf(trabajo.getIdTrabajo()));
        txtCliente.setText(trabajo.getVehiculo().getCliente().getNombre());
        txtVehiculo.setText(trabajo.getVehiculo().getPatente());
        txtDescripcion.setText(trabajo.getDescripcion());
        txtMonto.setText(String.valueOf(trabajo.getMonto()));

        cbEstadoPago.getItems().addAll("PENDIENTE", "EN_PROCESO", "TERMINADO");
        cbEstadoFacturacion.getItems().addAll("NOFACTURADO", "FACTURADO");

        if (trabajo.getEstadotrabajo() != null)
            cbEstadoPago.setValue(trabajo.getEstadotrabajo().name());
        System.out.print(trabajo.getEstadodefacturacion());
        if (trabajo.getEstadodefacturacion() != null)
            cbEstadoFacturacion.setValue(trabajo.getEstadodefacturacion().toString());
    }

    @FXML
    private void onGuardar() {
        try {
            trabajo.setDescripcion(txtDescripcion.getText());
            trabajo.setMonto(Float.parseFloat(txtMonto.getText()));

            trabajo.setEstadotrabajo(Trabajo.EstadoTrabajo.valueOf(cbEstadoPago.getValue()));
            trabajo.setEstadodefacturacion(Trabajo.Estadodefacturacion.valueOf(cbEstadoFacturacion.getValue()));

            trabajoDao.actualizarTrabajo(trabajo);

            mostrarAlerta("✅ Cambios guardados correctamente.");
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta("⚠️ Error al guardar: " + e.getMessage());
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
