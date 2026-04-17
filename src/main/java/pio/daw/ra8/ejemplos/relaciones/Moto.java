package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;

@Entity
public class Moto extends Vehiculo {

    private boolean tieneSidecar;

    public Moto() {}

    public Moto(String matricula, int anio, String marca, boolean tieneSidecar) {
        super(matricula, anio, marca);
        this.tieneSidecar = tieneSidecar;
    }

    public boolean isTieneSidecar()         { return tieneSidecar; }
    public void    setTieneSidecar(boolean s) { tieneSidecar = s; }

    @Override
    public String toString() {
        return super.toString().replace("}", ", sidecar=" + tieneSidecar + "}");
    }
}
