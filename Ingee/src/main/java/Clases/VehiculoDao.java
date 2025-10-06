package Clases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDao {
    private Connection conexion;

    public VehiculoDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // CREATE
    public void agregarVehiculo(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO Vehiculo (patente, marca, modelo, cliente_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, v.getPatente());
            stmt.setString(2, v.getMarca());
            stmt.setString(3, v.getModelo());
            stmt.setInt(4, v.getCliente() != null ? v.getCliente().getIdCliente() : null); // FK

            stmt.executeUpdate();

            // Obtener id autogenerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                v.setIdVehiculo(rs.getInt(1));
            }
        }
    }

    // READ por ID
    public Vehiculo obtenerVehiculoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Vehiculo WHERE id = ?";
        Vehiculo v = null;

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                v = new Vehiculo();
                v.setIdVehiculo(rs.getInt("id"));
                v.setPatente(rs.getString("patente"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));

                // Cargar cliente mínimo (solo ID)
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("cliente_id"));
                v.setCliente(c);
            }
        }
        return v;
    }

    // UPDATE
    public void actualizarVehiculo(Vehiculo v) throws SQLException {
        String sql = "UPDATE Vehiculo SET patente = ?, marca = ?, modelo = ?, cliente_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, v.getPatente());
            stmt.setString(2, v.getMarca());
            stmt.setString(3, v.getModelo());
            stmt.setInt(4, v.getCliente() != null ? v.getCliente().getIdCliente() : null);
            stmt.setInt(5, v.getIdVehiculo());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminarVehiculo(int id) throws SQLException {
        String sql = "DELETE FROM Vehiculo WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Vehiculo> obtenerTodosLosVehiculos() throws SQLException {
        String sql = "SELECT * FROM Vehiculo";
        List<Vehiculo> lista = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setIdVehiculo(rs.getInt("id"));
                v.setPatente(rs.getString("patente"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));

                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("cliente_id"));
                v.setCliente(c);

                lista.add(v);
            }
        }
        return lista;
    }
}
