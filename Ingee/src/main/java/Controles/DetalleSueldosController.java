package Controles;

import Clases.SueldoLiquidacion; // ¡Asegúrate de que esta clase esté en el paquete Clases!
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class DetalleSueldosController {

    // Componentes FXML de la tabla
    @FXML
    private TableView<SueldoLiquidacion> tablaDetalleSueldos;
    @FXML
    private TableColumn<SueldoLiquidacion, String> colNombre;
    @FXML
    private TableColumn<SueldoLiquidacion, Float> colFijo;
    @FXML
    private TableColumn<SueldoLiquidacion, Float> colComision;
    @FXML
    private TableColumn<SueldoLiquidacion, Float> colTotal;

    /**
     * Inicializa el controlador, configurando cómo se mapearán las propiedades
     * de SueldoLiquidacion a las columnas de la tabla.
     */
    @FXML
    public void initialize() {
        // Mapeo de columnas a las propiedades de la clase SueldoLiquidacion (usando los getters)
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colFijo.setCellValueFactory(new PropertyValueFactory<>("sueldoFijo"));
        colComision.setCellValueFactory(new PropertyValueFactory<>("comision"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPagar"));
    }

    /**
     * Método público llamado desde CerrarCajaController para inyectar los datos.
     * @param detalles Lista de SueldoLiquidacion calculados por el CajaManager.
     */
    public void setDetalleLiquidacion(List<SueldoLiquidacion> detalles) {
        if (detalles != null) {
            // Carga la lista de objetos SueldoLiquidacion en la tabla
            tablaDetalleSueldos.getItems().setAll(detalles);
        }
    }
}