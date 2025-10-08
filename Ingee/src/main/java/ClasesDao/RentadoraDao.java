package ClasesDao;

import Clases.ConexionBD;
import Clases.Rentadora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentadoraDao {
    private Connection conexion;

    public RentadoraDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // CREATE
    public void agregarRentadora(Rentadora r) throws SQLException {
        String sql = "INSERT INTO Rentadora (nombrerentadora) VALUES (?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, r.getNombreRentadora());
            stmt.executeUpdate();

            // Obtener id autogenerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                r.setIdRentadora(rs.getInt(1));
            }
        }
    }

    // READ (por id)
    public Rentadora obtenerRentadoraPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Rentadora WHERE idr = ?";
        Rentadora r = null;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                r = new Rentadora();
                r.setIdRentadora(rs.getInt("idr"));
                r.setNombreRentadora(rs.getString("nombrerentadora"));
            }
        }
        return r;
    }

    // READ (todas)
    public List<Rentadora> obtenerTodasLasRentadoras() throws SQLException {
        String sql = "SELECT * FROM Rentadora";
        List<Rentadora> rentadoras = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Rentadora r = new Rentadora();
                r.setIdRentadora(rs.getInt("idr"));
                r.setNombreRentadora(rs.getString("nombrerentadora"));
                rentadoras.add(r);
            }
        }
        return rentadoras;
    }

    // UPDATE
    public void actualizarRentadora(Rentadora r) throws SQLException {
        String sql = "UPDATE Rentadora SET nombrerentadora = ? WHERE idr = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, r.getNombreRentadora());
            stmt.setInt(2, r.getIdRentadora());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminarRentadora(int id) throws SQLException {
        String sql = "DELETE FROM Rentadora WHERE idr = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
