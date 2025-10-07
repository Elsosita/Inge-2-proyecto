package Controles;

import Clases.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class AgregarTrabajoController {

    @FXML private TextField txtPatente;
    @FXML private TextField txtModelo;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCliente;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;

    private final TrabajoDao trabajoDao = new TrabajoDao();

    public AgregarTrabajoController() throws SQLException {
    }

    @FXML
    private void onGuardar() {
        try {
            // Crear cliente
            Cliente c = new Cliente();
            c.setNombre(txtCliente.getText());
            c.setNumero(Integer.parseInt(txtTelefono.getText()));

            // Crear vehículo
            Vehiculo v = new Vehiculo();
            v.setPatente(txtPatente.getText());
            v.setModelo(txtModelo.getText());
            v.setCliente(c);

            // Crear trabajo
            Trabajo t = new Trabajo();
            t.setDescripcion(txtDescripcion.getText());
            t.setVehiculo(v);

            // Guardar en BD
            trabajoDao.agregarTrabajo(t);

            lblMensaje.setText("✅ Trabajo agregado correctamente.");

            limpiarCampos();

        } catch (Exception e) {
            lblMensaje.setText("❌ Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtPatente.clear();
        txtModelo.clear();
        txtDescripcion.clear();
        txtCliente.clear();
        txtTelefono.clear();
    }

    @FXML
    private void onCancelar() {
        // Cierra la ventana actual
        txtPatente.getScene().getWindow().hide();
    }
}