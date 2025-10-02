package Clases;

import java.util.List;

public class Particular extends Cliente {

    public Particular() {
        super();
    }

    public Particular(int id, String nombre, TD tipodocumento, int numerodoc, int numero, List<Vehiculo> vehiculos) {
        super(id, nombre, tipodocumento, numerodoc, numero, vehiculos);
    }

    public Particular(Particular particular) {
        super(particular);
    }

}