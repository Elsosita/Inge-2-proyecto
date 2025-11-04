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


    @FXML private TextField txtModelo;
    @FXML private TextField txtMarca;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPatente;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtMonto;
    @FXML private Label lblMensaje;
    @FXML private ComboBox<String> cbEstadoPago;
    @FXML private ComboBox<String> cbEstadoFacturacion;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<String> cbTieneAseguradora;
    //@FXML private ComboBox<Aseguradora> cbAseguradora;
    @FXML private Checkbox chkOrdenDigital;
    //@FXML private Button btnSeleccionarArchivo;
    //@FXML private TextField txtRutaArchivo;
    @FXML private ComboBox<String> cbTipoOrden;
    @FXML private TextField txtRutaArchivo;
    @FXML private Button btnSeleccionarArchivo;
    @FXML private ComboBox<Aseguradora> cbAseguradora;
    @FXML private ComboBox<Empleado> cmbEmpleado;
    @FXML private ComboBox<Vehiculo> cmbPatente;


    private final EmpleadoDao empleadoDao = EmpleadoDao.getInstancia();
    private ClienteManager clienteManager;
    private Cliente clienteSeleccionado;

    private VehiculoDao vehiculoDao;
    private Vehiculo vehiculoSeleccionado;
    private VehiculoManager vehiculoManager;
    private AseguradoraManager aseguradoraManager;
    private List<Vehiculo> todosLosVehiculos;



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

            List<Empleado> empleados = empleadoDao.listarTodos();
            cmbEmpleado.getItems().setAll(empleados);
            cmbEmpleado.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Empleado item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombreEmpleado());
                }
            });
            cmbEmpleado.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Empleado item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombreEmpleado());
                }
            });

            List<Vehiculo> vehiculos = vehiculoDao.obtenerTodosVehiculos();
            cmbPatente.getItems().setAll(vehiculos);


            cmbPatente.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Vehiculo item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getPatente() + " (" + item.getCliente().getNombre() + ")");
                }
            });
            cmbPatente.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Vehiculo item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getPatente() + " (" + item.getCliente().getNombre() + ")");
                }
            });

// Escuchar la SELECCIÓN del vehículo para autocompletar otros campos
            cmbPatente.valueProperty().addListener((obs, oldVal, newVal) -> {
                vehiculoSeleccionado = newVal;
                if (newVal != null) {
                    txtMarca.setText(newVal.getMarca());
                    txtModelo.setText(newVal.getModelo());
                    txtPatente.clear();

                    Cliente clienteDePatente = newVal.getCliente();


                    if (clienteDePatente != null) {
                        // Esto actualizará cmbCliente.setValue(Cliente B) y disparará el listener de cmbCliente
                        cmbCliente.setValue(clienteDePatente);
                        lblMensaje.setText("");


                    } else {

                        cmbCliente.getSelectionModel().clearSelection();
                    }


                } else {

                    txtMarca.clear();
                    txtModelo.clear();
                    cmbCliente.getSelectionModel().clearSelection();
                }
            });


            // Cargar TODOS los clientes al iniciar
            List<Cliente> clientes = clienteManager.obtenerTodos(); // Asumo que existe este método en ClienteManager
            cmbCliente.getItems().setAll(clientes);

            // Configurar cómo se muestra el objeto Cliente en el ComboBox
            cmbCliente.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Cliente item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre());
                }
            });
            cmbCliente.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Cliente item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre());
                }
            });

            // Escuchar la SELECCIÓN del cliente
            cmbCliente.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    clienteSeleccionado = newVal;
                    txtTelefono.setText(String.valueOf(newVal.getNumero()));
                } else {
                    clienteSeleccionado = null;
                    txtTelefono.clear();
                }
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }





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
            // 1️⃣ Validar que haya cliente seleccionado
            if (clienteSeleccionado == null) {
                lblMensaje.setText("⚠️ Seleccione un cliente antes de continuar.");
                return;
            }


            Vehiculo vehiculo;

            // Opción A: Se seleccionó un vehículo existente del ComboBox
            if (vehiculoSeleccionado != null) {
                vehiculo = vehiculoSeleccionado;
                System.out.println("Vehículo existente seleccionado: " + vehiculo.getPatente());
            }
            // Opción B: No se seleccionó nada. Se trata como NUEVO vehículo/patente.
            else {
                String patenteIngresada = txtPatente.getText().trim();

                // 2️⃣ Validación de datos mínimos para el nuevo vehículo
                if (patenteIngresada.isEmpty() || txtMarca.getText().isEmpty()) {
                    lblMensaje.setText("⚠️ Ingrese Patente y Marca si está registrando un vehículo nuevo.");
                    return;
                }

                // 3️⃣ Crear, asignar y registrar el vehículo nuevo
                Vehiculo vehiculoExistente = vehiculoManager.buscarPorPatente(patenteIngresada);
                if (vehiculoExistente != null) {
                    lblMensaje.setText("⚠️ Error: La patente ya existe. Seleccione el vehículo del combo.");
                    return;
                }

                vehiculo = new Vehiculo();
                vehiculo.setPatente(patenteIngresada);
                vehiculo.setMarca(txtMarca.getText());
                vehiculo.setModelo(txtModelo.getText());
                vehiculo.setCliente(clienteSeleccionado); // Asignamos el cliente seleccionado

                vehiculoManager.registrarVehiculo(vehiculo);
                System.out.println("Vehículo nuevo agregado: " + vehiculo.getPatente());
            }

            // 3️⃣ Crear el trabajo
            Trabajo t = new Trabajo();
            t.setDescripcion(txtDescripcion.getText());
            t.setVehiculo(vehiculo);
            t.setFecha(LocalDate.now());

            // 4️⃣ Leer los valores elegidos de los ComboBox
            String pagoSeleccionado = cbEstadoPago.getValue();
            String facturacionSeleccionada = cbEstadoFacturacion.getValue();

            if (pagoSeleccionado == null || facturacionSeleccionada == null) {
                lblMensaje.setText("⚠️ Seleccione el estado de pago y facturación.");
                return;
            }
            // Leer y validar monto
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
            // 5️⃣ Asignar los estados
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
                    System.out.println("📞 Teléfono actualizado en la BD para " + clienteSeleccionado.getNombre());
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
            Empleado empleadoSeleccionado = cmbEmpleado.getValue();

            if (empleadoSeleccionado == null) {
                lblMensaje.setText("⚠️ Debe seleccionar un empleado para asignar el trabajo.");
                return;
            }

            // 6️⃣ Guardar en BD
            trabajoDao.agregarTrabajo(t);
            empleadoDao.agregarTrabajoAEmpleado(
                    empleadoSeleccionado.getIdEmpleado(),
                    t.getIdTrabajo()
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/ConfirmacionTrabajo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Confirmación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            // 🚫 Deshabilita el botón “X”
            stage.setOnCloseRequest(event -> event.consume());

            stage.showAndWait();



            //lblMensaje.setText("✅ Trabajo agregado correctamente.");
            limpiarCampos();
            txtPatente.getScene().getWindow().hide();

        } catch (Exception e) {
            lblMensaje.setText("❌ Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }





    private void limpiarCampos() {
        txtPatente.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtDescripcion.clear();
        txtTelefono.clear();
        txtMonto.clear();
        cmbPatente.getSelectionModel().clearSelection();
        cmbCliente.getSelectionModel().clearSelection();
        cmbEmpleado.getSelectionModel().clearSelection();
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

            // 🔥 OBTENER EL CONTROLADOR
            NuevoClienteController controller = loader.getController();

            // Crear y configurar la ventana modal
            Stage stage = new Stage();
            stage.setTitle("Registrar nuevo cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Mostrar la ventana y esperar a que se cierre
            stage.showAndWait();

            // 🔥 1. CAPTURAR EL OBJETO DEVUELTO POR LA VENTANA SECUNDARIA
            Cliente nuevoCliente = controller.getClienteCreado();

            // 🔥 2. VERIFICAR SI SE CREÓ UN CLIENTE Y ACTUALIZAR EL COMBO
            if (nuevoCliente != null) {

                // ✅ AÑADIR el nuevo cliente al ObservableList del ComboBox
                cmbCliente.getItems().add(nuevoCliente);

                // ✅ SELECCIONAR el nuevo cliente en el ComboBox
                cmbCliente.setValue(nuevoCliente);

                // ✅ Mostrar un mensaje de éxito
                lblMensaje.setText("✅ Cliente " + nuevoCliente.getNombre() + " agregado y seleccionado.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al abrir la ventana de nuevo cliente: " + e.getMessage());
        }
        // Añadir catch para otras excepciones si es necesario
        catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("❌ Error inesperado al procesar el cliente.");
        }
    }

    private void filtrarPatentesPorCliente(Cliente cliente) {
        cmbPatente.getItems().clear();
        cmbPatente.getSelectionModel().clearSelection();

        if (cliente == null) {
            // Cargar todos los vehículos de la lista maestra
            cmbPatente.getItems().setAll(this.todosLosVehiculos);
            cmbPatente.setPromptText("Seleccione la patente...");
        } else {
            // Filtrar la lista maestra por el cliente seleccionado
            List<Vehiculo> patentesFiltradas = this.todosLosVehiculos.stream()
                    .filter(v -> v.getCliente() != null && v.getCliente().getIdCliente() == cliente.getIdCliente())
                    .toList();

            cmbPatente.getItems().setAll(patentesFiltradas);

            if (patentesFiltradas.isEmpty()) {
                cmbPatente.setPromptText("No hay patentes registradas para este cliente.");
            } else {
                cmbPatente.getSelectionModel().selectFirst();
            }
        }
    }
}