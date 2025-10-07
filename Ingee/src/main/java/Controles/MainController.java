package Controles;

import Clases.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;



import java.io.IOException;

public class MainController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Label fechatabla;

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

    public MainController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechatabla.setText(hoy.format(formato));
        configurarColumnas();
        cargarTrabajosDelDia();
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

    @FXML
    void mostrarManejoCaja() {
        abrirPestaña("Manejo de Caja", "/com/tuapp/vista/CajaView.fxml");
    }

    @FXML
    void mostrarFacturados() {
        abrirPestaña("Trabajos Facturados", "/com/tuapp/vista/FacturadosView.fxml");
    }

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
}
