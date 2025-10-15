package ClasesDao;

import Clases.Cliente;
import ClasesDao.ConexionBD;
import Clases.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private final Connection conexion;


    public ClienteDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }

    public ClienteDao(Connection conexion) {
        this.conexion = conexion;
    }


    public void agregarCliente(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (nombre, tipodocumento, numerodoc, numero) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.gettipodocumento().name());
            stmt.setInt(3, c.getNumerodoc());
            stmt.setLong(4, c.getNumero());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCliente(rs.getInt(1));
                }
            }
        }
    }


    public Cliente obtenerClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.settipodocumento(Cliente.TD.valueOf(rs.getString("tipodocumento")));
                    cliente.setNumerodoc(rs.getInt("numerodoc"));
                    cliente.setNumero(rs.getLong("numero"));

                    cliente.setVehiculos(obtenerVehiculosPorCliente(id));
                    return cliente;
                }
            }
        }
        return null;
    }


    public List<Cliente> obtenerTodosClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.settipodocumento(Cliente.TD.valueOf(rs.getString("tipodocumento")));
                c.setNumerodoc(rs.getInt("numerodoc"));
                c.setNumero(rs.getLong("numero"));
                lista.add(c);
            }
        }
        return lista;
    }


    public void actualizarCliente(Cliente c) throws SQLException {
        String sql = "UPDATE Cliente SET nombre=?, tipodocumento=?, numerodoc=?, numero=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.gettipodocumento().name());
            stmt.setInt(3, c.getNumerodoc());
            stmt.setLong(4, c.getNumero());
            stmt.setInt(5, c.getIdCliente());
            stmt.executeUpdate();
        }
    }


    public void actualizarTelefono(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET numero = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getNumero());
            stmt.setInt(2, cliente.getIdCliente());
            stmt.executeUpdate();
        }
    }


    public void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public List<Cliente> buscarClientesPorNombre(String texto) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE nombre LIKE ? OR numero LIKE ? LIMIT 10";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            String filtro = "%" + texto + "%";
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setNumero(rs.getLong("numero"));
                    clientes.add(c);
                }
            }
        }
        return clientes;
    }


    private List<Vehiculo> obtenerVehiculosPorCliente(int clienteId) throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo WHERE cliente_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vehiculo v = new Vehiculo();
                    v.setIdVehiculo(rs.getInt("id"));
                    v.setPatente(rs.getString("patente"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));
                    vehiculos.add(v);
                }
            }
        }
        return vehiculos;
    }


    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setNumero(rs.getLong("numero"));
                    return c;
                }
            }
        }
        return null;
    }
}
