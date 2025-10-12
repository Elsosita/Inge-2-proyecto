package ClasesDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // 🔒 Instancia única de la conexión
    private static Connection conexion;

    // 🔧 Datos de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/GestionVehicular";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    // 🚫 Constructor privado → impide crear instancias de esta clase
    private ConexionBD() { }

    // ✅ Método público para obtener la conexión única
    public static Connection getConnection() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión establecida correctamente.");
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
                throw e;
            }
        }
        return conexion;
    }
}
