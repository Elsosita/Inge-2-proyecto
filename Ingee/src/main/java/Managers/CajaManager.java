package Clases;

import ClasesDao.CajaDao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class CajaManager {

    private final CajaDao cajaDao;

    public CajaManager() {
        try {
            cajaDao = new CajaDao();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private static Caja cajaAbierta;

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

}

