package ClasesDao;

import Clases.Usuario;
import java.sql.*;

public class UsuarioDao {
    private final Connection conexion;

    public UsuarioDao() throws SQLException {
        this.conexion = ConexionBD.getInstance().getConnection();
    }


    public Usuario buscarPorNombreUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT id, nombreUsuario, passwordHash, rol FROM Usuario WHERE nombreUsuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombreUsuario(rs.getString("nombreUsuario"));
                    u.setPasswordHash(rs.getString("passwordHash"));
                    u.setRol(rs.getString("rol"));
                    return u;
                }
            }
        }
        return null;
    }

    public void insertar(Usuario u) throws SQLException {
        String sql = "INSERT INTO Usuario (nombreUsuario, passwordHash, rol) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, u.getNombreUsuario());
            stmt.setString(2, u.getPasswordHash());
            stmt.setString(3, u.getRol());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }
    }
}