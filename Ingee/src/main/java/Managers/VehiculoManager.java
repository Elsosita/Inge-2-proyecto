package Managers;

import Clases.*;
import ClasesDao.ClienteDao;
import ClasesDao.VehiculoDao;
import java.sql.Connection;
import java.sql.SQLException;

public class VehiculoManager {

    private final VehiculoDao vehiculoDao;

    public VehiculoManager(Connection conexion) {
        this.vehiculoDao = new VehiculoDao(conexion);
    }

    public Vehiculo buscarPorPatente(String patente) throws SQLException {
        return vehiculoDao.buscarPorPatente(patente);
    }

    public void registrarVehiculo(Vehiculo v) throws SQLException {
        vehiculoDao.agregarVehiculo(v);
    }
}