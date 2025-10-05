package Clases;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class AseguradoraDao {
    private Connection conexion;

    public AseguradoraDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }

    // INSERT
    public void agregarAseguradora(Aseguradora a) throws SQLException {
        String sql = "INSERT INTO Aseguradora (nombre, ordenDeProvision) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, a.getNombreAseguradora());
            stmt.setString(2, a.getOrdenDeProvision());
            stmt.executeUpdate();
        }
    }

    // READ → obtener aseguradora por ID
    public Aseguradora obtenerAseguradoraPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Aseguradora WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Aseguradora(
                        rs.getString("nombre"),
                        rs.getString("ordenDeProvision")
                );
            }
        }
        return null;
    }

    // Método para abrir el PDF asociado a una aseguradora
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
