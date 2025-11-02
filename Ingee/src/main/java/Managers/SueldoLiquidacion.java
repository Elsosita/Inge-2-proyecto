package Clases;

public class SueldoLiquidacion {
    private final String nombreEmpleado;
    private final float sueldoFijo;
    private final float comision;
    private final float totalPagar;

    public SueldoLiquidacion(String nombreEmpleado, float sueldoFijo, float comision, float totalPagar) {
        this.nombreEmpleado = nombreEmpleado;
        this.sueldoFijo = sueldoFijo;
        this.comision = comision;
        this.totalPagar = totalPagar;
    }

    // Getters
    public String getNombreEmpleado() { return nombreEmpleado; }
    public float getSueldoFijo() { return sueldoFijo; }
    public float getComision() { return comision; }
    public float getTotalPagar() { return totalPagar; }
}