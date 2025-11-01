package Managers;

import Clases.Trabajo;
import ClasesDao.TrabajoDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrabajoManager {

    private static TrabajoManager instancia;
    private TrabajoDao trabajoDao;

    // Constructor privado para Singleton
    private TrabajoManager() throws SQLException {
        this.trabajoDao = new TrabajoDao();
    }

    // Método para obtener la instancia única
    public static synchronized TrabajoManager getInstancia() throws SQLException {
        if (instancia == null) {
            instancia = new TrabajoManager();
        }
        return instancia;
    }

    public List<Trabajo> obtenerTrabajosNoFacturados() throws SQLException {
        return trabajoDao.obtenerTrabajosNoFacturados();
    }

    public List<Trabajo> obtenerTrabajosDelDia() {
        try {
            return trabajoDao.obtenerTrabajosDelDia();
        } catch (SQLException e) {
            System.err.println("Error al obtener trabajos del día: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Trabajo> obtenerTrabajosFacturados() throws SQLException {
        return trabajoDao.obtenerTrabajosFacturados();
    }

}
