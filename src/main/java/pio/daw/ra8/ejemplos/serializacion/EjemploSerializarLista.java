package pio.daw.ra8.ejemplos.serializacion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BLOQUE 2 – Serialización de una colección de objetos.
 * Muestra que se puede serializar directamente un List<T>
 * siempre que T implemente Serializable.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploSerializarLista"
 */
public class EjemploSerializarLista {

    private static final String FICHERO = "target/alumnos.dat";

    public static void main(String[] args) {
        banner();
        new File("target").mkdirs();

        // ── 1. Construir la lista ──────────────────────────────────────────
        List<AlumnoSerial> alumnos = new ArrayList<>();
        alumnos.add(new AlumnoSerial("Ana García",    9.5, "DAW1"));
        alumnos.add(new AlumnoSerial("Carlos Ruiz",   7.0, "DAW2"));
        alumnos.add(new AlumnoSerial("María López",   8.3, "DAW1"));
        alumnos.add(new AlumnoSerial("Jorge Martín",  6.5, "DAW2"));

        System.out.println("Serializando " + alumnos.size() + " alumnos...");

        // ── 2. Guardar lista completa ──────────────────────────────────────
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FICHERO))) {
            oos.writeObject(alumnos);
            System.out.println("Guardado en: " + FICHERO);
        } catch (IOException e) {
            System.err.println("Error al serializar: " + e.getMessage());
            return;
        }

        // ── 3. Recuperar lista ────────────────────────────────────────────
        System.out.println("\nDeserializando...");
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FICHERO))) {

            @SuppressWarnings("unchecked")
            List<AlumnoSerial> recuperados = (List<AlumnoSerial>) ois.readObject();

            recuperados.forEach(a -> System.out.println("  - " + a));
            System.out.println("\n✓ Lista recuperada: " + recuperados.size() + " alumnos.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar: " + e.getMessage());
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Serializar una lista de Alumnos");
        System.out.println("═".repeat(55));
    }
}
