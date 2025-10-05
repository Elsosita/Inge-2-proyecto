package Clases;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pago {
    private enum Tipo {Digital, Efectivo}
    private Tipo tipo;
    private float monto;
    private LocalDate fecha;
    private LocalTime hora;


    public Pago() {
    }

    public Pago(Tipo tipo, float m, LocalDate f, LocalTime h) {
        this.tipo = tipo;
        this.monto = m;
        this.fecha = f;
        this.hora = h;
    }

    public Pago (Pago pago) {
        this.tipo = pago.tipo;
        this.monto = pago.monto;
        this.fecha = pago.fecha;
        this.hora = pago.hora;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
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

}