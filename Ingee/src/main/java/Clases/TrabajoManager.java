package Clases;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrabajoManager {
    private TrabajoDao trabajoDao;

    public TrabajoManager() throws SQLException {
        this.trabajoDao = new TrabajoDao();
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

}



