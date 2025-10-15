package Controles;

import Clases.*;
import ClasesDao.ConexionBD;
import ClasesDao.*;
import Managers.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AgregarTrabajoController {

    @FXML private TextField txtPatente;
    @FXML private TextField txtModelo;
    @FXML private TextField txtMarca;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCliente;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtMonto;
    @FXML private Label lblMensaje;
    @FXML private ComboBox<String> cbEstadoPago;
    @FXML private ComboBox<String> cbEstadoFacturacion;
    @FXML private ListView<Cliente> listaSugerencias;
    @FXML private ListView<Vehiculo> listaPatentes;
    @FXML private ComboBox<String> cbTieneAseguradora;
    //@FXML private ComboBox<Aseguradora> cbAseguradora;
    @FXML private Checkbox chkOrdenDigital;
    //@FXML private Button btnSeleccionarArchivo;
    //@FXML private TextField txtRutaArchivo;
    @FXML private ComboBox<String> cbTipoOrden;
    @FXML private TextField txtRutaArchivo;
    @FXML private Button btnSeleccionarArchivo;
    @FXML private ComboBox<Aseguradora> cbAseguradora;




    private ClienteManager clienteManager;
    private Cliente clienteSeleccionado;

    private VehiculoDao vehiculoDao;
    private Vehiculo vehiculoSeleccionado;
    private VehiculoManager vehiculoManager;
    private AseguradoraManager aseguradoraManager;



    private final TrabajoDao trabajoDao = new TrabajoDao();

    public AgregarTrabajoController() throws SQLException {
    }


    public void initialize() {
        try {
            Connection conexion = ConexionBD.getInstance().getConnection();
            clienteManager = new ClienteManager(conexion);
            vehiculoDao = new VehiculoDao();
            vehiculoManager = new VehiculoManager(conexion);
            aseguradoraManager = new AseguradoraManager(conexion);
            List<Aseguradora> aseguradoras = aseguradoraManager.obtenerTodas();
            List<Aseguradora> aseguradorasFiltradas = aseguradoras.stream()
                    .filter(a -> a.getIdAseguradora() != 1) // 👈 Ignora la de id 1
                    .toList();
            cbAseguradora.getItems().setAll(aseguradorasFiltradas);


        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Autocompletado de clientes
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
            Cliente seleccionado = listaSugerencias.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                clienteSeleccionado = seleccionado; // guardamos el cliente elegido
                txtCliente.setText(seleccionado.getNombre());

                // ✅ Autocompletar teléfono
                txtTelefono.setText(String.valueOf(seleccionado.getNumero()));

                listaSugerencias.getItems().clear();
            }
        });


        //Autocompletado de patentes
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
                txtMarca.setText(vehiculoSeleccionado.getMarca());
                txtModelo.setText(vehiculoSeleccionado.getModelo());
                txtCliente.setText(vehiculoSeleccionado.getCliente().getNombre());
                listaPatentes.getItems().clear();
            }
        });
        // Mostrar nombre de aseguradora en el ComboBox
        cbAseguradora.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Aseguradora item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreAseguradora());
            }
        });
        cbAseguradora.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Aseguradora item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreAseguradora());
            }
        });
        //Agregue esto ultimo paso 6
        cbAseguradora.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean tieneAseguradora = newVal != null && newVal.getIdAseguradora() != 1; // 1 = "SinAseguradora"
            cbTipoOrden.setDisable(!tieneAseguradora);
            if (!tieneAseguradora) {
                cbTipoOrden.setValue("Física");
                txtRutaArchivo.clear();
                txtRutaArchivo.setDisable(true);
                btnSeleccionarArchivo.setDisable(true);
            }
        });

        // Escuchar cambios en "Tiene aseguradora"
        cbTieneAseguradora.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean tiene = "Sí".equalsIgnoreCase(newVal);
            cbAseguradora.setDisable(!tiene);
        });

        // Cargar opciones del tipo de orden
        cbTipoOrden.getItems().addAll("Física", "Digital");
        cbTipoOrden.setValue("Física"); // valor por defecto

        // Por defecto el campo de ruta y botón de selección están deshabilitados
        txtRutaArchivo.setDisable(true);
        btnSeleccionarArchivo.setDisable(true);

        // Desactivar tipo de orden si no hay aseguradora
        cbTipoOrden.setDisable(true);
    }

    @FXML
    private void onTipoOrdenChanged() {
        String tipoSeleccionado = cbTipoOrden.getValue();

        boolean esDigital = "Digital".equals(tipoSeleccionado);

        txtRutaArchivo.setDisable(!esDigital);
        btnSeleccionarArchivo.setDisable(!esDigital);

        if (!esDigital) {
            txtRutaArchivo.clear(); // limpiar ruta si pasa a física
        }
    }

    @FXML
    private void onSeleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        File archivoSeleccionado = fileChooser.showOpenDialog(null);
        if (archivoSeleccionado != null) {
            txtRutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    private void onGuardar() {
        try {
            //Validar que haya cliente seleccionado
            if (clienteSeleccionado == null) {
                lblMensaje.setText("⚠️ Seleccione o registre un cliente antes de continuar.");
                return;
            }

            //Verificar si el vehículo ya existe en la BD
            String patenteIngresada = txtPatente.getText().trim();
            Vehiculo vehiculo = vehiculoManager.buscarPorPatente(patenteIngresada);

            if (vehiculo == null) {
                vehiculo = new Vehiculo();
                vehiculo.setPatente(patenteIngresada);
                vehiculo.setMarca(txtMarca.getText());
                vehiculo.setModelo(txtModelo.getText());
                vehiculo.setCliente(clienteSeleccionado);

                vehiculoManager.registrarVehiculo(vehiculo);

            }


            if (vehiculo == null) {
                //i no existe, crear uno nuevo y guardarlo
                vehiculo = new Vehiculo();
                vehiculo.setPatente(patenteIngresada);
                vehiculo.setMarca(txtMarca.getText());
                vehiculo.setModelo(txtModelo.getText());
                vehiculo.setMarca(txtMarca.getText());
                vehiculo.setCliente(clienteSeleccionado);

                vehiculoDao.agregarVehiculo(vehiculo);
            }

            //Crear el trabajo
            Trabajo t = new Trabajo();
            t.setDescripcion(txtDescripcion.getText());
            t.setVehiculo(vehiculo);
            t.setFecha(LocalDate.now());

            //Leer los valores elegidos de los ComboBox
            String pagoSeleccionado = cbEstadoPago.getValue();
            String facturacionSeleccionada = cbEstadoFacturacion.getValue();

            if (pagoSeleccionado == null || facturacionSeleccionada == null) {
                lblMensaje.setText("⚠️ Seleccione el estado de pago y facturación.");
                return;
            }
            //Leer y validar monto
            String montoTexto = txtMonto.getText().trim();
            if (montoTexto.isEmpty()) {
                lblMensaje.setText("⚠️ Ingrese el monto del trabajo.");
                return;
            }

            try {
                float monto = Float.parseFloat(montoTexto);
                t.setMonto(monto);
            } catch (NumberFormatException ex) {
                lblMensaje.setText("⚠️ El monto debe ser un número válido.");
                return;
            }
            //Asignar los estados
            t.setEstadopago(Trabajo.EstadoPago.valueOf(pagoSeleccionado));
            t.setEstadotrabajo(Trabajo.EstadoTrabajo.PENDIENTE);
            t.setEstadodefacturacion(Trabajo.Estadodefacturacion.valueOf(facturacionSeleccionada));

            // Aseguradora
            Aseguradora aseguradoraSeleccionada = null;
            if ("Sí".equalsIgnoreCase(cbTieneAseguradora.getValue())) {
                aseguradoraSeleccionada = cbAseguradora.getValue();
                if (aseguradoraSeleccionada == null) {
                    lblMensaje.setText("⚠️ Seleccione una aseguradora.");
                    return;
                }
                t.setAseguradora(aseguradoraSeleccionada);
            } else {
                // Sin aseguradora → usar id 0
                Aseguradora sinAseguradora = new Aseguradora();
                sinAseguradora.setIdAseguradora(1);
                t.setAseguradora(sinAseguradora);
            }

            if (clienteSeleccionado != null) {
                long telefonoIngresado = Long.parseLong(txtTelefono.getText());
                if (clienteSeleccionado.getNumero() != telefonoIngresado) {
                    clienteSeleccionado.setNumero(telefonoIngresado);
                    clienteManager.actualizarTelefono(clienteSeleccionado);
                }
            }

            if (aseguradoraSeleccionada != null && aseguradoraSeleccionada.getIdAseguradora() != 1) {
                t.setAseguradora(aseguradoraSeleccionada);

                String tipoOrden = cbTipoOrden.getValue();
                if ("Digital".equals(tipoOrden)) {
                    t.setOrdenDeProvision(txtRutaArchivo.getText());
                } else {
                    t.setOrdenDeProvision(null); // Física
                }
            } else {
                t.setAseguradora(null); // o asignás la de ID 1 (“Sin aseguradora”)
                t.setOrdenDeProvision(null);
            }

            //Guardar en BD
            trabajoDao.agregarTrabajo(t);
            //asignar empl
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/AsignarEmpleados.fxml"));
            Parent root = loader.load();

            AsignarEmpleadosController controller = loader.getController();
            controller.setTrabajo(t);

            Stage stage = new Stage();
            stage.setTitle("Asignar empleados");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Controles/ConfirmacionTrabajo.fxml"));
            Parent root2 = loader2.load();

            Stage stage2 = new Stage();
            stage2.setTitle("Confirmación");
            stage2.setScene(new Scene(root2));
            stage2.initModality(Modality.APPLICATION_MODAL);
            stage2.setResizable(false);


            stage2.setOnCloseRequest(event -> event.consume());

            stage2.showAndWait();

            limpiarCampos();
            txtPatente.getScene().getWindow().hide();

        } catch (Exception e) {
            lblMensaje.setText("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void onAsignarEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/AsignarEmpleados.fxml"));
            Parent root = loader.load();
            AsignarEmpleadosController ctrl = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Asignar empleados");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la ventana de empleados: " + e.getMessage()).showAndWait();
        }
    }



    private void limpiarCampos() {
        txtPatente.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtDescripcion.clear();
        txtCliente.clear();
        txtTelefono.clear();
        txtMonto.clear();
    }

    @FXML
    private void onCancelar() {
        txtPatente.getScene().getWindow().hide();
    }
    @FXML
    private void onAgregarNuevoCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/NuevoCliente.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registrar nuevo cliente");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al abrir la ventana de nuevo cliente: " + e.getMessage());
        }
    }
}