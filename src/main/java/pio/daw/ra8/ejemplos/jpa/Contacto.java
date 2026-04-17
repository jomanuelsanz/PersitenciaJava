package pio.daw.ra8.ejemplos.jpa;

import jakarta.persistence.*;

@Entity
public class Contacto {

    @Id
    @GeneratedValue
    private long id;

    private String nombre;
    private String telefono;
    private String email;

    public Contacto() {}

    public Contacto(String nombre, String telefono, String email) {
        this.nombre   = nombre;
        this.telefono = telefono;
        this.email    = email;
    }

    public long   getId()               { return id; }
    public String getNombre()           { return nombre; }
    public void   setNombre(String n)   { nombre = n; }
    public String getTelefono()         { return telefono; }
    public void   setTelefono(String t) { telefono = t; }
    public String getEmail()            { return email; }
    public void   setEmail(String e)    { email = e; }

    @Override
    public String toString() {
        return "Contacto{id=" + id + ", nombre='" + nombre
               + "', tel='" + telefono + "', email='" + email + "'}";
    }
}
