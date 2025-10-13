package ClasesDao;

import Clases.Cliente;
import ClasesDao.ConexionBD;
import Clases.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDao {
    private final Connection conexion;

    // ✅ Usa la única conexión del Singleton
    public VehiculoDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }

    public VehiculoDao(Connection conexion) {
        this.conexion = conexion;
    }

    // ✅ CREATE
    public void agregarVehiculo(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO Vehiculo (patente, marca, modelo, cliente_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, v.getPatente());
            stmt.setString(2, v.getMarca());
            stmt.setString(3, v.getModelo());

            if (v.getCliente() != null && v.getCliente().getIdCliente() > 0) {
                stmt.setInt(4, v.getCliente().getIdCliente());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    v.setIdVehiculo(rs.getInt(1));
                }
            }
        }
    }

    // ✅ READ - por ID
    public Vehiculo obtenerVehiculoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Vehiculo WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearVehiculo(rs);
            }
        }
        return null;
    }

    // ✅ READ - todos
    public List<Vehiculo> obtenerTodosLosVehiculos() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapearVehiculo(rs));
        }
        return lista;
    }

    // ✅ UPDATE
    public void actualizarVehiculo(Vehiculo v) throws SQLException {
        String sql = "UPDATE Vehiculo SET patente=?, marca=?, modelo=?, cliente_id=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, v.getPatente());
            stmt.setString(2, v.getMarca());
            stmt.setString(3, v.getModelo());
            if (v.getCliente() != null && v.getCliente().getIdCliente() > 0) {
                stmt.setInt(4, v.getCliente().getIdCliente());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, v.getIdVehiculo());
            stmt.executeUpdate();
        }
    }

    // ✅ DELETE
    public void eliminarVehiculo(int id) throws SQLException {
        String sql = "DELETE FROM Vehiculo WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ✅ Buscar por cliente
    public List<Vehiculo> buscarPorCliente(int idCliente) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo WHERE cliente_id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapearVehiculo(rs));
            }
        }
        return lista;
    }

    // ✅ Buscar por patente (uno solo)
    public Vehiculo buscarPorPatente(String patente) throws SQLException {
        String sql = "SELECT * FROM Vehiculo WHERE patente = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, patente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vehiculo v = mapearVehiculo(rs);

                    // Cargar cliente si existe
                    int idCliente = rs.getInt("cliente_id");
                    if (idCliente > 0) {
                        ClienteDao clienteDao = new ClienteDao(conexion);
                        Cliente cliente = clienteDao.buscarPorId(idCliente);
                        v.setCliente(cliente);
                    }
                    return v;
                }
            }
        }
        return null;
    }

    // ✅ Buscar vehículos por texto de patente (parcial)
    public List<Vehiculo> buscarVehiculosPorPatente(String filtro) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = """
            SELECT v.id, v.patente, v.marca, v.modelo, c.nombre AS cliente_nombre
            FROM Vehiculo v
            JOIN Cliente c ON v.cliente_id = c.id
            WHERE v.patente LIKE ?
        """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vehiculo v = new Vehiculo();
                    v.setIdVehiculo(rs.getInt("id"));
                    v.setPatente(rs.getString("patente"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));

                    Cliente c = new Cliente();
                    c.setNombre(rs.getString("cliente_nombre"));
                    v.setCliente(c);

                    lista.add(v);
                }
            }
        }
        return lista;
    }

    // ✅ Método auxiliar para mapear ResultSet → Vehiculo
    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setIdVehiculo(rs.getInt("id"));
        v.setPatente(rs.getString("patente"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));

        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("cliente_id"));
        v.setCliente(c);

        return v;
    }
}
