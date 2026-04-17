package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;

@Entity
public class Libro {

    @Id
    @GeneratedValue
    private long id;

    private String titulo;
    private String isbn;
    private double precio;
    private int    anioPublicacion;

    @ManyToOne
    private Autor autor;

    public Libro() {}

    public Libro(String titulo, String isbn, double precio, int anioPublicacion) {
        this.titulo          = titulo;
        this.isbn            = isbn;
        this.precio          = precio;
        this.anioPublicacion = anioPublicacion;
    }

    public long   getId()                  { return id; }
    public String getTitulo()              { return titulo; }
    public void   setTitulo(String t)      { titulo = t; }
    public String getIsbn()                { return isbn; }
    public double getPrecio()              { return precio; }
    public void   setPrecio(double p)      { precio = p; }
    public int    getAnioPublicacion()     { return anioPublicacion; }
    public Autor  getAutor()               { return autor; }
    public void   setAutor(Autor a)        { autor = a; }

    @Override
    public String toString() {
        String nombreAutor = autor != null ? autor.getNombre() : "desconocido";
        return "Libro{id=" + id + ", titulo='" + titulo
               + "', isbn='" + isbn + "', precio=" + precio
               + ", anio=" + anioPublicacion
               + ", autor='" + nombreAutor + "'}";
    }
}
