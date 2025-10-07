package Clases;

import java.time.LocalDate;
import java.util.List;

public class Trabajo {
    private int id;
    private String descripcion;
    private LocalDate fecha;
    public enum EstadoPago { PENDIENTE, PAGADO }
    public enum EstadoTrabajo { PENDIENTE, EN_PROCESO, TERMINADO }
    private EstadoPago estadopago;
    private EstadoTrabajo estadotrabajo;
    private float monto;
    public enum Estadodefacturacion {FACTURADO, NOFACTURADO};
    private Estadodefacturacion estadodefacturacion;
    private Vehiculo vehiculo;
    private Aseguradora aseguradora;
    private List<Empleado> empleados;
    private List<Pago> pagos;


    public Trabajo() {}

    public Trabajo(int id, String descripcion, LocalDate fecha,EstadoPago estadopago, EstadoTrabajo estadotrabajo, float monto,Estadodefacturacion estadodefacturacion, Vehiculo vehiculo,  Aseguradora aseguradora, List<Empleado> empleados, List<Pago> pagos) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estadopago = estadopago;
        this.estadotrabajo = estadotrabajo;
        this.monto = monto;
        this.estadodefacturacion = estadodefacturacion;
        this.vehiculo = vehiculo;
        this.aseguradora = aseguradora;
        this.empleados = empleados;
        this.pagos = pagos;
    }

    public Trabajo(Trabajo trabajo) {
        this.id = trabajo.id;
        this.descripcion = trabajo.descripcion;
        this.fecha = trabajo.fecha;
        this.estadopago = trabajo.estadopago;
        this.estadotrabajo = trabajo.estadotrabajo;
        this.monto = trabajo.monto;
        this.estadodefacturacion = trabajo.estadodefacturacion;
        this.vehiculo = trabajo.vehiculo;
        this.aseguradora = trabajo.aseguradora;
        this.empleados = trabajo.empleados;
        this.pagos = trabajo.pagos;
    }

    public int getIdTrabajo() {
        return id;
    }

    public void setIdTrabajo(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoPago getEstadopago() {
        return estadopago;
    }

    public void setEstadopago(EstadoPago estadopago) {
        this.estadopago = estadopago;
    }

    public EstadoTrabajo getEstadotrabajo() {
        return estadotrabajo;
    }

    public void setEstadotrabajo(EstadoTrabajo estadotrabajo) {
        this.estadotrabajo = estadotrabajo;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Estadodefacturacion getEstadodefacturacion() {
        return estadodefacturacion;
    }
    public void setEstadodefacturacion(Estadodefacturacion estadodefacturacion) {
        this.estadodefacturacion = estadodefacturacion;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Aseguradora getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(Aseguradora aseguradora) {
        this.aseguradora = aseguradora;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }
    public void agregarEmpleado(Empleado empleado) {
        if (!empleados.contains(empleado)) {
            empleados.add(empleado);
            empleado.agregarTrabajo(this);
        }
    }

    public void agregarPago(Pago pago) {
        if (!pagos.contains(pago)) {
            pagos.add(pago);
        }
    }

    public List<Pago> getPagos() {
        return pagos;
    }

}
