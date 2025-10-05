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

        try {
            AseguradoraDao dao = new AseguradoraDao();
            Aseguradora nueva = new Aseguradora("La Segura S.A.", "C:/Users/User/Desktop/Facultad/Sistemas operativos/Teoría 2 AdP.pdf");
            dao.agregarAseguradora(nueva);
            dao.abrirOrdenDeProvision(1);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
