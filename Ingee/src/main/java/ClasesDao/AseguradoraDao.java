package ClasesDao;

import Clases.Aseguradora;
import ClasesDao.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AseguradoraDao {

    private final Connection conexion;

    public AseguradoraDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }


    public AseguradoraDao(Connection conexion) {
        this.conexion = conexion;
    }


    public void agregarAseguradora(Aseguradora a) throws SQLException {
        String sql = "INSERT INTO Aseguradora (nombre) VALUES (?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNombreAseguradora());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) a.setIdAseguradora(rs.getInt(1));
            }
        }
    }


    public Aseguradora obtenerAseguradoraPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Aseguradora WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Aseguradora(
                            rs.getInt("id"),
                            rs.getString("nombre")
                    );
                }
            }
        }
        return null;
    }


    public List<Aseguradora> listarTodas(boolean incluirSinAseguradora) throws SQLException {
        List<Aseguradora> lista = new ArrayList<>();
        String sql = incluirSinAseguradora
                ? "SELECT * FROM Aseguradora"
                : "SELECT * FROM Aseguradora WHERE id <> 1";

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


    public void actualizarAseguradora(Aseguradora a) throws SQLException {
        String sql = "UPDATE Aseguradora SET nombre = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, a.getNombreAseguradora());
            stmt.setInt(2, a.getIdAseguradora());
            stmt.executeUpdate();
        }
    }


    public void eliminarAseguradora(int id) throws SQLException {
        String sql = "DELETE FROM Aseguradora WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public Aseguradora buscarPorId(int id) throws SQLException {
        return obtenerAseguradoraPorId(id);
    }

}
