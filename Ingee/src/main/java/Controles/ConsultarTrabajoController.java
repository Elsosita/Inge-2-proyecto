package Controles;

import Clases.*;
import ClasesDao.TrabajoDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultarTrabajoController {

    @FXML private TextField txtBuscar;
    @FXML private TableView<Trabajo> tablaTrabajos;
    @FXML private TableColumn<Trabajo, Integer> colId;
    @FXML private TableColumn<Trabajo, String> colDescripcion;
    @FXML private TableColumn<Trabajo, String> colCliente;
    @FXML private TableColumn<Trabajo, String> colPatente;
    @FXML private TableColumn<Trabajo, Float> colMonto;
    @FXML private TableColumn<Trabajo, String> colEstadoPago;
    @FXML private TableColumn<Trabajo, String> colFecha;
    @FXML private TableColumn<Trabajo, String> colEstadoTrabajo;

    private TrabajoDao trabajoDao;
    public static int aux;

    public void initialize() {
        try {
            trabajoDao = new TrabajoDao();
            configurarTabla();
            cargarTrabajos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configurarTabla() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdTrabajo()).asObject());
        colDescripcion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDescripcion()));
        colCliente.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getVehiculo() != null && c.getValue().getVehiculo().getCliente() != null
                        ? c.getValue().getVehiculo().getCliente().getNombre() : ""));
        colPatente.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getVehiculo() != null ? c.getValue().getVehiculo().getPatente() : ""));
        colMonto.setCellValueFactory(c -> new javafx.beans.property.SimpleFloatProperty(c.getValue().getMonto()).asObject());
        colEstadoPago.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstadopagoString()));

        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha().toString()));
        colEstadoTrabajo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstadotrabajo().name()));
    }

    private void cargarTrabajos() throws SQLException {
        tablaTrabajos.setItems(FXCollections.observableArrayList(trabajoDao.obtenerTrabajosConVehiculoYCliente()));

    }

    @FXML
    private void onBuscar() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            try {
                cargarTrabajos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        List<Trabajo> filtrados = tablaTrabajos.getItems().stream()
                .filter(t ->
                        t.getDescripcion().toLowerCase().contains(filtro)
                                || (t.getVehiculo() != null && t.getVehiculo().getPatente().toLowerCase().contains(filtro))
                                || (t.getVehiculo() != null && t.getVehiculo().getCliente() != null
                                && t.getVehiculo().getCliente().getNombre().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        tablaTrabajos.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void onModificar() {
        Trabajo seleccionado = tablaTrabajos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un trabajo para modificar.").showAndWait();
            return;
        }
        aux=seleccionado.getIdTrabajo();

        // Podés abrir aquí tu ventana “AgregarTrabajo.fxml” pero en modo edición
        // Pasándole el trabajo seleccionado al controlador
    }
    public static int getTrabajo(){
        return aux;
    }

    @FXML
    private void onEliminar() {
        Trabajo seleccionado = tablaTrabajos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un trabajo para eliminar.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar el trabajo seleccionado?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmar eliminación");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            try {
                trabajoDao.eliminarTrabajo(seleccionado.getIdTrabajo());
                tablaTrabajos.getItems().remove(seleccionado);
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al eliminar el trabajo.").showAndWait();
            }
        }
    }

    @FXML
    private void onCerrar() {
        ((Stage) txtBuscar.getScene().getWindow()).close();
    }

    @FXML
    private void onCerrarVentana() {
        // 🔹 Cierra la ventana actual
        Stage stage = (Stage) txtBuscar.getScene().getWindow();
        stage.close();
    }
}
