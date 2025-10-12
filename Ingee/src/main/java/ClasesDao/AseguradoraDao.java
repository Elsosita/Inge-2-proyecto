package ClasesDao;

import Clases.Aseguradora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AseguradoraDao {
    private Connection conexion;

    public AseguradoraDao() throws SQLException {
        this.conexion = ConexionBD.getConnection();
    }
    public AseguradoraDao(Connection conexion) {
        this.conexion = conexion;
    }
    // CREATE
    public void agregarAseguradora(Aseguradora a) throws SQLException {
        String sql = "INSERT INTO Aseguradora (nombre) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNombreAseguradora());
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
                        rs.getString("nombre")
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
                        rs.getString("nombre")
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
            stmt.setInt(2, a.getIdAseguradora());
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
    /*public void abrirOrdenDeProvision(int id) throws SQLException, IOException {
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
    }*/
    public List<Aseguradora> obtenerTodas() throws SQLException {
        List<Aseguradora> lista = new ArrayList<>();
        String sql = "SELECT * FROM Aseguradora";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Aseguradora a = new Aseguradora();
                a.setIdAseguradora(rs.getInt("id"));
                a.setNombreAseguradora(rs.getString("nombre"));
                lista.add(a);
            }
        }
        return lista;
    }
    public Aseguradora buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Aseguradora WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Aseguradora a = new Aseguradora();
                a.setIdAseguradora(rs.getInt("id"));
                a.setNombreAseguradora(rs.getString("nombre"));
                return a;
            }
        }
        return null;
    }

}
