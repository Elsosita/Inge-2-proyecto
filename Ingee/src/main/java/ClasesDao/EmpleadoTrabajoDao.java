package ClasesDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoTrabajoDao {
    private final Connection conexion;

    public EmpleadoTrabajoDao(Connection conexion) {
        this.conexion = conexion;
    }

    public void asignarEmpleadoATrabajo(int empleado_id, int trabajo_id) throws SQLException {
        String sql = "INSERT INTO Empleado_Trabajo (empleado_id, trabajo_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, empleado_id);
            stmt.setInt(2, trabajo_id);
            stmt.executeUpdate();
        }
    }

    public List<Integer> obtenerEmpleadosPorTrabajo(int idTrabajo) throws SQLException {
        List<Integer> empleados = new ArrayList<>();
        String sql = "SELECT id_empleado FROM Empleado_Trabajo WHERE id_trabajo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                empleados.add(rs.getInt("id_empleado"));
            }
        }
        return empleados;
    }

    public void eliminarRelacionesPorTrabajo(int idTrabajo) throws SQLException {
        String sql = "DELETE FROM Empleado_Trabajo WHERE id_trabajo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajo);
            stmt.executeUpdate();
        }
    }
}
