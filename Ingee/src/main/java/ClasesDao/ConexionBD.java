package ClasesDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/GestionVehicular";
    private static final String USER = "root";      // tu usuario de MySQL
    private static final String PASSWORD = "mysql";  // tu contraseña

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
