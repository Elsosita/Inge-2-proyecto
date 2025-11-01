/*package Controles;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Importa tus clases Modelo, Manager y los Enums
import Clases.*;
import ClasesDao.*; // Importante
// ... otras importaciones

public class ModificarTrabajoController {

    // --- Campos FXML ---
    @FXML private TextField txtIdTrabajo;
    @FXML private TextField txtCliente;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cmbEstadoTrabajo;
    @FXML private ComboBox<String> cmbEstadoFacturacion;

    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;

    // --- Variables de lógica ---
    private Trabajo trabajoActual;
    //private TrabajoManager manager; // Usa tu Manager

    @FXML
    private void initialize() {
        //this.manager = TrabajoManager.getInstance();

        // --- ASÍ SE HACE ---
        // 1. Llena el ComboBox de Estados de Trabajo
        // .values() devuelve un array [PENDIENTE, NO_REALIZADO, REALIZADO]
        // El ComboBox llamará a .toString() en cada uno y mostrará el String.
        cmbEstadoTrabajo.setItems(FXCollections.observableArrayList(EstadoTrabajo.values()));

        // 2. Llena el ComboBox de Estados de Facturación
        cmbEstadoFacturacion.setItems(FXCollections.observableArrayList(EstadoFacturacion.values()));
    }
    }

    /**
     * MÉTODO PÚBLICO para recibir el objeto desde el controlador anterior.

    public void initData(Trabajo trabajo) {
        this.trabajoActual = trabajo;

        // 2. Poblar todos los campos con los datos del trabajo
        txtIdTrabajo.setText(String.valueOf(trabajo.getId()));
        txtDescripcion.setText(trabajo.getDescripcion());
        txtMonto.setText(String.valueOf(trabajo.getMonto())); // Asume que getMonto() devuelve double o float

        // Asigno los valores actuales a los ComboBox
        cmbEstadoTrabajo.setValue(trabajo.getEstadotrabajo()); // Asume que getEstadoTrabajo() devuelve el Enum
        cmbEstadoFacturacion.setValue(trabajo.getEstadoFacturacion()); // Asume que getEstadoFacturacion() devuelve el Enum

        // --- Campos no modificables ---
        // Asumo que tu objeto Trabajo tiene objetos Cliente y Vehiculo anidados.
        // Si solo tienes IDs (ej. trabajo.getIdCliente()), necesitarías
        // llamar al ClienteManager/VehiculoManager para obtener los nombres.
        // Pero es mejor si la consulta original de 'ConsultarTrabajo' ya trajo esta info.

        // Asumiendo que trabajo.getCliente() devuelve un objeto Cliente
        if (trabajo.getCliente() != null) {
            txtCliente.setText(trabajo.getCliente().getNombreCompleto()); // O getNombre()
        }

        // Asumiendo que trabajo.getVehiculo() devuelve un objeto Vehiculo
        if (trabajo.getVehiculo() != null) {
            txtMarca.setText(trabajo.getVehiculo().getMarca());
            txtModelo.setText(trabajo.getVehiculo().getModelo());
        }
    }

    @FXML
    private void onGuardarClick(ActionEvent event) {
        // 3. Validar y Guardar
        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El monto debe ser un número válido.");
            return;
        }

        // 4. Actualizar el objeto 'trabajoActual'
        trabajoActual.setDescripcion(txtDescripcion.getText());
        trabajoActual.setMonto(monto);
        trabajoActual.setEstadoTrabajo(cmbEstadoTrabajo.getValue());
        trabajoActual.setEstadoFacturacion(cmbEstadoFacturacion.getValue());

        // 5. Llamar al Manager (o DAO) para actualizar la BD
        boolean exito = manager.modificarTrabajo(trabajoActual); // O el nombre de tu método de update

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El trabajo se modificó correctamente.");
            cerrarVentana();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el trabajo en la base de datos.");
        }
    }

    @FXML
    private void onCancelarClick(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        // Obtiene el Stage (ventana) actual desde uno de los botones y la cierra
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // Método de utilidad para alertas
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}*/