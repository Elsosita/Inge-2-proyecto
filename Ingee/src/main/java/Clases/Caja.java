package Clases;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class Caja {
    private float montototal;
    private LocalDate  fecha;
    private ArrayList<Retiro> retiros = new ArrayList<>();
    private List<Pago> pagos;


    public Caja(){}

    public Caja(float montototal, LocalDate fecha, ArrayList<Retiro> retiros, List<Pago> pagos) {
        this.montototal=montototal;
        this.fecha=fecha;
        this.retiros=retiros;
        this.pagos=pagos;
    }


    public Caja(Caja caja) {
        this.montototal = caja.montototal;
        this.fecha = caja.fecha;
        this.retiros = new ArrayList<>(caja.retiros);
        this.pagos = new ArrayList<>(caja.pagos);
    }


    public float getMontototal() {
        return montototal;
    }

    public void setMontototal(float montototal) {
        this.montototal = montototal;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Retiro> getRetiros() {
        return retiros;
    }

    public void agregarRetiro(Retiro retiro) {
        if (!retiros.contains(retiro)) {
            retiros.add(retiro);
        }
    }

    public void agregarPago(Pago pago) {
        if (!pagos.contains(pago)) {
            pagos.add(pago);
        }
    }

    public List<Pago> getPagos() {
        return pagos;
    }


}
