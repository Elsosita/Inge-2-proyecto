package Clases;

import java.sql.SQLException;
import java.util.List;

public class TrabajoManager {
    private TrabajoDao trabajoDao;

    public TrabajoManager() throws SQLException {
        this.trabajoDao = new TrabajoDao();
    }

    public void mostrarTrabajosNoFacturados() {
        try {
            List<Trabajo> trabajos = trabajoDao.obtenerTrabajosNoFacturados();
            for (Trabajo t : trabajos) {
                System.out.println("Trabajo: " + t.getDescripcion() + ", Monto: " + t.getMonto());
                System.out.println("Vehiculo: " + t.getVehiculo().getPatente() + " ("
                        + t.getVehiculo().getMarca() + " "
                        + t.getVehiculo().getModelo() + ")");
                System.out.println("Cliente: " + t.getVehiculo().getCliente().getNombre()
                        + ", Tel: " + t.getVehiculo().getCliente().getNumero());
                System.out.println("-------------------------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener trabajos no facturados: " + e.getMessage());
        }

    }

}

