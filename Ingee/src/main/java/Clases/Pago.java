package Clases;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pago {
    private int id;
    public enum Tipo {Digital, Efectivo}
    private Tipo tipo;
    private float monto;
    private LocalDate fecha;
    private LocalTime hora;
    private Trabajo trabajo;
    private Caja caja;


    public Pago() {
    }

    public Pago(Tipo tipo, float m, LocalDate f, LocalTime h, Trabajo t, Caja c) {
        this.tipo = tipo;
        this.monto = m;
        this.fecha = f;
        this.hora = h;
        this.trabajo = t;
        this.caja = c;

        trabajo.agregarPago(this);
        caja.agregarPago(this);
    }

    public Pago (Pago pago) {
        this.tipo = pago.tipo;
        this.monto = pago.monto;
        this.fecha = pago.fecha;
        this.hora = pago.hora;
        this.trabajo = pago.trabajo;

        trabajo.agregarPago(this);
        caja.agregarPago(this);
    }
    public int getIdPago() {
        return id;
    }
    public Tipo getTipoPago() {
        return tipo;
    }

    public void setTipoPago(Tipo tipo) {
        this.tipo = tipo;
    }

    public float getMontoPago() {
        return monto;
    }

    public void setMontoPago(float monto) {
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fecha;
    }
    public void setFechaPago(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalTime getHoraPago() {
        return hora;
    }
    public void setHoraPago(LocalTime hora) {
        this.hora = hora;
    }
    public Trabajo getTrabajoPago() {
        return trabajo;
    }

    public void setTrabajoPago(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public Caja getCajaPago() {
        return caja;
    }

    public void setCajaPago(Caja caja) {
        this.caja = caja;
    }

}