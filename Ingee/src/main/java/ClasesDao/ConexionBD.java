package ClasesDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instancia;
    private static Connection conexion;

    private static final String URL = "jdbc:mysql://localhost:3306/GestionVehicular";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    private ConexionBD() {
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConexionBD getInstance() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public static Connection getConnection() {
        return conexion;
    }
}
