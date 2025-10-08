package Managers;


import Clases.Cliente;
import ClasesDao.ClienteDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ClienteManager {
    private final ClienteDao clienteDao;

    public ClienteManager() throws SQLException {
        this.clienteDao = new ClienteDao();
    }

    public ClienteManager(Connection conexion) {
        this.clienteDao = new ClienteDao(conexion);
    }

    public void registrarCliente(Cliente cliente) throws SQLException {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío");
        }
        if (cliente.getNumero() <= 0) {
            throw new IllegalArgumentException("El número de teléfono debe ser válido");
        }
        clienteDao.agregarCliente(cliente);
    }

    public List<Cliente> buscarClientes(String texto) throws SQLException {
        if (texto == null || texto.isBlank()) return List.of();
        return clienteDao.buscarClientesPorNombre(texto);
    }

    public Cliente obtenerPorId(int id) throws SQLException {
        return clienteDao.obtenerPorId(id);
    }

    public void actualizarTelefono(Cliente cliente) throws SQLException {
        if (cliente == null || cliente.getIdCliente() <= 0) {
            throw new IllegalArgumentException("Cliente inválido o sin ID asignado.");
        }
        clienteDao.actualizarTelefono(cliente);
    }

}
