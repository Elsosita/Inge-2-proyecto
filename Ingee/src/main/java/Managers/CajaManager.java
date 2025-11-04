package Clases;

import ClasesDao.CajaDao;
import ClasesDao.PagoDao;
import ClasesDao.RetiroDao;
import ClasesDao.EmpleadoDao;
import ClasesDao.TrabajoDao;
import Clases.SueldoLiquidacion;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CajaManager {

    private static CajaManager instancia;

    // 🔥 CAMBIO: 'cajaDao' ya no es estático para ser consistente con el patrón Singleton de la clase Manager
    private final CajaDao cajaDao;
    private final PagoDao pagoDao;
    private final RetiroDao retiroDao;
    private final EmpleadoDao empleadoDao;
    private final TrabajoDao trabajoDao;

    private static Caja cajaAbierta;

    private CajaManager() throws SQLException {
        // Inicialización de DAOs (Singleton y New Instance)
        this.cajaDao = CajaDao.getInstancia();
        this.pagoDao = new PagoDao();
        this.retiroDao = RetiroDao.getInstancia();
        this.empleadoDao = EmpleadoDao.getInstancia();
        this.trabajoDao = new TrabajoDao();
    }

    public static CajaManager getInstancia() {
        if (instancia == null) {
            try {
                instancia = new CajaManager();
            } catch (SQLException e) {
                // Manejar la excepción lanzada por los DAOs
                throw new RuntimeException("Error al inicializar CajaManager o sus DAOs", e);
            }
        }
        return instancia;
    }

    public void abrirCaja(float montoInicial) throws SQLException {
        Caja caja = new Caja();
        caja.setMontototal(montoInicial);
        caja.setMontoefectivo(montoInicial);
        caja.setMontodigital(0);
        caja.setFecha(LocalDate.now());
        caja.setHora(LocalTime.now());
        caja.setEstado(Caja.Estado.ABIERTA);

        cajaDao.abrirCaja(caja);
    }

    public boolean hayCajaAbierta() throws SQLException {
        return cajaDao.obtenerCajaAbierta() != null;
    }

    public static void setCajaAbierta(Caja caja) {
        cajaAbierta = caja;
    }

    public static Caja getCajaAbierta() {
        return cajaAbierta;
    }

    // Este método ya no es necesario si se usa cerrarCajaConSueldos(), pero se mantiene por si acaso
    public void cerrarCaja() throws SQLException {
        Caja cajaAbierta = cajaDao.obtenerCajaAbierta();
        if (cajaAbierta != null) {
            cajaDao.cerrarCaja(cajaAbierta.getIdCaja());
        }
    }

    public void registrarPago(Pago nuevoPago) throws SQLException {
        pagoDao.insertar(nuevoPago);

        Caja caja = nuevoPago.getCajaPago();
        float monto = nuevoPago.getMontoPago();

        if (nuevoPago.getTipoPago() == Pago.Tipo.EFECTIVO) {
            caja.setMontoefectivo(caja.getMontoefectivo() + monto);
        } else {
            caja.setMontodigital(caja.getMontodigital() + monto);
        }
        caja.setMontototal(caja.getMontototal() + monto);

        cajaDao.actualizarMontos(caja);
    }

    // ==========================================================
    // LÓGICA DE NEGOCIO: CERRAR CAJA Y LIQUIDAR SUELDOS (REAL)
    // ==========================================================

    public void cerrarCajaConSueldos() throws SQLException {
        Caja caja = getCajaAbierta();

        if (caja == null) {
            throw new IllegalStateException("No se encontró ninguna caja abierta en el sistema.");
        }

        List<Empleado> empleados = empleadoDao.listarTodos();
        float montoTotalRetirosSueldos = 0;

        // 1. Calcular, registrar los Retiros por Sueldo y acumular el total a descontar
        for (Empleado empleado : empleados) {
            float sueldoTotal = calcularSueldoEmpleado(empleado, caja.getFecha());

            if (sueldoTotal > 0) {
                // Crea el objeto Retiro
                Retiro retiroSueldo = new Retiro(
                        sueldoTotal,
                        "Pago Sueldo y Comisión (" + caja.getFecha().toString() + ")",
                        empleado.getIdEmpleado(),
                        caja.getIdCaja()
                );

                // Registra el Retiro en la BD usando el DAO
                retiroDao.insertar(retiroSueldo);

                // Actualiza el monto en la caja (en memoria)
                montoTotalRetirosSueldos += sueldoTotal;
            }
        }

        // 2. Actualizar los montos finales en la caja (en memoria)
        caja.setMontototal(caja.getMontototal() - montoTotalRetirosSueldos);
        caja.setMontoefectivo(caja.getMontoefectivo() - montoTotalRetirosSueldos);

        // 3. Persistir los cambios y cerrar la caja en la BD
        cajaDao.actualizarMontos(caja);
        cajaDao.cerrarCaja(caja);

        // 4. Limpiar la referencia de caja abierta
        setCajaAbierta(null);
    }

    /**
     * Calcula el sueldo total: Sueldo Fijo + (5% de la suma de montos de sus Trabajos completados).
     */
    private float calcularSueldoEmpleado(Empleado empleado, LocalDate fechaCaja) throws SQLException {
        // 5% de la facturación generada (Trabajos pagados desde la fecha de caja)
        float totalGenerado = trabajoDao.sumarMontoTrabajosPorEmpleadoYFecha(
                empleado.getIdEmpleado(),
                fechaCaja
        );

        float comision = totalGenerado * 0.05f;
        float sueldoFijo = empleado.getSueldoEmpleado();

        return sueldoFijo + comision;
    }




    public Caja simularCierreCajaConSueldos() throws SQLException {
        Caja cajaAbiertaActual = getCajaAbierta();

        if (cajaAbiertaActual == null) {
            return null;
        }

        // Crear una COPIA de la caja para la simulación (Caja tiene constructor de copia)
        Caja cajaSimulada = new Caja(cajaAbiertaActual);

        LocalDate fechaInicioCalculo = cajaAbiertaActual.getFecha();

        List<Empleado> empleados = empleadoDao.listarTodos();
        float montoTotalRetirosSueldos = 0;

        // 1. Calcular el total de SUELDOS (fijo + comisión) a pagar para la simulación
        for (Empleado empleado : empleados) {
            // USAMOS EL MISMO MÉTODO QUE EN EL CIERRE REAL
            float sueldoTotal = calcularSueldoEmpleado(empleado, fechaInicioCalculo);
            montoTotalRetirosSueldos += sueldoTotal;
        }

        // 2. Aplicar los descuentos a la caja simulada
        cajaSimulada.setMontototal(cajaSimulada.getMontototal() - montoTotalRetirosSueldos);
        cajaSimulada.setMontoefectivo(cajaSimulada.getMontoefectivo() - montoTotalRetirosSueldos);

        return cajaSimulada;
    }
    public List<SueldoLiquidacion> obtenerDetalleLiquidacionSueldos() throws SQLException {
        Caja caja = getCajaAbierta();

        if (caja == null) {
            return new ArrayList<>(); // Devuelve una lista vacía si no hay caja abierta
        }

        LocalDate fechaInicioCalculo = caja.getFecha();
        List<Empleado> empleados = empleadoDao.listarTodos();
        List<SueldoLiquidacion> detalles = new ArrayList<>();

        for (Empleado empleado : empleados) {
            // Obtenemos los componentes del cálculo para ser transparentes
            float totalGenerado = trabajoDao.sumarMontoTrabajosPorEmpleadoYFecha(
                    empleado.getIdEmpleado(),
                    fechaInicioCalculo
            );

            float comision = totalGenerado * 0.05f;
            // Si tienes el sueldo fijo hardcodeado a 3000f, usa ese valor aquí:
            float sueldoFijo = empleado.getSueldoEmpleado();

            float totalPagar = sueldoFijo + comision;

            detalles.add(new SueldoLiquidacion(
                    empleado.getNombreEmpleado(),
                    sueldoFijo,
                    comision,
                    totalPagar
            ));
        }
        return detalles;
    }
}