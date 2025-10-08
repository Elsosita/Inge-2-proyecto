package Clases;

import java.time.LocalTime;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class Caja {
    private  int id;
    private float montototal;
    private LocalDate  fecha;
    private LocalTime hora;
    public enum Estado{ABIERTA, CERRADA}
    private Estado estado;
    private ArrayList<Retiro> retiros = new ArrayList<>();
    private List<Pago> pagos = new ArrayList<>();


    public Caja(){}

    public Caja(float montototal, LocalDate fecha, LocalTime hora, Estado estado, ArrayList<Retiro> retiros, List<Pago> pagos) {
        this.montototal=montototal;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.estado = estado;
        this.retiros=retiros;
        this.pagos=pagos;
    }


    public Caja(Caja caja) {
        this.montototal = caja.montototal;
        this.fecha = caja.fecha;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.estado = caja.estado;
        this.retiros = new ArrayList<>(caja.retiros);
        this.pagos = new ArrayList<>(caja.pagos);
    }

    public int getIdCaja() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
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
