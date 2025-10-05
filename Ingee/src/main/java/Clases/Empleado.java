package Clases;
 import java.util.ArrayList;
 import java.util.List;

public class Empleado {
    private String nombre;
    private int telefono;
    private float sueldo;
    private List<Trabajo> trabajos;
    private List<Retiro> retiros;

    public Empleado() {}

    public Empleado(String nombre, int telefono, float sueldo, List<Trabajo> trabajos, List<Retiro> retiros) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.sueldo = sueldo;
        this.trabajos = trabajos;
        this.retiros = retiros;
    }

    public Empleado(Empleado empleado) {
        this.nombre= empleado.nombre;
        this.telefono= empleado.telefono;
        this.sueldo= empleado.sueldo;
        this.trabajos = empleado.trabajos;
        this.retiros = empleado.retiros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public float getSueldo() {
        return sueldo;
    }

    public void setSueldo(float sueldo) {
        this.sueldo = sueldo;
    }

    public List<Trabajo> getTrabajos() {
        return trabajos;
    }

    public void agregarTrabajo(Trabajo trabajo) {
        if (!trabajos.contains(trabajo)) {
            trabajos.add(trabajo);
            trabajo.agregarEmpleado(this);
        }
    }

    public void agregarRetiro(Retiro retiro) {
        if (!retiros.contains(retiro)) {
            retiros.add(retiro);
        }
    }

    public List<Retiro> getRetiros() {
        return retiros;
    }

}
