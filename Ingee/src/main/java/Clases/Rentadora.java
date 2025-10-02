package Clases;

public class Rentadora extends Cliente {
    private int idr;
    private String nombrerentadora;

    public Rentadora() {}

    public Rentadora(String nombrerentadora, int idr) {
        this.nombrerentadora = nombrerentadora;
        this.idr = idr;
    }

    public Rentadora(Rentadora r) {
        this.nombrerentadora = r.nombrerentadora;
        this.idr = r.idr;
    }

    public String getNombreRentadora() {
        return nombrerentadora;
    }

    public void setNombreRentadora(String nombre) {
        this.nombrerentadora = nombre;
    }

    public int getIdRentadora() {
        return idr;
    }

    public void setIdRentadora(int id) {
        this.idr = id;
    }

    @Override
    public String toString() {
        return "Rentadora{" +
                "idr=" + idr +
                ", nombrerentadora='" + nombrerentadora + '\'' +
                '}';
    }
}
