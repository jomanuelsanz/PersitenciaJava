package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;

@Entity
public class Coche extends Vehiculo {

    private int numeroPuertas;

    public Coche() {}

    public Coche(String matricula, int anio, String marca, int numeroPuertas) {
        super(matricula, anio, marca);
        this.numeroPuertas = numeroPuertas;
    }

    public int  getNumeroPuertas()      { return numeroPuertas; }
    public void setNumeroPuertas(int n) { numeroPuertas = n; }

    @Override
    public String toString() {
        return super.toString().replace("}", ", puertas=" + numeroPuertas + "}");
    }
}
