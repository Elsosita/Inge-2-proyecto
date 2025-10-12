package ClasesDao;

import Clases.Caja;
import Clases.ConexionBD;
import Clases.Pago;
import Clases.Trabajo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDao {
    private Connection conexion;

    public PagoDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // CREATE

    public void insertar(Pago pago) throws SQLException {
        String sql = "INSERT INTO Pago (tipo, monto, trabajo_id, caja_id) VALUES (?, ?, ?, ?)";
        String sqlUpdateTrabajo = "UPDATE Trabajo SET estadoPago = 'PAGADO' WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false); // 🔹 Inicia transacción

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateTrabajo)) {

                stmt.setString(1, pago.getTipoPago().name());
                stmt.setFloat(2, pago.getMontoPago());
                stmt.setInt(3, pago.getTrabajoPago().getIdTrabajo());
                stmt.setInt(4, pago.getCajaPago().getIdCaja());
                stmt.executeUpdate();


                stmtUpdate.setInt(1, pago.getTrabajoPago().getIdTrabajo());
                stmtUpdate.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // READ por id
    public Pago obtenerPagoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Pago p JOIN Trabajo t ON p.trabajo_id = t.id JOIN Caja c ON p.caja_id = c.id WHERE p.id = ?";
        Pago p = null;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                p = new Pago();
                p.setTipoPago(Pago.Tipo.valueOf(rs.getString("tipo")));
                p.setMontoPago(rs.getFloat("monto"));
                // Fecha y hora si las guardas en la base
                p.setFechaPago(rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null);
                p.setHoraPago(rs.getTime("hora") != null ? rs.getTime("hora").toLocalTime() : null);

                // Cargar Trabajo y Caja mínimos
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("trabajo_id"));
                p.setTrabajoPago(t);

                Caja c = new Caja();
                c.setId(rs.getInt("caja_id"));
                p.setCajaPago(c);
            }
        }
        return p;
    }

    // UPDATE
    public void actualizarPago(Pago p, int id) throws SQLException {
        String sql = "UPDATE Pago SET tipo = ?, monto = ?, trabajo_id = ?, caja_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, p.getTipoPago().name());
            stmt.setFloat(2, p.getMontoPago());
            stmt.setInt(3, p.getTrabajoPago().getIdTrabajo());
            stmt.setInt(4, p.getCajaPago().getIdCaja());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminarPago(int id) throws SQLException {
        String sql = "DELETE FROM Pago WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Listar todos los pagos
    public List<Pago> obtenerTodosLosPagos() throws SQLException {
        String sql = "SELECT * FROM Pago";
        List<Pago> pagos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pago p = new Pago();
                p.setTipoPago(Pago.Tipo.valueOf(rs.getString("tipo")));
                p.setMontoPago(rs.getFloat("monto"));
                p.setFechaPago(rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null);
                p.setHoraPago(rs.getTime("hora") != null ? rs.getTime("hora").toLocalTime() : null);

                // Solo seteamos ids de relaciones
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("trabajo_id"));
                p.setTrabajoPago(t);

                Caja c = new Caja();
                c.setId(rs.getInt("caja_id"));
                p.setCajaPago(c);

                pagos.add(p);
            }
        }
        return pagos;
    }
}
