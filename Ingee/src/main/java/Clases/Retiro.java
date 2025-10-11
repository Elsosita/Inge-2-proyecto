package Clases;

import java.time.LocalDate;
import java.time.LocalTime;

public class Retiro {
    private int id;
    private float monto;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;
    private int codigoempleado_retiro;
    private int codigocaja_retiro;

    public Retiro() {}

    public Retiro(float monto, String descripcion, int codigoempleado_retiro, int codigocaja_retiro) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.codigoempleado_retiro = codigoempleado_retiro;
        this.codigocaja_retiro = codigocaja_retiro;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public float getMonto() { return monto; }
    public void setMonto(float monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCodigoempleado_retiro() { return codigoempleado_retiro; }
    public void setCodigoempleado_retiro(int codigoempleado_retiro) { this.codigoempleado_retiro = codigoempleado_retiro; }

    public int getCodigocaja_retiro() { return codigocaja_retiro; }
    public void setCodigocaja_retiro(int codigocaja_retiro) { this.codigocaja_retiro = codigocaja_retiro; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
}
