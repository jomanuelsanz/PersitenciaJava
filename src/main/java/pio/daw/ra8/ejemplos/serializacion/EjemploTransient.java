package pio.daw.ra8.ejemplos.serializacion;

import java.io.*;

/**
 * BLOQUE 2 – Campos transient.
 * Demuestra que un campo marcado como transient no se guarda en el stream;
 * tras la deserialización su valor es null (o 0 para tipos primitivos).
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploTransient"
 */
public class EjemploTransient {

    public static void main(String[] args) {
        banner();

        AlumnoSerial alumno = new AlumnoSerial("Lucía Torres", 9.2, "DAW2");
        alumno.setPassword("s3cr3t0!");

        System.out.println("Antes de serializar:");
        System.out.println("  " + alumno);
        System.out.println("  password = '" + alumno.getPassword() + "'  ← se declaró transient");

        // Serializar en memoria (ByteArrayOutputStream para no tocar disco)
        byte[] bytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(alumno);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // Deserializar desde el byte array
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(bytes))) {

            AlumnoSerial recuperado = (AlumnoSerial) ois.readObject();

            System.out.println("\nDespués de deserializar:");
            System.out.println("  " + recuperado);
            System.out.println("  password = '" + recuperado.getPassword() + "'  ← es null (no se serializó)");

            System.out.println("\n✓ Comprobado: los campos transient no persisten.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Campos transient en serialización");
        System.out.println("═".repeat(55));
    }
}
