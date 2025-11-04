package ClasesDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instancia;
    private Connection conexion;

    private static final String URL = "jdbc:mysql://localhost:3306/GestionVehicular?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    private ConexionBD() {
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión OK");
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

    public Connection getConnection() {
        return conexion;
    }
}
