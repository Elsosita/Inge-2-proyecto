package ClasesDao;

import Clases.Caja;
import ClasesDao.ConexionBD;
import Clases.Pago;
import Clases.Trabajo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDao {
    private final Connection conexion;


    public PagoDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }

    public PagoDao(Connection conexion) {
        this.conexion = conexion;
    }


    public void insertar(Pago pago) throws SQLException {
        String sqlInsert = "INSERT INTO Pago (tipo, monto, trabajo_id, caja_id) VALUES (?, ?, ?, ?)";
        String sqlUpdateTrabajo = "UPDATE Trabajo SET estadoPago = 'PAGADO' WHERE id = ?";


        conexion.setAutoCommit(false);
        try (PreparedStatement stmtPago = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtTrabajo = conexion.prepareStatement(sqlUpdateTrabajo)) {

            stmtPago.setString(1, pago.getTipoPago().name());
            stmtPago.setFloat(2, pago.getMontoPago());
            stmtPago.setInt(3, pago.getTrabajoPago().getIdTrabajo());
            stmtPago.setInt(4, pago.getCajaPago().getIdCaja());
            stmtPago.executeUpdate();

            stmtTrabajo.setInt(1, pago.getTrabajoPago().getIdTrabajo());
            stmtTrabajo.executeUpdate();

            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }


    public Pago obtenerPagoPorId(int id) throws SQLException {
        String sql = """
                SELECT p.*, t.id AS trabajo_id, c.id AS caja_id
                FROM Pago p
                JOIN Trabajo t ON p.trabajo_id = t.id
                JOIN Caja c ON p.caja_id = c.id
                WHERE p.id = ?
                """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pago p = new Pago();
                    p.setTipoPago(Pago.Tipo.valueOf(rs.getString("tipo")));
                    p.setMontoPago(rs.getFloat("monto"));
                    p.setFechaPago(rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null);
                    p.setHoraPago(rs.getTime("hora") != null ? rs.getTime("hora").toLocalTime() : null);

                    Trabajo t = new Trabajo();
                    t.setIdTrabajo(rs.getInt("trabajo_id"));
                    p.setTrabajoPago(t);

                    Caja c = new Caja();
                    c.setId(rs.getInt("caja_id"));
                    p.setCajaPago(c);

                    return p;
                }
            }
        }
        return null;
    }


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


    public void eliminarPago(int id) throws SQLException {
        String sql = "DELETE FROM Pago WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public List<Pago> obtenerTodosLosPagos() throws SQLException {
        String sql = "SELECT * FROM Pago";
        List<Pago> pagos = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pago p = new Pago();
                p.setTipoPago(Pago.Tipo.valueOf(rs.getString("tipo")));
                p.setMontoPago(rs.getFloat("monto"));
                p.setFechaPago(rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null);
                p.setHoraPago(rs.getTime("hora") != null ? rs.getTime("hora").toLocalTime() : null);

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
