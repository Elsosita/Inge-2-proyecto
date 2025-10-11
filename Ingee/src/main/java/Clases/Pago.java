package Clases;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pago {
    private int id;
    public enum Tipo {DIGITAL, EFECTIVO}
    private Tipo tipo;
    private float monto;
    private LocalDate fecha;
    private LocalTime hora;
    private Trabajo trabajo;
    private Caja caja;

    public Pago() {}

    public Pago(Tipo tipo, float monto, Trabajo trabajo, Caja caja) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.trabajo = trabajo;
        this.caja = caja;

        if (trabajo != null)
            trabajo.agregarPago(this);
        if (caja != null)
            caja.agregarPago(this);
    }

    // Getters y Setters
    public int getIdPago() { return id; }

    public Tipo getTipoPago() { return tipo; }
    public void setTipoPago(Tipo tipo) { this.tipo = tipo; }

    public float getMontoPago() { return monto; }
    public void setMontoPago(float monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fecha; }
    public void setFechaPago(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraPago() { return hora; }
    public void setHoraPago(LocalTime hora) { this.hora = hora; }

    public Trabajo getTrabajoPago() { return trabajo; }
    public void setTrabajoPago(Trabajo trabajo) { this.trabajo = trabajo; }

    public Caja getCajaPago() { return caja; }
    public void setCajaPago(Caja caja) { this.caja = caja; }
}
