package Clases;

import java.util.ArrayList;
import java.time.LocalDate;

public class Caja {
    private float montototal;
    private LocalDate  fecha;
    private ArrayList<Retiro> retiros = new ArrayList<>();


    public Caja(){}

    public Caja(float montototal, LocalDate fecha, ArrayList<Retiro> retiros) {
        this.montototal=montototal;
        this.fecha=fecha;
        this.retiros=retiros;
    }


    public Caja(Caja caja) {
        this.montototal = caja.montototal;
        this.fecha = caja.fecha;
        this.retiros = new ArrayList<>(caja.retiros);
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

    public void setRetiros(ArrayList<Retiro> retiros) {
        this.retiros = retiros;
    }
    public void agregarRetiro(Retiro retiro) {
        this.retiros.add(retiro);
        this.montototal -= retiro.getMonto(); // si Retiro tiene getMonto()
    }



}
