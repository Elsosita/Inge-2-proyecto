package Clases;

import Clases.Retiro;
import Clases.Trabajo;

import java.util.ArrayList;
import java.util.List;

public class Empleado {
    private int id;
    private String nombre;
    private int telefono;
    private float sueldo;
    private List<Trabajo> trabajos = new ArrayList<>();
    private List<Retiro> retiros = new ArrayList<>();

    public Empleado() {}

    // Constructor completo
    public Empleado(int id, String nombre, int telefono, float sueldo, List<Trabajo> trabajos, List<Retiro> retiros) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.sueldo = sueldo;
        this.trabajos = trabajos != null ? trabajos : new ArrayList<>();
        this.retiros = retiros != null ? retiros : new ArrayList<>();
    }

    // Getters y setters únicos
    public int getIdEmpleado() { return id; }
    public void setIdEmpleado(int id) { this.id = id; }

    public String getNombreEmpleado() { return nombre; }
    public void setNombreEmpleado(String nombre) { this.nombre = nombre; }

    public int getTelefonoEmpleado() { return telefono; }
    public void setTelefonoEmpleado(int telefono) { this.telefono = telefono; }

    public float getSueldoEmpleado() { return sueldo; }
    public void setSueldoEmpleado(float sueldo) { this.sueldo = sueldo; }

    public List<Trabajo> getTrabajosEmpleado() { return trabajos; }
    public void setTrabajosEmpleado(List<Trabajo> trabajos) { this.trabajos = trabajos; }

    public List<Retiro> getRetirosEmpleado() { return retiros; }
    public void setRetirosEmpleado(List<Retiro> retiros) { this.retiros = retiros; }
}
