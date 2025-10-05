package Clases;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AseguradoraDao {
    private Connection conexion;

    public AseguradoraDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // CREATE
    public void agregarAseguradora(Aseguradora a) throws SQLException {
        String sql = "INSERT INTO Aseguradora (nombre, ordenDeProvision) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNombreAseguradora());
            stmt.setString(2, a.getOrdenDeProvision());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) a.setIdAseguradora(rs.getInt(1));
        }
    }

    // READ por ID
    public Aseguradora obtenerAseguradoraPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Aseguradora WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Aseguradora(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ordenDeProvision")
                );
            }
        }
        return null;
    }

    // READ todos
    public List<Aseguradora> listarTodas() throws SQLException {
        List<Aseguradora> lista = new ArrayList<>();
        String sql = "SELECT * FROM Aseguradora";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Aseguradora(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ordenDeProvision")
                ));
            }
        }
        return lista;
    }

    // UPDATE
    public void actualizarAseguradora(Aseguradora a) throws SQLException {
        String sql = "UPDATE Aseguradora SET nombre=?, ordenDeProvision=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, a.getNombreAseguradora());
            stmt.setString(2, a.getOrdenDeProvision());
            stmt.setInt(3, a.getIdAseguradora());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminarAseguradora(int id) throws SQLException {
        String sql = "DELETE FROM Aseguradora WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public void abrirOrdenDeProvision(int id) throws SQLException, IOException {
        Aseguradora a = obtenerAseguradoraPorId(id);
        if (a != null && a.getOrdenDeProvision() != null) {
            File archivo = new File(a.getOrdenDeProvision());
            if (archivo.exists()) {
                Desktop.getDesktop().open(archivo); // abre el PDF con el programa por defecto
            } else {
                System.out.println("El archivo no existe en la ruta: " + archivo.getAbsolutePath());
            }
        } else {
            System.out.println("No se encontró la aseguradora o no tiene orden de provisión.");
        }
    }
}
