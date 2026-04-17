package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;

/**
 * Clase base de la jerarquía de vehículos.
 * Con ObjectDB (OODB) la herencia funciona de forma natural;
 * la anotación @Inheritance la documenta explícitamente para JPA.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Vehiculo {

    @Id
    @GeneratedValue
    private long id;

    private String matricula;
    private int    anio;
    private String marca;

    protected Vehiculo() {}

    protected Vehiculo(String matricula, int anio, String marca) {
        this.matricula = matricula;
        this.anio      = anio;
        this.marca     = marca;
    }

    public long   getId()          { return id; }
    public String getMatricula()   { return matricula; }
    public int    getAnio()        { return anio; }
    public String getMarca()       { return marca; }

    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "{id=" + id + ", matricula='" + matricula
               + "', marca='" + marca + "', anio=" + anio + "}";
    }
}
