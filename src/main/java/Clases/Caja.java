package Clases;

import java.util.ArrayList;

public class Caja {
    private float montototal;
    private ArrayList<Retiro> retiros = new ArrayList<>();


    public Caja(){}

    public Caja(float montototal){
        this.montototal=montototal;
    }


    public Caja(Caja caja) {
        this.montototal = caja.montototal;
        this.retiros = new ArrayList<>(caja.retiros);
    }


    public float getMontototal() {
        return montototal;
    }

    public void setMontototal(float montototal) {
        this.montototal = montototal;
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
