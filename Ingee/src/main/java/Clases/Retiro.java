package Clases;

public class Retiro {
    private float monto;
    private String descripcion;
    private int codigoempleado_retiro;
    private int codigocaja_retiro;

    public Retiro (){}

    public Retiro (float monto, String descripcion, int codigoempleado_retiro, int codigocaja_retiro){
        this.monto=monto;
        this.descripcion=descripcion;
        this.codigoempleado_retiro=codigoempleado_retiro;
        this.codigocaja_retiro=codigocaja_retiro;
    }

    public Retiro(Retiro retiro){
        this.monto=retiro.monto;
        this.descripcion=retiro.descripcion;
        this.codigoempleado_retiro=retiro.codigoempleado_retiro;
        this.codigocaja_retiro=retiro.codigocaja_retiro;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigoempleado_retiro() {
        return codigoempleado_retiro;
    }

    public void setCodigoempleado_retiro(int codigoempleado_retiro) {
        this.codigoempleado_retiro = codigoempleado_retiro;
    }

    public int getCodigocaja_retiro() {
        return codigocaja_retiro;
    }

    public void setCodigocaja_retiro(int codigocaja_retiro) {
        this.codigocaja_retiro = codigocaja_retiro;
    }
}



