package Clases;

import java.sql.SQLException;
import java.util.List;

public class TrabajoManager {
    private TrabajoDao trabajoDao;

    public TrabajoManager() throws SQLException {
        this.trabajoDao = new TrabajoDao();
    }

    public List<Trabajo> listarTrabajosNoFacturados() {
        try {
            return trabajoDao.obtenerTrabajosNoFacturados();
        } catch (SQLException e) {
            System.out.println("Error al obtener trabajos no facturados: " + e.getMessage());
            return null;
        }
    }
}

