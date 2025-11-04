package Managers;

import Clases.Usuario;
import ClasesDao.UsuarioDao;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthManager {
    private static AuthManager instancia;
    private final UsuarioDao usuarioDao;

    // Patrón Singleton para el Manager
    private AuthManager() throws SQLException {
        this.usuarioDao = new UsuarioDao();
    }

    public static AuthManager getInstancia() {
        if (instancia == null) {
            try {
                instancia = new AuthManager();
            } catch (SQLException e) {
                // Manejo de error al obtener la conexión (similar a CajaManager)
                throw new RuntimeException("Error al inicializar AuthManager", e);
            }
        }
        return instancia;
    }


    // 🔒 MÉTODO DE HASHEO (PRIVADO) 🔒
    // Usa SHA-256 para convertir la contraseña en una cadena segura
    private String hashearPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            // Codificar el hash binario a una cadena Base64 (texto seguro)
            return Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Algoritmo de hasheo no soportado.", e);
        }
    }

    /**
     * Intenta autenticar a un usuario con su nombre y contraseña.
     * @param nombreUsuario El nombre de usuario ingresado.
     * @param password La contraseña en texto plano ingresada.
     * @return El objeto Usuario si la autenticación es exitosa, o null si falla.
     */
    public Usuario autenticar(String nombreUsuario, String password) throws SQLException {
        Usuario usuario = usuarioDao.buscarPorNombreUsuario(nombreUsuario);

        if (usuario != null) {
            // 1. Hashear la contraseña ingresada por el usuario
            String hashIngresado = hashearPassword(password);

            // 2. Comparar el hash de la entrada con el hash guardado en la BD
            if (hashIngresado.equals(usuario.getPasswordHash())) {
                return usuario; // ✅ Autenticación exitosa
            }
        }
        return null; // ❌ Autenticación fallida
    }
}