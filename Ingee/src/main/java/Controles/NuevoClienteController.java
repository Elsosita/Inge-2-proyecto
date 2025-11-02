package Controles;

import Clases.*;
import Managers.ClienteManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NuevoClienteController {

    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cbTipoDocumento;
    @FXML private TextField txtNumeroDocumento;
    @FXML private TextField txtTelefono;

    private ClienteManager clienteManager = new ClienteManager();
    private Cliente clienteCreado;

    public NuevoClienteController() throws SQLException {
    }

    @FXML
    private void initialize() {
        cbTipoDocumento.getItems().addAll("DNI", "CUIL", "CUIT");
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        try {
            String nombre = txtNombre.getText().trim(); // Limpiar espacios del nombre
            String tipoDocumento = cbTipoDocumento.getValue();
            String numeroDocumentoTexto = txtNumeroDocumento.getText().trim(); // Limpiar espacios del documento
            String telefonoTexto = txtTelefono.getText().trim(); // 🔥 Limpiar espacios del teléfono

            // Validaciones básicas
            if (nombre.isBlank() || tipoDocumento == null || numeroDocumentoTexto.isBlank() || telefonoTexto.isBlank()) {
                mostrarAlerta("Campos incompletos", "Por favor complete todos los campos requeridos.");
                return;
            }

            // Conversión a números antes de crear el objeto.
            // Usamos Integer.parseInt y Long.parseLong. El try-catch manejará los fallos.
            int numeroDoc = Integer.parseInt(numeroDocumentoTexto);

            // 🔥 CORRECCIÓN CLAVE: Usar Long.parseLong() y eliminar cualquier espacio interno (\s+)
            long telefono = Long.parseLong(telefonoTexto.replaceAll("\\s+", ""));

            Cliente nuevo = new Cliente();
            nuevo.setNombre(nombre);
            nuevo.settipodocumento(Cliente.TD.valueOf(tipoDocumento));
            nuevo.setNumerodoc(numeroDoc);
            nuevo.setNumero(telefono); // setNumero ahora recibe el valor long correcto

            // Llama al manager para registrar el cliente
            clienteManager.registrarCliente(nuevo);
            clienteCreado = nuevo;

            mostrarAlerta("Éxito", "Cliente agregado correctamente.");
            cerrarVentana();

        } catch (NumberFormatException e) {
            // Este catch maneja los errores si númeroDocumento o teléfono no son dígitos válidos.
            mostrarAlerta("Error", "Los campos de documento y teléfono deben contener solo dígitos válidos.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo agregar el cliente. Revise la conexión o los datos ingresados.");
        }
    }

    @FXML
    private void onCancelar(ActionEvent event) {
        cerrarVentana();
    }

    // --- Métodos auxiliares ---
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    public Cliente getClienteCreado() {
        return clienteCreado;
    }
}
