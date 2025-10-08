package Controles;

import Clases.*;
import Managers.TrabajoManager;
import javafx.application.Application;

import java.sql.Connection;
import java.sql.SQLException;

public class Launcher {
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);

        try (Connection con = ConexionBD.getConnection()) {
            if (con != null) {
                System.out.println("¡Conexión exitosa!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TrabajoManager trabajoManager = null;
        try {
            trabajoManager = new TrabajoManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("===== Trabajos no facturados =====");

        // Mostramos los trabajos no facturados
        try {
            trabajoManager.obtenerTrabajosNoFacturados();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
