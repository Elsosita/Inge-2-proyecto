package Clases;
//aca

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CajaDao {
    private Connection conexion;

    public CajaDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // CREATE
    public void agregarCaja(Caja c) throws SQLException {
        String sql = "INSERT INTO Caja (montototal, fecha) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setFloat(1, c.getMontototal());
            stmt.setDate(2, Date.valueOf(c.getFecha()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1)); // Asigna el id generado al objeto Caja
            }
        }
    }

    // READ por ID
    public Caja obtenerCajaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Caja WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Caja c = new Caja();
                c.setId(rs.getInt("id"));
                c.setMontototal(rs.getFloat("montototal"));
                c.setFecha(rs.getDate("fecha").toLocalDate());
                c.getRetiros().clear(); // opcional, aseguramos lista vacía
                c.getPagos().clear();   // opcional, aseguramos lista vacía
                return c;
            }
        }
        return null;
    }

    // LISTAR todas
    public List<Caja> listarTodas() throws SQLException {
        List<Caja> lista = new ArrayList<>();
        String sql = "SELECT * FROM Caja";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Caja c = new Caja();
                c.setId(rs.getInt("id"));
                c.setMontototal(rs.getFloat("montototal"));
                c.setFecha(rs.getDate("fecha").toLocalDate());
                c.getRetiros().clear();
                c.getPagos().clear();
                lista.add(c);
            }
        }
        return lista;
    }

    // UPDATE
    public void actualizarCaja(Caja c) throws SQLException {
        String sql = "UPDATE Caja SET montototal=?, fecha=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setFloat(1, c.getMontototal());
            stmt.setDate(2, Date.valueOf(c.getFecha()));
            stmt.setInt(3, c.getIdCaja());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminarCaja(int id) throws SQLException {
        String sql = "DELETE FROM Caja WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
