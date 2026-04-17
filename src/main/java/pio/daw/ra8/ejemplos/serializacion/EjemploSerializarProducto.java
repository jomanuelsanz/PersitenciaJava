package pio.daw.ra8.ejemplos.serializacion;

import java.io.*;

/**
 * BLOQUE 2 – Serialización básica.
 * Guarda un único objeto en disco y lo recupera en una nueva instancia.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploSerializarProducto"
 */
public class EjemploSerializarProducto {

    private static final String FICHERO = "target/producto.dat";

    public static void main(String[] args) {
        banner();
        new File("target").mkdirs();

        // ── 1. Crear y serializar ──────────────────────────────────────────
        ProductoSerial original = new ProductoSerial("Teclado mecánico", 79.99, 15);
        System.out.println("Serializando: " + original);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FICHERO))) {
            oos.writeObject(original);
        } catch (IOException e) {
            System.err.println("Error al serializar: " + e.getMessage());
            return;
        }

        long bytes = new File(FICHERO).length();
        System.out.println("Guardado en: " + FICHERO + " (" + bytes + " bytes)");

        // ── 2. Deserializar ───────────────────────────────────────────────
        System.out.println("\nDeserializando...");
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FICHERO))) {

            ProductoSerial recuperado = (ProductoSerial) ois.readObject();
            System.out.println("Recuperado:   " + recuperado);
            System.out.println("\n✓ Serialización completada con éxito.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar: " + e.getMessage());
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Serializar y deserializar un Producto");
        System.out.println("═".repeat(55));
    }
}
