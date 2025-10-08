package ClasesDao;
//aaa

import Clases.Cliente;
import Clases.ConexionBD;
import Clases.Trabajo;
import Clases.Vehiculo;

import java.sql.*;
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
            stmt.setInt(8, t.getAseguradora() != null ? t.getAseguradora().getIdAseguradora() : 1);
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
        String sql = """
        SELECT 
            t.id AS idTrabajo,
            t.descripcion AS descripcion,
            t.monto AS monto,
            t.fecha AS fecha,
            v.id AS idVehiculo,
            v.patente AS patente,
            v.marca AS marca,
            v.modelo AS modelo,
            c.id AS idCliente,
            c.nombre AS nombreCliente,
            c.numero AS telefonoCliente
        FROM Trabajo t
        JOIN Vehiculo v ON t.vehiculo_id = v.id
        JOIN Cliente c ON v.cliente_id = c.id
        WHERE t.estadodefacturacion = 'NOFACTURADO'
    """;

        List<Trabajo> trabajos = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Vehiculo
                Vehiculo v = new Vehiculo();
                v.setIdVehiculo(rs.getInt("idVehiculo"));
                v.setPatente(rs.getString("patente"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));

                // Cliente
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("idCliente"));
                c.setNombre(rs.getString("nombreCliente"));
                c.setNumero(rs.getInt("telefonoCliente"));

                v.setCliente(c);

                // Trabajo
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("idTrabajo"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setMonto(rs.getFloat("monto"));
                t.setVehiculo(v);

                trabajos.add(t);
            }
        }

        return trabajos;
    }

    public List<Trabajo> obtenerTrabajosDelDia() throws SQLException {
        List<Trabajo> trabajos = new ArrayList<>();
        String sql = "SELECT t.*, v.patente, v.marca, v.modelo, c.nombre AS cliente_nombre, c.numero AS cliente_numero " +
                "FROM Trabajo t " +
                "JOIN Vehiculo v ON t.vehiculo_id = v.id " +
                "JOIN Cliente c ON v.cliente_id = c.id " +
                "WHERE t.fecha = CURDATE()"; // Filtramos por fecha de hoy

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("id"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFecha(rs.getDate("fecha").toLocalDate());
                t.setMonto(rs.getFloat("monto"));

                // Vehículo
                Vehiculo v = new Vehiculo();
                v.setPatente(rs.getString("patente"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));

                // Cliente
                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("cliente_nombre"));
                cliente.setNumero((int) rs.getLong("cliente_numero"));
                v.setCliente(cliente);

                t.setVehiculo(v);
                trabajos.add(t);
            }
        }
        return trabajos;
    }

    public List<Trabajo> obtenerTrabajosFacturados() throws SQLException {
        String sql = """
        SELECT 
            t.id AS idTrabajo,
            t.descripcion AS descripcion,
            t.monto AS monto,
            t.fecha AS fecha,
            v.id AS idVehiculo,
            v.patente AS patente,
            v.marca AS marca,
            v.modelo AS modelo,
            c.id AS idCliente,
            c.nombre AS nombreCliente,
            c.numero AS telefonoCliente
        FROM Trabajo t
        JOIN Vehiculo v ON t.vehiculo_id = v.id
        JOIN Cliente c ON v.cliente_id = c.id
        WHERE t.estadodefacturacion = 'FACTURADO'
    """;

        List<Trabajo> trabajos = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Vehiculo
                Vehiculo v = new Vehiculo();
                v.setIdVehiculo(rs.getInt("idVehiculo"));
                v.setPatente(rs.getString("patente"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));

                // Cliente
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("idCliente"));
                c.setNombre(rs.getString("nombreCliente"));
                c.setNumero(rs.getInt("telefonoCliente"));

                v.setCliente(c);

                // Trabajo
                Trabajo t = new Trabajo();
                t.setIdTrabajo(rs.getInt("idTrabajo"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setMonto(rs.getFloat("monto"));
                t.setVehiculo(v);

                trabajos.add(t);
            }
        }

        return trabajos;
    }

}
