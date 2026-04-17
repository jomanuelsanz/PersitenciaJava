package pio.daw.ra8.ejemplos.serializacion;

import java.io.Serializable;

/**
 * POJO serializable con un campo transient para demostrar
 * que ciertas propiedades pueden excluirse de la serialización.
 */
public class AlumnoSerial implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private double nota;
    private String curso;

    /**
     * transient: este campo NO se guarda en el stream.
     * Útil para contraseñas, tokens o conexiones abiertas.
     */
    private transient String password;

    public AlumnoSerial(String nombre, double nota, String curso) {
        this.nombre = nombre;
        this.nota   = nota;
        this.curso  = curso;
    }

    public String getNombre()          { return nombre; }
    public double getNota()            { return nota; }
    public String getCurso()           { return curso; }
    public String getPassword()        { return password; }
    public void   setPassword(String p){ password = p; }

    @Override
    public String toString() {
        return "Alumno{nombre='" + nombre + "', nota=" + nota + ", curso='" + curso + "'}";
    }
}
