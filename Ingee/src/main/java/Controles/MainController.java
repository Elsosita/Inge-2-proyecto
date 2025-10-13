package Controles;

import Clases.*;
import ClasesDao.TrabajoDao;
import Managers.TrabajoManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;



import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Label fechatabla;
    @FXML
    private Label txtNombreVentana;

    @FXML
    private TableView<Trabajo> tablaTrabajos;
    @FXML
    private TableColumn<Trabajo, String> colPatente;
    @FXML
    private TableColumn<Trabajo, String> colModelo;
    @FXML
    private TableColumn<Trabajo, String> colDescripcion;
    @FXML
    private TableColumn<Trabajo, String> colCliente;
    @FXML
    private TableColumn<Trabajo, String> colTelefono;


    private final TrabajoDao trabajoDao = new TrabajoDao();

    private TrabajoManager trabajoManager;



    public MainController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
        Timeline reloj = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    LocalDateTime ahora = LocalDateTime.now();
                    boolean mostrarDosPuntos = (ahora.getSecond() % 2 == 0);
                    String separador = mostrarDosPuntos ? ":" : " ";
                    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String horaYMinutos = String.format("%02d%s%02d", ahora.getHour(), separador, ahora.getMinute());
                    fechatabla.setText(formatoFecha.format(ahora) + " " + horaYMinutos);
                })
        );
        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
        configurarColumnas();
        cargarTrabajosDelDia();
        txtNombreVentana.setText("Trabajos del dia");
        try {
            trabajoManager = new TrabajoManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarFechaActual() {
        LocalDate hoy = LocalDate.now(); // Obtiene la fecha de hoy
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Cambia el formato si querés
        fechatabla.setText(hoy.format(formato));
    }

    private void configurarColumnas() {
        // 🔹 Vehículo: patente y modelo
        colPatente.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getVehiculo() != null
                                ? data.getValue().getVehiculo().getPatente()
                                : ""
                )
        );

        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getVehiculo() != null
                                ? data.getValue().getVehiculo().getModelo()
                                : ""
                )
        );

        // 🔹 Trabajo: descripción (ya está directo)
        colDescripcion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion())
        );

        // 🔹 Cliente: nombre y teléfono
        colCliente.setCellValueFactory(data ->
                new SimpleStringProperty(
                        (data.getValue().getVehiculo() != null &&
                                data.getValue().getVehiculo().getCliente() != null)
                                ? data.getValue().getVehiculo().getCliente().getNombre()
                                : ""
                )
        );

        colTelefono.setCellValueFactory(data ->
                new SimpleStringProperty(
                        (data.getValue().getVehiculo() != null &&
                                data.getValue().getVehiculo().getCliente() != null)
                                ? String.valueOf(data.getValue().getVehiculo().getCliente().getNumero())
                                : ""
                )
        );
    }


    private void cargarTrabajosDelDia() throws SQLException {
        ObservableList<Trabajo> lista = FXCollections.observableArrayList(trabajoDao.obtenerTrabajosDelDia());
        tablaTrabajos.setItems(lista);
    }

    @FXML
    void mostrarIngresarTrabajo() {
        abrirPestaña("Ingresar Trabajo", "/com/tuapp/vista/TrabajoView.fxml");
    }

    @FXML
    void mostrarIngresarPago() {
        abrirPestaña("Ingresar Pago", "/com/tuapp/vista/PagoView.fxml");
    }


   /* @FXML
    void mostrarFacturados() {
        abrirPestaña("Trabajos Facturados", "/com/tuapp/vista/FacturadosView.fxml");
    }*/

    private void abrirPestaña(String titulo, String rutaFXML) {
        // Revisar si la pestaña ya existe
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(titulo)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            AnchorPane vista = loader.load();

            Tab nuevaTab = new Tab(titulo, vista);
            nuevaTab.setClosable(true);
            tabPane.getTabs().add(nuevaTab);
            tabPane.getSelectionModel().select(nuevaTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onIngresarTrabajo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/AgregarTrabajo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ingresar nuevo trabajo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal mientras está abierta
            stage.setResizable(false);
            stage.showAndWait();
            initialize();

            // OBTENER EL CONTROLADOR, si querés traer el trabajo creado después
            AgregarTrabajoController controller = loader.getController();
            // Trabajo nuevoTrabajo = controller.getTrabajoCreado();
            // if (nuevoTrabajo != null) { ... actualizar tabla ... }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarFacturados() {
        txtNombreVentana.setText("Trabajos facturados");
        try {
            List<Trabajo> trabajos = trabajoManager.obtenerTrabajosFacturados();
            ObservableList<Trabajo> trabajosObservable = FXCollections.observableArrayList(trabajos);
            tablaTrabajos.setItems(trabajosObservable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onVolver(ActionEvent event) throws SQLException {
        initialize();
    }

    @FXML
    private void mostrarManejoCaja() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/ManejoCajaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Manejo de Caja");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarCaja() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/CerrarCajaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cerrar Caja");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
