package Managers;

import ClasesDao.AseguradoraDao;
import Clases.Aseguradora;
import ClasesDao.ClienteDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AseguradoraManager {

    private final AseguradoraDao aseguradoraDao;


    public AseguradoraManager(Connection conexion) throws SQLException {
        this.aseguradoraDao = new AseguradoraDao(conexion);
    }

    public List<Aseguradora> obtenerTodas() throws SQLException {
        return aseguradoraDao.obtenerTodas();
    }

    public Aseguradora buscarPorId(int id) throws SQLException {
        return aseguradoraDao.buscarPorId(id);
    }
}
