package ClasesDao;

import ClasesDao.ConexionBD;
import Clases.Retiro;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RetiroDao {
    private final Connection conexion;

    // ✅ Usa la instancia única del Singleton
    private RetiroDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }

    public RetiroDao(Connection conexion) {
        this.conexion = conexion;
    }
    private static RetiroDao instancia;

    public static synchronized RetiroDao getInstancia() throws SQLException {
        if (instancia == null) {
            instancia = new RetiroDao();
        }
        return instancia;
    }



    // ✅ CREATE
    public boolean insertar(Retiro retiro) {
        String sql = "INSERT INTO Retiro (monto, descripcion, codigoempleado_retiro, codigocaja_retiro, fecha, hora) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setFloat(1, retiro.getMonto());
            ps.setString(2, retiro.getDescripcion());
            ps.setInt(3, retiro.getCodigoempleado_retiro());
            ps.setInt(4, retiro.getCodigocaja_retiro());
            ps.setDate(5, Date.valueOf(retiro.getFecha()));
            ps.setTime(6, Time.valueOf(retiro.getHora()));

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar retiro: " + e.getMessage());
            return false;
        }
    }

    // ✅ READ → por empleado
    public List<Retiro> obtenerRetirosPorEmpleado(int codigoEmpleado) throws SQLException {
        String sql = "SELECT * FROM Retiro WHERE codigoempleado_retiro = ?";
        List<Retiro> retiros = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, codigoEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    retiros.add(mapearRetiro(rs));
                }
            }
        }
        return retiros;
    }

    // ✅ READ → todos
    public List<Retiro> obtenerTodosLosRetiros() throws SQLException {
        String sql = "SELECT * FROM Retiro";
        List<Retiro> retiros = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                retiros.add(mapearRetiro(rs));
            }
        }
        return retiros;
    }

    // ✅ UPDATE
    public void actualizarRetiro(Retiro retiro) throws SQLException {
        String sql = """
            UPDATE Retiro 
            SET monto = ?, descripcion = ?, codigocaja_retiro = ?, fecha = ?, hora = ?
            WHERE codigoempleado_retiro = ?
        """;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setFloat(1, retiro.getMonto());
            stmt.setString(2, retiro.getDescripcion());
            stmt.setInt(3, retiro.getCodigocaja_retiro());
            stmt.setDate(4, Date.valueOf(retiro.getFecha()));
            stmt.setTime(5, Time.valueOf(retiro.getHora()));
            stmt.setInt(6, retiro.getCodigoempleado_retiro());
            stmt.executeUpdate();
        }
    }

    // ✅ DELETE
    public void eliminarRetiro(int codigoEmpleado, LocalDate fecha, LocalTime hora) throws SQLException {
        String sql = "DELETE FROM Retiro WHERE codigoempleado_retiro = ? AND fecha = ? AND hora = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, codigoEmpleado);
            stmt.setDate(2, Date.valueOf(fecha));
            stmt.setTime(3, Time.valueOf(hora));
            stmt.executeUpdate();
        }
    }

    // ✅ Método auxiliar para mapear ResultSet → Retiro
    private Retiro mapearRetiro(ResultSet rs) throws SQLException {
        Retiro r = new Retiro();
        r.setMonto(rs.getFloat("monto"));
        r.setDescripcion(rs.getString("descripcion"));
        r.setCodigoempleado_retiro(rs.getInt("codigoempleado_retiro"));
        r.setCodigocaja_retiro(rs.getInt("codigocaja_retiro"));
        r.setFecha(rs.getDate("fecha").toLocalDate());
        r.setHora(rs.getTime("hora").toLocalTime());
        return r;
    }
}
