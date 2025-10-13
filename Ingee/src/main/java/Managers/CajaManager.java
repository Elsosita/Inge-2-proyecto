package Clases;

import ClasesDao.CajaDao;
import ClasesDao.PagoDao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class CajaManager {

    private static CajaManager instancia;

    private static CajaDao cajaDao;
    private final PagoDao pagoDao;

    private static Caja cajaAbierta;

    private CajaManager() throws SQLException {
        this.cajaDao = CajaDao.getInstancia();
        pagoDao = new PagoDao();
    }

    public static CajaManager getInstancia() {
        if (instancia == null) {
            try {
                instancia = new CajaManager();
            } catch (SQLException e) {
                throw new RuntimeException("Error al inicializar CajaManager", e);
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

    public void cerrarCaja() throws SQLException {
        Caja cajaAbierta = cajaDao.obtenerCajaAbierta();
        if (cajaAbierta != null) {
            cajaDao.cerrarCaja(getCajaAbierta().getIdCaja());
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

}

