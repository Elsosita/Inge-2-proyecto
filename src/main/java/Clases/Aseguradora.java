package Clases;

import java.util.ArrayList;
import java.util.List;

public class Aseguradora {
    private int id;
    private String nombre;
    private String ordenDeProvision;
    private List<Trabajo> trabajos = new ArrayList<>();

    public Aseguradora() {}

    public Aseguradora(String nombre, String ordenDeProvision) {
        this.nombre = nombre;
        this.ordenDeProvision = ordenDeProvision;
    }

    public Aseguradora(Aseguradora a) {
        this.nombre = a.nombre;
        this.ordenDeProvision = a.ordenDeProvision;
    }

    public int getIdAseguradora() {
        return id;
    }

    public void setIdAseguradora(int id) {
        this.id = id;
    }

    public String getNombreAseguradora() {
        return nombre;
    }

    public void setNombreAseguradora(String nombre) {
        this.nombre = nombre;
    }

    public String getOrdenDeProvision() {
        return ordenDeProvision;
    }

    public void setOrdenDeProvision(String ordenDeProvision) {
        this.ordenDeProvision = ordenDeProvision;
    }

    public List<Trabajo> getTrabajos() {
        return trabajos;
    }

    public void setTrabajos(List<Trabajo> trabajos) {
        this.trabajos = trabajos;
    }

    public void agregarTrabajo(Trabajo trabajo) {
        this.trabajos.add(trabajo);
        trabajo.setAseguradora(this);
    }
}


