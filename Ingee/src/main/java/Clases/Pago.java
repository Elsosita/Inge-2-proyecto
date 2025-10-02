package Clases;

public class Pago {
    private enum Tipo {Digital, Efectivo}
    private Tipo tipo;
    private float monto;


    public Pago() {
    }

    public Pago(Tipo tipo, float m) {
        this.tipo = tipo;
        this.monto = m;
    }

    public Pago (Pago pago) {
        this.tipo = pago.tipo;
        this.monto = pago.monto;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

}