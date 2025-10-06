package Clases;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TrabajoVista {
    private StringProperty patente;
    private StringProperty modelo;
    private StringProperty descripcion;
    private StringProperty nombreCliente;
    private StringProperty telefonoCliente;

    public TrabajoVista(String patente, String modelo, String descripcion, String nombreCliente, String telefonoCliente) {
        this.patente = new SimpleStringProperty(patente);
        this.modelo = new SimpleStringProperty(modelo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.nombreCliente = new SimpleStringProperty(nombreCliente);
        this.telefonoCliente = new SimpleStringProperty(telefonoCliente);
    }

    // Getters para TableView
    public StringProperty patenteProperty() { return patente; }
    public StringProperty modeloProperty() { return modelo; }
    public StringProperty descripcionProperty() { return descripcion; }
    public StringProperty nombreClienteProperty() { return nombreCliente; }
    public StringProperty telefonoClienteProperty() { return telefonoCliente; }
}
