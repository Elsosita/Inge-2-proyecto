package Controles;

import Clases.Trabajo;
import Clases.TrabajoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.util.List;

public class HelloController {

    @FXML
    private TableView<Trabajo> tableTrabajos;

    @FXML
    private TableColumn<Trabajo, String> colPatente;

    @FXML
    private TableColumn<Trabajo, String> colModelo;

    @FXML
    private TableColumn<Trabajo, String> colDescripcion;

    @FXML
    private TableColumn<Trabajo, String> colClienteNombre;

    @FXML
    private TableColumn<Trabajo, String> colClienteTelefono;

    private TrabajoManager trabajoManager;

    @FXML
    public void initialize() {
        try {
            trabajoManager = new TrabajoManager();

            // Configurar columnas
            colPatente.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getVehiculo().getPatente()));
            colModelo.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getVehiculo().getModelo()));
            colDescripcion.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getDescripcion()));
            colClienteNombre.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getVehiculo().getCliente().getNombre()));
            colClienteTelefono.setCellValueFactory(data ->
                    new SimpleStringProperty(
                            String.valueOf(data.getValue().getVehiculo().getCliente().getNumero())
                    ));

            // Obtener trabajos del día
            List<Trabajo> trabajos = trabajoManager.obtenerTrabajosDelDia();
            ObservableList<Trabajo> trabajosObservable = FXCollections.observableArrayList(trabajos);
            tableTrabajos.setItems(trabajosObservable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
