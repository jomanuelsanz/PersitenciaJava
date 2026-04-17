package pio.daw.ra8.ejemplos.serializacion;

import java.io.Serializable;

/**
 * POJO serializable (sin JPA). Sirve como punto de partida antes de usar un OODB.
 */
public class ProductoSerial implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private double precio;
    private int    stock;

    public ProductoSerial(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock  = stock;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int    getStock()  { return stock; }

    @Override
    public String toString() {
        return "Producto{nombre='" + nombre + "', precio=" + precio + ", stock=" + stock + "}";
    }
}
