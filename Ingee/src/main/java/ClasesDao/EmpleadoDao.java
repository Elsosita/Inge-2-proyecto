package ClasesDao;

import ClasesDao.ConexionBD;
import Clases.Empleado;
import Clases.Retiro;
import Clases.Trabajo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDao {
    private static EmpleadoDao instancia;
    private final Connection conexion;

    // 🔹 Constructor privado (solo accesible dentro de la clase)
    private EmpleadoDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }

    // 🔹 Método estático para obtener la única instancia
    public static EmpleadoDao getInstancia() throws SQLException {
        if (instancia == null) {
            instancia = new EmpleadoDao();
        }
        return instancia;
    }
    public EmpleadoDao(Connection conexion) {
        this.conexion = conexion;
    }

    // ✅ CREATE
    public void agregarEmpleado(Empleado e) throws SQLException {
        String sql = "INSERT INTO Empleado (nombre, telefono, sueldo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, e.getNombreEmpleado());
            stmt.setLong(2, e.getTelefonoEmpleado()); // 👈 mejor long por seguridad
            stmt.setFloat(3, e.getSueldoEmpleado());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setIdEmpleado(rs.getInt(1));
                }
            }
        }
    }

    // ✅ READ por ID
    public Empleado obtenerEmpleadoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Empleado WHERE id = ?";
        Empleado e = null;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    e = new Empleado();
                    e.setIdEmpleado(rs.getInt("id"));
                    e.setNombreEmpleado(rs.getString("nombre"));
                    e.setTelefonoEmpleado(rs.getLong("telefono"));
                    e.setSueldoEmpleado(rs.getFloat("sueldo"));

                    // Relaciones
                    e.setTrabajosEmpleado(obtenerTrabajosDeEmpleado(id));
                    e.setRetirosEmpleado(obtenerRetirosDeEmpleado(id));
                }
            }
        }

        return e;
    }

    // ✅ READ → listar todos
    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Empleado";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empleado e = new Empleado();
                e.setIdEmpleado(rs.getInt("id"));
                e.setNombreEmpleado(rs.getString("nombre"));
                e.setTelefonoEmpleado(rs.getLong("telefono"));
                e.setSueldoEmpleado(rs.getFloat("sueldo"));
                lista.add(e);
            }
        }

        return lista;
    }

    // ✅ UPDATE
    public void actualizarEmpleado(Empleado e) throws SQLException {
        String sql = "UPDATE Empleado SET nombre = ?, telefono = ?, sueldo = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, e.getNombreEmpleado());
            stmt.setLong(2, e.getTelefonoEmpleado());
            stmt.setFloat(3, e.getSueldoEmpleado());
            stmt.setInt(4, e.getIdEmpleado());
            stmt.executeUpdate();
        }
    }

    // ✅ DELETE
    public void eliminarEmpleado(int id) throws SQLException {
        String sql = "DELETE FROM Empleado WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ✅ RELACIONES

    // 🔹 Obtener trabajos asignados al empleado
    private List<Trabajo> obtenerTrabajosDeEmpleado(int empleadoId) throws SQLException {
        String sql = """
                SELECT t.* FROM Trabajo t
                JOIN Empleado_Trabajo et ON t.id = et.trabajo_id
                WHERE et.empleado_id = ?
                """;

        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Trabajo t = new Trabajo();
                    t.setIdTrabajo(rs.getInt("id"));
                    t.setDescripcion(rs.getString("descripcion"));
                    t.setMonto(rs.getFloat("monto"));
                    trabajos.add(t);
                }
            }
        }
        return trabajos;
    }

    // 🔹 Obtener retiros asociados
    private List<Retiro> obtenerRetirosDeEmpleado(int empleadoId) throws SQLException {
        String sql = "SELECT * FROM Retiro WHERE codigoempleado_retiro = ?";
        List<Retiro> retiros = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Retiro r = new Retiro();
                    r.setMonto(rs.getFloat("monto"));
                    r.setDescripcion(rs.getString("descripcion"));
                    retiros.add(r);
                }
            }
        }
        return retiros;
    }

    // ✅ Método para vincular un trabajo a un empleado
    public void agregarTrabajoAEmpleado(int empleadoId, int trabajoId) throws SQLException {
        String sql = "INSERT INTO Empleado_Trabajo (empleado_id, trabajo_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            stmt.setInt(2, trabajoId);
            stmt.executeUpdate();
        }
    }
}
