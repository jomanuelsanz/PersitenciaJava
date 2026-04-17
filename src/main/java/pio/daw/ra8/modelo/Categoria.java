package pio.daw.ra8.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Categoria {

    @Id
    @GeneratedValue
    private long id;

    private String nombre;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();

    /** Constructor vacío obligatorio para JPA */
    public Categoria() {}

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /** Método de conveniencia que mantiene la coherencia bidireccional */
    public void addProducto(Producto p) {
        productos.add(p);
        p.setCategoria(this);
    }

    public void removeProducto(Producto p) {
        productos.remove(p);
        p.setCategoria(null);
    }

    // ── Getters / Setters ──────────────────────────────────────────────────

    public long getId()               { return id; }
    public String getNombre()         { return nombre; }
    public void setNombre(String n)   { nombre = n; }
    public List<Producto> getProductos() { return productos; }

    @Override
    public String toString() {
        return "Categoria{id=" + id + ", nombre='" + nombre
               + "', productos=" + productos.size() + "}";
    }
}
