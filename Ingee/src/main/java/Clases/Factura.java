package Clases;

public class Factura {
    private int numerodefactura;
    private enum EstadoDeFactura {facturado, nofacturado}
    private EstadoDeFactura estadodefactura;

    public Factura() {}

    public Factura(int numerodefactura, EstadoDeFactura estadodefactura) {
        this.numerodefactura = numerodefactura;
        this.estadodefactura = estadodefactura;
    }
    public Factura(Factura factura) {
        this.estadodefactura=factura.estadodefactura;
        this.numerodefactura=factura.numerodefactura;

    }

    public int getNumerodefactura() {
        return numerodefactura;
    }

    public void setNumerodefactura(int numerodefactura) {
        this.numerodefactura = numerodefactura;
    }

    public EstadoDeFactura getEstadodefactura() {
        return estadodefactura;
    }

    public void setEstadodefactura(EstadoDeFactura estadodefactura) {
        this.estadodefactura = estadodefactura;
    }

}
