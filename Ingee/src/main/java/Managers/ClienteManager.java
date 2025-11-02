package Managers;

import Clases.Cliente;
import ClasesDao.ClienteDao; // Asegúrate de que este DAO exista
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClienteManager {
    private final ClienteDao clienteDao;

    // Constructor que usa el constructor sin argumentos de ClienteDao (asume conexión interna)
    public ClienteManager() throws SQLException {
        // 🔥 Importante: Este constructor requiere que ClienteDao tenga un constructor ClienteDao()
        this.clienteDao = new ClienteDao();
    }

    // Constructor que recibe una Connection (para inyección de dependencia)
    public ClienteManager(Connection conexion) {
        this.clienteDao = new ClienteDao(conexion);
    }

    // ==========================================================
    // NUEVO MÉTODO PARA OBTENER TODOS LOS CLIENTES
    // ==========================================================
    /**
     * Obtiene una lista completa de clientes delegando al ClienteDao.
     * @return Lista de objetos Cliente.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<Cliente> obtenerTodos() throws SQLException {
        return clienteDao.obtenerTodos();
    }
    // ==========================================================

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
        return clienteDao.buscarPorId(id);
    }

    public void actualizarTelefono(Cliente cliente) throws SQLException {
        if (cliente == null || cliente.getIdCliente() <= 0) {
            throw new IllegalArgumentException("Cliente inválido o sin ID asignado.");
        }
        clienteDao.actualizarTelefono(cliente);
    }
}