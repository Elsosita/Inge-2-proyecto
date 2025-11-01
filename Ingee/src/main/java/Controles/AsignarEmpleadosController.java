package Controles;

import Clases.Empleado;
import Clases.Trabajo;
import ClasesDao.ConexionBD;
import ClasesDao.EmpleadoDao;
import ClasesDao.EmpleadoTrabajoDao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsignarEmpleadosController {

    @FXML private ListView<CheckBox> listaEmpleados;
    private Trabajo trabajoRecienCreado;

    private Connection conexion;
    private EmpleadoDao empleadoDao;
    private EmpleadoTrabajoDao empleadoTrabajoDao;
    private Trabajo trabajoAsociado;

    public void initialize() {
        try {
            conexion = ConexionBD.getInstance().getConnection();
            empleadoDao = new EmpleadoDao(conexion);
            empleadoTrabajoDao = new EmpleadoTrabajoDao(conexion);

            List<Empleado> empleados = empleadoDao.listarTodos();
            List<CheckBox> checkBoxes = new ArrayList<>();

            for (Empleado e : empleados) {
                CheckBox cb = new CheckBox(e.getNombreEmpleado());
                cb.setUserData(e); // guardamos el objeto
                checkBoxes.add(cb);
            }

            listaEmpleados.setItems(FXCollections.observableArrayList(checkBoxes));

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error cargando empleados: " + e.getMessage()).showAndWait();
        }
    }

    public void setTrabajoRecienCreado(Trabajo trabajo) {
        this.trabajoRecienCreado = trabajo;
    }



    public void setTrabajo(Trabajo t) {
        this.trabajoAsociado = t;
    }


    @FXML
    private void onGuardar() {
        if (trabajoAsociado == null) {
            new Alert(Alert.AlertType.WARNING, "No hay trabajo asociado.").showAndWait();
            return;
        }

        try {
            for (CheckBox cb : listaEmpleados.getItems()) {
                if (cb.isSelected()) {
                    Empleado emp = (Empleado) cb.getUserData();
                    empleadoTrabajoDao.asignarEmpleadoATrabajo(emp.getIdEmpleado(), trabajoAsociado.getIdTrabajo());
                }
            }
            new Alert(Alert.AlertType.INFORMATION, "✅ Empleados asignados correctamente.").showAndWait();
            cerrarVentana();

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al asignar empleados: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) listaEmpleados.getScene().getWindow();
        stage.close();
    }
}
