package Clases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private Connection conexion;

    public ClienteDao(Connection conexion) {
        this.conexion = conexion;
    }

    // CREATE → agregar un cliente
    public void agregarCliente(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (nombre, tipodocumento, numerodoc, numero) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.gettipodocumento().name()); // Convertimos enum a String
            stmt.setInt(3, c.getNumerodoc());
            stmt.setInt(4, c.getNumero());
            stmt.executeUpdate();

            // Obtener id generado y asignarlo al objeto
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ → obtener cliente por ID (y sus vehículos)
    public Cliente obtenerClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        Cliente cliente = null;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.settipodocumento(Cliente.TD.valueOf(rs.getString("tipodocumento")));
                    cliente.setNumerodoc(rs.getInt("numerodoc"));
                    cliente.setNumero(rs.getInt("numero"));

                    // Cargar vehículos del cliente
                    cliente.setVehiculos(obtenerVehiculosPorCliente(id));
                }
            }
        }
        return cliente;
    }

    // READ → obtener todos los clientes
    public List<Cliente> obtenerTodosClientes() throws SQLException {
        String sql = "SELECT id FROM Cliente";
        List<Cliente> lista = new ArrayList<>();
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(obtenerClientePorId(rs.getInt("id")));
            }
        }
        return lista;
    }

    // UPDATE → modificar cliente
    public void actualizarCliente(Cliente c) throws SQLException {
        String sql = "UPDATE Cliente SET nombre=?, tipodocumento=?, numerodoc=?, numero=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.gettipodocumento().name());
            stmt.setInt(3, c.getNumerodoc());
            stmt.setInt(4, c.getNumero());
            stmt.setInt(5, c.getId());
            stmt.executeUpdate();
        }
    }

    // DELETE → eliminar cliente
    public void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // MÉTODO AUXILIAR → obtener vehículos de un cliente
    private List<Vehiculo> obtenerVehiculosPorCliente(int clienteId) throws SQLException {
        String sql = "SELECT * FROM Vehiculo WHERE cliente_id=?";
        List<Vehiculo> vehiculos = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vehiculo v = new Vehiculo();
                    v.setId(rs.getInt("id"));
                    v.setPatente(rs.getString("patente"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));
                    vehiculos.add(v);
                }
            }
        }
        return vehiculos;
    }
}

