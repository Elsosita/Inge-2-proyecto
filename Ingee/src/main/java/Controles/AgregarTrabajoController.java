package Controles;

import Clases.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AgregarTrabajoController {

    @FXML private TextField txtPatente;
    @FXML private TextField txtModelo;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCliente;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;
    @FXML private ComboBox<String> cbEstadoPago;
    @FXML private ComboBox<String> cbEstadoFacturacion;
    @FXML private ListView<Cliente> listaSugerencias;
    @FXML private ListView<Vehiculo> listaPatentes;

    private ClienteManager clienteManager;
    private Cliente clienteSeleccionado;

    private VehiculoDao vehiculoDao;
    private Vehiculo vehiculoSeleccionado;


    private final TrabajoDao trabajoDao = new TrabajoDao();


    public void initialize() {
        try {
            Connection conexion = ConexionBD.getConnection();
            clienteManager = new ClienteManager(conexion);
            vehiculoDao = new VehiculoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 🔹 Autocompletado de clientes
        txtCliente.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) {
                listaSugerencias.getItems().clear();
                return;
            }
            try {
                List<Cliente> sugerencias = clienteManager.buscarClientes(newVal);
                listaSugerencias.getItems().setAll(sugerencias);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        listaSugerencias.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
            }
        });

        listaSugerencias.setOnMouseClicked(event -> {
            clienteSeleccionado = listaSugerencias.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                txtCliente.setText(clienteSeleccionado.getNombre());
                listaSugerencias.getItems().clear();
            }
        });

        // 🔹 Autocompletado de patentes
        txtPatente.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) {
                listaPatentes.getItems().clear();
                return;
            }
            try {
                List<Vehiculo> sugerencias = vehiculoDao.buscarVehiculosPorPatente(newVal);
                listaPatentes.getItems().setAll(sugerencias);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        listaPatentes.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Vehiculo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.getPatente() + " (" + item.getCliente().getNombre() + ")");
                }
            }
        });

        listaPatentes.setOnMouseClicked(event -> {
            vehiculoSeleccionado = listaPatentes.getSelectionModel().getSelectedItem();
            if (vehiculoSeleccionado != null) {
                txtPatente.setText(vehiculoSeleccionado.getPatente());
                txtModelo.setText(vehiculoSeleccionado.getModelo());
                txtCliente.setText(vehiculoSeleccionado.getCliente().getNombre());
                listaPatentes.getItems().clear();
            }
        });
    }



    public AgregarTrabajoController() throws SQLException {
    }

    @FXML
    private void onGuardar() {
        try {
            // Validar que haya un vehículo seleccionado
            if (vehiculoSeleccionado == null) {
                lblMensaje.setText("⚠️ Seleccione o registre un vehículo existente.");
                return;
            }

            // Crear nuevo trabajo
            Trabajo t = new Trabajo();
            t.setDescripcion(txtDescripcion.getText());
            t.setVehiculo(vehiculoSeleccionado);
            t.setFecha(LocalDate.now());

            // Leer los valores elegidos de los ComboBox
            String pagoSeleccionado = cbEstadoPago.getValue();
            String facturacionSeleccionada = cbEstadoFacturacion.getValue();

            // Validar que no estén vacíos
            if (pagoSeleccionado == null || facturacionSeleccionada == null) {
                lblMensaje.setText("⚠️ Seleccione el estado de pago y facturación.");
                return;
            }

            // Convertir texto a enum
            t.setEstadopago(Trabajo.EstadoPago.valueOf(pagoSeleccionado));
            t.setEstadotrabajo(Trabajo.EstadoTrabajo.PENDIENTE);
            t.setEstadodefacturacion(Trabajo.Estadodefacturacion.valueOf(facturacionSeleccionada));

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
    @FXML
    private void onAgregarNuevoCliente() {
        try {
            // Cargar el FXML del nuevo cliente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/NuevoCliente.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Registrar nuevo cliente");
            stage.setScene(new Scene(root));

            // Que sea modal (bloquea la ventana anterior hasta cerrar esta)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Mostrar la ventana
            stage.showAndWait();

            // Luego podrías recuperar el cliente agregado (si devuelves datos desde el controller)
            // NuevoClienteController controller = loader.getController();
            // Cliente nuevoCliente = controller.getClienteCreado();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al abrir la ventana de nuevo cliente: " + e.getMessage());
        }
    }
}