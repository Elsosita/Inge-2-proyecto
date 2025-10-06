package Clases;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nombre;
    public enum TD { DNI, CUIT, CUIL };
    private TD tipodocumento;
    private int numerodoc;
    private int numero;
    private List<Vehiculo> vehiculos = new ArrayList<>();

    public Cliente() {}

    public Cliente(int id, String nombre,TD tipodocumento, int numerodoc, int numero, List<Vehiculo> vehiculos) {
        this.id = id;
        this.nombre = nombre;
        this.tipodocumento= tipodocumento;
        this.numerodoc = numerodoc;
        this.numero = numero;
        this.vehiculos = vehiculos;
    }
    public Cliente(Cliente c) {
        this.id = c.id;
        this.nombre = c.nombre;
        this.tipodocumento= c.tipodocumento;
        this.numerodoc = c.numerodoc;
        this.numero = c.numero;
        this.vehiculos= c.vehiculos;
    }

    public int getIdCliente() {
        return id;
    }
    public void setIdCliente(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TD gettipodocumento() {
        return tipodocumento;
    }

    public void settipodocumento(TD tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public int getNumerodoc() {
        return numerodoc;
    }
    public void setNumerodoc(int numerodoc) {
        this.numerodoc = numerodoc;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void agregarVehiculo(Vehiculo v) {
        vehiculos.add(v);
        v.setCliente(this);
    }

    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) { this.vehiculos = vehiculos; }

}
