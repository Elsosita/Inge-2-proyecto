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
            String nombre = txtNombre.getText();
            String tipoDocumento = cbTipoDocumento.getValue();
            String numeroDocumento = txtNumeroDocumento.getText();
            String telefono = txtTelefono.getText();

            // Validaciones básicas
            if (nombre.isBlank() || tipoDocumento == null || numeroDocumento.isBlank() || telefono.isBlank()) {
                mostrarAlerta("Campos incompletos", "Por favor complete todos los campos requeridos.");
                return;
            }

            Cliente nuevo = new Cliente();
            nuevo.setNombre(nombre);
            nuevo.settipodocumento(Cliente.TD.valueOf(tipoDocumento));
            nuevo.setNumerodoc(Integer.parseInt(numeroDocumento));
            nuevo.setNumero(Integer.parseInt(telefono));

            // Llama al manager para registrar el cliente
            clienteManager.registrarCliente(nuevo);
            clienteCreado = nuevo;

            mostrarAlerta("Éxito", "Cliente agregado correctamente.");
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El número de teléfono debe contener solo dígitos.");
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
