package Controles;

import Clases.*;
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

    }


}
