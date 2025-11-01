package ClasesDao;
    //xdxd
import Clases.Cliente;
import ClasesDao.ConexionBD;
import Clases.Trabajo;
import Clases.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrabajoDao {
    private final Connection conexion;


    public TrabajoDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }

    public TrabajoDao(Connection conexion) {
        this.conexion = conexion;
    }


    public void agregarTrabajo(Trabajo t) throws SQLException {
        String sql = """
            INSERT INTO Trabajo 
            (descripcion, fecha, estadopago, estadotrabajo, monto, estadodefacturacion, vehiculo_id, aseguradora_id, ordendeprovision)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, t.getDescripcion());
            stmt.setDate(2, Date.valueOf(t.getFecha()));
            stmt.setString(3, t.getEstadopago().name());
            stmt.setString(4, t.getEstadotrabajo().name());
            stmt.setFloat(5, t.getMonto());
            stmt.setString(6, t.getEstadodefacturacion().name());
            stmt.setObject(7, t.getVehiculo() != null ? t.getVehiculo().getIdVehiculo() : null, Types.INTEGER);
            stmt.setObject(8, t.getAseguradora() != null ? t.getAseguradora().getIdAseguradora() : null, Types.INTEGER);
            stmt.setString(9, t.getOrdenDeProvision());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) t.setIdTrabajo(rs.getInt(1));
            }
        }
    }


    public Trabajo obtenerTrabajoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Trabajo WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearTrabajo(rs);
            }
        }
        return null;
    }


    public void actualizarTrabajo(Trabajo t) throws SQLException {
        String sql = """
            UPDATE Trabajo 
            SET descripcion = ?, fecha = ?, estadopago = ?, estadotrabajo = ?, monto = ?, 
                estadodefacturacion = ?, vehiculo_id = ?, aseguradora_id = ?, ordendeprovision = ?
            WHERE id = ?
        """;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, t.getDescripcion());
            stmt.setDate(2, Date.valueOf(t.getFecha()));
            stmt.setString(3, t.getEstadopago().name());
            stmt.setString(4, t.getEstadotrabajo().name());
            stmt.setFloat(5, t.getMonto());
            stmt.setString(6, t.getEstadodefacturacion().name());
            stmt.setObject(7, t.getVehiculo() != null ? t.getVehiculo().getIdVehiculo() : null, Types.INTEGER);
            stmt.setObject(8, t.getAseguradora() != null ? t.getAseguradora().getIdAseguradora() : null, Types.INTEGER);
            stmt.setString(9, t.getOrdenDeProvision());
            stmt.setInt(10, t.getIdTrabajo());
            stmt.executeUpdate();
        }
    }




    public void eliminarTrabajo(int id) throws SQLException {

        String sql = "DELETE FROM Trabajo WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Trabajo> TrabajoxId(int id) throws SQLException {
        String sql = """
    SELECT t.id AS id, t.descripcion, t.monto, t.fecha,
           v.id AS idVehiculo, v.patente, v.marca, v.modelo,
           c.id AS idCliente, c.nombre AS cliente_nombre, c.numero AS cliente_numero
    FROM Trabajo t
    JOIN Vehiculo v ON t.vehiculo_id = v.id
    JOIN Cliente c ON v.cliente_id = c.id
    WHERE t.id = ?
""";
        List<Trabajo> trabajo = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) trabajo.add(mapearTrabajoConCliente2(rs));
        }
        return trabajo;
    }



    public List<Trabajo> obtenerTrabajosNoFacturados() throws SQLException {
        String sql = """
    SELECT t.id AS id, t.descripcion, t.monto, t.fecha,
           v.id AS idVehiculo, v.patente, v.marca, v.modelo,
           c.id AS idCliente, c.nombre AS cliente_nombre, c.numero AS cliente_numero
    FROM Trabajo t
    JOIN Vehiculo v ON t.vehiculo_id = v.id
    JOIN Cliente c ON v.cliente_id = c.id
    WHERE t.estadodefacturacion != 'FACTURADO' OR t.estadodefacturacion IS NULL
""";

        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                trabajos.add(mapearTrabajoConCliente(rs));
            }
        }
        return trabajos;
    }


    public List<Trabajo> obtenerTodosLosTrabajos() throws SQLException {
        String sql = """
        SELECT t.id AS id, t.descripcion, t.monto, t.fecha,
               t.estadopago, t.estadotrabajo, t.estadodefacturacion, t.ordendeprovision,
               v.id AS idVehiculo, v.patente, v.marca, v.modelo,
               c.id AS idCliente, c.nombre AS cliente_nombre, c.numero AS cliente_numero
        FROM Trabajo t
        JOIN Vehiculo v ON t.vehiculo_id = v.id
        JOIN Cliente c ON v.cliente_id = c.id
        ORDER BY t.fecha DESC
    """;

        List<Trabajo> trabajos = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                trabajos.add(mapearTrabajoConCliente(rs));
            }
        }
        return trabajos;
    }


    public List<Trabajo> obtenerTrabajosDelDia() throws SQLException {
        String sql = """
            SELECT t.*, v.patente, v.marca, v.modelo, c.nombre AS cliente_nombre, c.numero AS cliente_numero
            FROM Trabajo t
            JOIN Vehiculo v ON t.vehiculo_id = v.id
            JOIN Cliente c ON v.cliente_id = c.id
            WHERE t.fecha = CURDATE()
        """;

        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) trabajos.add(mapearTrabajoConCliente(rs));
        }
        return trabajos;
    }


    public List<Trabajo> obtenerTrabajosFacturados() throws SQLException {
        String sql = """
            SELECT t.id AS id, t.descripcion, t.monto, t.fecha,
                   v.id AS idVehiculo, v.patente, v.marca, v.modelo,
                   c.id AS idCliente, c.nombre AS cliente_nombre, c.numero AS cliente_numero
            FROM Trabajo t
            JOIN Vehiculo v ON t.vehiculo_id = v.id
            JOIN Cliente c ON v.cliente_id = c.id
            WHERE t.estadodefacturacion = 'FACTURADO'
        """;

        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) trabajos.add(mapearTrabajoConCliente(rs));
        }
        return trabajos;
    }


    public List<Trabajo> obtenerTrabajosSinPago() throws SQLException {
        String sql = """
            SELECT t.*, v.patente, v.marca, v.modelo, c.nombre AS cliente_nombre, c.numero AS cliente_numero
            FROM Trabajo t
            JOIN Vehiculo v ON t.vehiculo_id = v.id
            JOIN Cliente c ON v.cliente_id = c.id
            WHERE t.estadopago = 'PENDIENTE'
        """;

        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) trabajos.add(mapearTrabajoConCliente(rs));
        }
        return trabajos;
    }


    private Trabajo mapearTrabajo(ResultSet rs) throws SQLException {
        Trabajo t = new Trabajo();
        t.setIdTrabajo(rs.getInt("id"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setFecha(rs.getDate("fecha").toLocalDate());
        t.setMonto(rs.getFloat("monto"));
        t.setEstadopago(Trabajo.EstadoPago.valueOf(rs.getString("estadopago")));
        System.out.print(t.getEstadopago().name());
        t.setEstadotrabajo(Trabajo.EstadoTrabajo.valueOf(rs.getString("estadotrabajo")));
        t.setEstadodefacturacion(Trabajo.Estadodefacturacion.valueOf(rs.getString("estadodefacturacion")));
        t.setOrdenDeProvision(rs.getString("ordendeprovision"));
        return t;
    }

    private Trabajo mapearTrabajoConCliente(ResultSet rs) throws SQLException {
        Trabajo t = new Trabajo();
        t.setIdTrabajo(rs.getInt("id"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setFecha(rs.getDate("fecha").toLocalDate());
        t.setMonto(rs.getFloat("monto"));
        //t.setEstadopago(Trabajo.EstadoPago.valueOf(rs.getString("estadoPago")));
        //t.setEstadotrabajo(Trabajo.EstadoTrabajo.valueOf(rs.getString("estadoTrabajo")));

        Vehiculo v = new Vehiculo();
        v.setPatente(rs.getString("patente"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));

        Cliente cliente = new Cliente();
        cliente.setNombre(rs.getString("cliente_nombre"));
        cliente.setNumero(rs.getLong("cliente_numero"));
        v.setCliente(cliente);
        t.setVehiculo(v);

        return t;
    }

    private Trabajo mapearTrabajoConCliente2(ResultSet rs) throws SQLException {
        Trabajo t = new Trabajo();
        t.setIdTrabajo(rs.getInt("id"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setFecha(rs.getDate("fecha").toLocalDate());
        t.setMonto(rs.getFloat("monto"));
        t.setEstadopago(Trabajo.EstadoPago.valueOf(rs.getString("estadoPago")));
        t.setEstadotrabajo(Trabajo.EstadoTrabajo.valueOf(rs.getString("estadoTrabajo")));

        Vehiculo v = new Vehiculo();
        v.setPatente(rs.getString("patente"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));

        Cliente cliente = new Cliente();
        cliente.setNombre(rs.getString("cliente_nombre"));
        cliente.setNumero(rs.getLong("cliente_numero"));
        v.setCliente(cliente);
        t.setVehiculo(v);

        return t;
    }

    public List<Trabajo> obtenerTrabajosConVehiculoYCliente() throws SQLException {
        String sql = """
        SELECT t.*, v.patente, v.marca, v.modelo, 
               c.nombre AS cliente_nombre, c.numero AS cliente_numero
        FROM Trabajo t
        JOIN Vehiculo v ON t.vehiculo_id = v.id
        JOIN Cliente c ON v.cliente_id = c.id
    """;

        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) trabajos.add(mapearTrabajoConCliente2(rs));
        }
        return trabajos;
    }
}
