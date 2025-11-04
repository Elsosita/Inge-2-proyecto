package Clases;

// Podrías necesitar el rol si más adelante quieres más usuarios.
public class Usuario {
    private int id;
    private String nombreUsuario; // El nombre de usuario (ej: 'admin')
    private String passwordHash;  // ¡El hash de la contraseña!
    private String rol;           // (Opcional) Ej: "ADMIN", "EMPLEADO"

    public Usuario() {}

    public Usuario(String nombreUsuario, String passwordHash, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    // Asumiendo que el campo en la BD se llama 'password_hash'

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}