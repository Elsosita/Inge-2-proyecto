package Clases;

import java.util.ArrayList;
import java.util.List;

public class Aseguradora {
    private int id;
    private String nombre;
    private List<Trabajo> trabajos = new ArrayList<>();

    public Aseguradora() {}

    public Aseguradora(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Aseguradora(String nombre, List<Trabajo> trabajos) {
        this.nombre = nombre;
        this.trabajos = trabajos;
    }

    public Aseguradora(Aseguradora a) {
        this.nombre = a.nombre;

        this.trabajos = new ArrayList<>(a.trabajos);
    }

    // Getters y Setters
    public int getIdAseguradora() { return id; }
    public void setIdAseguradora(int id) { this.id = id; }

    public String getNombreAseguradora() { return nombre; }
    public void setNombreAseguradora(String nombre) { this.nombre = nombre; }

    public List<Trabajo> getTrabajosAseguradora() { return trabajos; }
    public void setTrabajosAseguradora(List<Trabajo> trabajos) { this.trabajos = trabajos; }
}
