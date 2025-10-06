package Clases;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrabajoDao {
    private Connection conexion;

    public TrabajoDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // CREATE
    public void agregarTrabajo(Trabajo t) throws SQLException {
        String sql = "INSERT INTO Trabajo (descripcion, fecha, estadopago, estadotrabajo, monto, estadodefacturacion, vehiculo_id, aseguradora_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, t.getDescripcion());
            stmt.setDate(2, Date.valueOf(t.getFecha()));
            stmt.setString(3, t.getEstadopago().name());
            stmt.setString(4, t.getEstadotrabajo().name());
            stmt.setFloat(5, t.getMonto());
            stmt.setString(6, t.getEstadodefacturacion().name());
            stmt.setInt(7, t.getVehiculo() != null ? t.getVehiculo().getIdVehiculo() : 0);
            stmt.setInt(8, t.getAseguradora() != null ? t.getAseguradora().getIdAseguradora() : 0);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                t.setIdTrabajo(rs.getInt(1));
            }
        }
    }

    // READ - por ID
    public Trabajo obtenerTrabajoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Trabajo WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("id"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFecha(rs.getDate("fecha").toLocalDate());
                t.setEstadopago(Trabajo.EstadoPago.valueOf(rs.getString("estadopago")));
                t.setEstadotrabajo(Trabajo.EstadoTrabajo.valueOf(rs.getString("estadotrabajo")));
                t.setMonto(rs.getFloat("monto"));
                t.setEstadodefacturacion(Trabajo.Estadodefacturacion.valueOf(rs.getString("estadodefacturacion")));
                // Las asociaciones se pueden cargar después con DAOs de Vehiculo y Aseguradora
                return t;
            }
        }
        return null;
    }

    // READ - todos
    public List<Trabajo> obtenerTodosLosTrabajos() throws SQLException {
        String sql = "SELECT * FROM Trabajo";
        List<Trabajo> trabajos = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("id"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFecha(rs.getDate("fecha").toLocalDate());
                t.setEstadopago(Trabajo.EstadoPago.valueOf(rs.getString("estadopago")));
                t.setEstadotrabajo(Trabajo.EstadoTrabajo.valueOf(rs.getString("estadotrabajo")));
                t.setMonto(rs.getFloat("monto"));
                t.setEstadodefacturacion(Trabajo.Estadodefacturacion.valueOf(rs.getString("estadodefacturacion")));
                trabajos.add(t);
            }
        }
        return trabajos;
    }

    // UPDATE
    public void actualizarTrabajo(Trabajo t) throws SQLException {
        String sql = "UPDATE Trabajo SET descripcion = ?, fecha = ?, estadopago = ?, estadotrabajo = ?, monto = ?, estadodefacturacion = ?, vehiculo_id = ?, aseguradora_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, t.getDescripcion());
            stmt.setDate(2, Date.valueOf(t.getFecha()));
            stmt.setString(3, t.getEstadopago().name());
            stmt.setString(4, t.getEstadotrabajo().name());
            stmt.setFloat(5, t.getMonto());
            stmt.setString(6, t.getEstadodefacturacion().name());
            stmt.setInt(7, t.getVehiculo() != null ? t.getVehiculo().getIdVehiculo() : 0);
            stmt.setInt(8, t.getAseguradora() != null ? t.getAseguradora().getIdAseguradora() : 0);
            stmt.setInt(9, t.getIdTrabajo());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminarTrabajo(int id) throws SQLException {
        String sql = "DELETE FROM Trabajo WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Trabajo> obtenerTrabajosNoFacturados() throws SQLException {
        List<Trabajo> trabajos = new ArrayList<>();
        String sql = "SELECT * FROM Trabajo WHERE estadodefacturacion = 'NOFACTURADO'";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("id"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFecha(rs.getDate("fecha").toLocalDate());
                t.setEstadopago(Trabajo.EstadoPago.valueOf(rs.getString("estadoPago")));
                t.setEstadotrabajo(Trabajo.EstadoTrabajo.valueOf(rs.getString("estadoTrabajo")));
                trabajos.add(t);
            }
        }

        return trabajos;
    }

}
