package ClasesDao;


import Clases.Caja;
import Clases.ConexionBD;

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
    public void abrirCaja(Caja caja) throws SQLException {
        String sql = "INSERT INTO Caja (montototal, fecha, hora, estado) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setFloat(1, caja.getMontototal());
            pst.setDate(2, Date.valueOf(caja.getFecha()));
            pst.setTime(3, Time.valueOf(caja.getHora()));
            pst.setString(4, caja.getEstado().name());

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                caja.setId(rs.getInt(1));
            }
        }
    }

    public Caja obtenerCajaAbierta() throws SQLException {
        String sql = "SELECT * FROM Caja WHERE estado = 'ABIERTA' ORDER BY id DESC LIMIT 1";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                Caja caja = new Caja();
                caja.setMontototal(rs.getFloat("montototal"));
                caja.setFecha(rs.getDate("fecha").toLocalDate());
                caja.setHora(rs.getTime("hora").toLocalTime());
                caja.setEstado(Caja.Estado.valueOf(rs.getString("estado")));
                return caja;
            }
            return null;
        }
    }

}
