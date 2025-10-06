package Clases;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RetiroDao {
    private Connection conexion;

    public RetiroDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // CREATE
    public void agregarRetiro(Retiro retiro) throws SQLException {
        String sql = "INSERT INTO Retiro (monto, descripcion, codigoempleado_retiro, codigocaja_retiro, fecha, hora) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setFloat(1, retiro.getMonto());
            stmt.setString(2, retiro.getDescripcion());
            stmt.setInt(3, retiro.getCodigoempleado_retiro());
            stmt.setInt(4, retiro.getCodigocaja_retiro());
            stmt.setDate(5, Date.valueOf(retiro.getFecha()));
            stmt.setTime(6, Time.valueOf(retiro.getHora()));
            stmt.executeUpdate();
        }
    }

    // READ por empleado
    public List<Retiro> obtenerRetirosPorEmpleado(int codigoEmpleado) throws SQLException {
        String sql = "SELECT * FROM Retiro WHERE codigoempleado_retiro = ?";
        List<Retiro> retiros = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, codigoEmpleado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Retiro r = new Retiro();
                r.setMonto(rs.getFloat("monto"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setCodigoempleado_retiro(rs.getInt("codigoempleado_retiro"));
                r.setCodigocaja_retiro(rs.getInt("codigocaja_retiro"));
                r.setFecha(rs.getDate("fecha").toLocalDate());
                r.setHora(rs.getTime("hora").toLocalTime());
                retiros.add(r);
            }
        }
        return retiros;
    }

    // READ todos
    public List<Retiro> obtenerTodosLosRetiros() throws SQLException {
        String sql = "SELECT * FROM Retiro";
        List<Retiro> retiros = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Retiro r = new Retiro();
                r.setMonto(rs.getFloat("monto"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setCodigoempleado_retiro(rs.getInt("codigoempleado_retiro"));
                r.setCodigocaja_retiro(rs.getInt("codigocaja_retiro"));
                r.setFecha(rs.getDate("fecha").toLocalDate());
                r.setHora(rs.getTime("hora").toLocalTime());
                retiros.add(r);
            }
        }
        return retiros;
    }

    // UPDATE
    public void actualizarRetiro(Retiro retiro) throws SQLException {
        String sql = "UPDATE Retiro SET monto = ?, descripcion = ?, codigocaja_retiro = ?, fecha = ?, hora = ? WHERE codigoempleado_retiro = ?";
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

    // DELETE
    public void eliminarRetiro(int codigoEmpleado, LocalDate fecha, LocalTime hora) throws SQLException {
        String sql = "DELETE FROM Retiro WHERE codigoempleado_retiro = ? AND fecha = ? AND hora = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, codigoEmpleado);
            stmt.setDate(2, Date.valueOf(fecha));
            stmt.setTime(3, Time.valueOf(hora));
            stmt.executeUpdate();
        }
    }
}

