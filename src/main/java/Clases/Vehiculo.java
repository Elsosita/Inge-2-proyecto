package Clases;

import java.util.ArrayList;
import java.util.List;
public class Vehiculo {
    private int id;
    private String patente;
    private String marca;
    private String modelo;
    private List<Trabajo> trabajos;
    private Cliente cliente;

    public Vehiculo(){}

    public Vehiculo(int id, String patente, String marca, String modelo, List<Trabajo> trabajos, Cliente cliente) {
        this.id = id;
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.trabajos = trabajos;
        this.cliente = cliente;

    }
    public Vehiculo(Vehiculo vehiculo) {
        this.id = vehiculo.id;
        this.patente = vehiculo.patente;
        this.marca = vehiculo.marca;
        this.modelo = vehiculo.modelo;
        this.trabajos = new ArrayList<>();
        this.cliente = vehiculo.cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void agregarTrabajo(Trabajo t) {
        trabajos.add(t);
    }

    public List<Trabajo> getTrabajos() {
        return trabajos;
    }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
