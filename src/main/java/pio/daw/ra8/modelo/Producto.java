package pio.daw.ra8.modelo;

import jakarta.persistence.*;

@Entity
public class Producto {

    @Id
    @GeneratedValue
    private long id;

    private String nombre;
    private double precio;
    private int    stock;

    /** Relación opcional: un producto puede no pertenecer a ninguna categoría */
    @ManyToOne
    private Categoria categoria;

    public Producto() {}

    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock  = stock;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────

    public long      getId()             { return id; }
    public String    getNombre()         { return nombre; }
    public void      setNombre(String n) { nombre = n; }
    public double    getPrecio()         { return precio; }
    public void      setPrecio(double p) { precio = p; }
    public int       getStock()          { return stock; }
    public void      setStock(int s)     { stock = s; }
    public Categoria getCategoria()      { return categoria; }
    public void      setCategoria(Categoria c) { categoria = c; }

    @Override
    public String toString() {
        String cat = categoria != null ? categoria.getNombre() : "sin categoría";
        return "Producto{id=" + id + ", nombre='" + nombre
               + "', precio=" + precio + ", stock=" + stock
               + ", categoria='" + cat + "'}";
    }
}
