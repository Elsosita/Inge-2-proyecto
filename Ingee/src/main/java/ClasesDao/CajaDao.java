package ClasesDao;

import Clases.Caja;
import ClasesDao.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CajaDao {

    private final Connection conexion;

    // ✅ Constructor que usa Singleton
    public CajaDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }

    // ✅ Constructor alternativo (por si querés inyectar la conexión manualmente)
    public CajaDao(Connection conexion) {
        this.conexion = conexion;
    }

    // ✅ CREATE
    public void agregarCaja(Caja c) throws SQLException {
        String sql = "INSERT INTO Caja (montototal, fecha, hora, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setFloat(1, c.getMontototal());
            stmt.setDate(2, Date.valueOf(c.getFecha()));
            stmt.setTime(3, Time.valueOf(c.getHora()));
            stmt.setString(4, c.getEstado().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        }
    }

    // ✅ READ por ID
    public Caja obtenerCajaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Caja WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Caja c = new Caja();
                    c.setId(rs.getInt("id"));
                    c.setMontototal(rs.getFloat("montototal"));
                    c.setFecha(rs.getDate("fecha").toLocalDate());
                    if (rs.getTime("hora") != null)
                        c.setHora(rs.getTime("hora").toLocalTime());
                    if (rs.getString("estado") != null)
                        c.setEstado(Caja.Estado.valueOf(rs.getString("estado")));
                    return c;
                }
            }
        }
        return null;
    }

    // ✅ LISTAR todas
    public List<Caja> listarTodas() throws SQLException {
        List<Caja> lista = new ArrayList<>();
        String sql = "SELECT * FROM Caja ORDER BY fecha DESC";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Caja c = new Caja();
                c.setId(rs.getInt("id"));
                c.setMontototal(rs.getFloat("montototal"));
                c.setFecha(rs.getDate("fecha").toLocalDate());
                if (rs.getTime("hora") != null)
                    c.setHora(rs.getTime("hora").toLocalTime());
                if (rs.getString("estado") != null)
                    c.setEstado(Caja.Estado.valueOf(rs.getString("estado")));
                lista.add(c);
            }
        }
        return lista;
    }

    /*// ✅ UPDATE
    public void actualizarCaja(Caja c) throws SQLException {
        String sql = "UPDATE Caja SET montototal = ?, fecha = ?, hora = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setFloat(1, c.getMontototal());
            stmt.setDate(2, Date.valueOf(c.getFecha()));
            stmt.setTime(3, Time.valueOf(c.getHora()));
            stmt.setString(4, c.getEstado().name());
            stmt.setInt(5, c.getId());
            stmt.executeUpdate();
        }
    }*/

    // ✅ DELETE
    public void eliminarCaja(int id) throws SQLException {
        String sql = "DELETE FROM Caja WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ✅ Obtener la última caja abierta
    public Caja obtenerCajaAbierta() throws SQLException {
        String sql = "SELECT * FROM Caja WHERE estado = 'ABIERTA' ORDER BY id DESC LIMIT 1";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Caja caja = new Caja();
                caja.setId(rs.getInt("id"));
                caja.setMontototal(rs.getFloat("montototal"));
                caja.setFecha(rs.getDate("fecha").toLocalDate());
                caja.setHora(rs.getTime("hora").toLocalTime());
                caja.setEstado(Caja.Estado.valueOf(rs.getString("estado")));
                return caja;
            }
        }
        return null;
    }

    // ✅ Cambiar estado a “ABIERTA”
    public void abrirCaja(Caja caja) throws SQLException {
        String sql = "INSERT INTO Caja (montototal, fecha, hora, estado) VALUES (?, ?, ?, 'ABIERTA')";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setFloat(1, caja.getMontototal());
            stmt.setDate(2, Date.valueOf(caja.getFecha()));
            stmt.setTime(3, Time.valueOf(caja.getHora()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    caja.setId(rs.getInt(1));
                }
            }
        }
    }

    // CERRAR CAJA
    public void cerrarCaja(int idCaja) throws SQLException {
        String sql = "UPDATE Caja SET estado = 'CERRADA' WHERE id = ?";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, idCaja);
            pst.executeUpdate();
        }
    }
}
