package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Autor {

    @Id
    @GeneratedValue
    private long id;

    private String nombre;
    private String nacionalidad;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Libro> libros = new ArrayList<>();

    public Autor() {}

    public Autor(String nombre, String nacionalidad) {
        this.nombre       = nombre;
        this.nacionalidad = nacionalidad;
    }

    /** Mantiene la coherencia bidireccional de la relación */
    public void addLibro(Libro l) {
        libros.add(l);
        l.setAutor(this);
    }

    public long         getId()            { return id; }
    public String       getNombre()        { return nombre; }
    public void         setNombre(String n){ nombre = n; }
    public String       getNacionalidad()  { return nacionalidad; }
    public List<Libro>  getLibros()        { return libros; }

    @Override
    public String toString() {
        return "Autor{id=" + id + ", nombre='" + nombre
               + "', nacionalidad='" + nacionalidad
               + "', libros=" + libros.size() + "}";
    }
}
