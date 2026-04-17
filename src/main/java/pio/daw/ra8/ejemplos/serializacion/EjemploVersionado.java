package pio.daw.ra8.ejemplos.serializacion;

import java.io.*;
import java.util.Arrays;

/**
 * BLOQUE 2 – serialVersionUID y compatibilidad entre versiones.
 *
 * Demuestra dos escenarios:
 *   1. Serialización/deserialización normal (UIDs coinciden → OK).
 *   2. Simulación de cambio de UID: se parchea el stream en memoria
 *      para provocar InvalidClassException en tiempo de ejecución.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploVersionado"
 */
public class EjemploVersionado {

    public static void main(String[] args) throws Exception {
        banner();

        // ── 1. Serialización normal ────────────────────────────────────────
        System.out.println("1. SERIALIZACIÓN NORMAL (serialVersionUID = 1L)");
        System.out.println("─".repeat(50));

        AlumnoSerial alumno = new AlumnoSerial("Pedro Sánchez", 8.1, "DAW1");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(alumno);
        }
        byte[] bytes = baos.toByteArray();
        System.out.println("   Serializado: " + bytes.length + " bytes");

        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(bytes))) {
            AlumnoSerial r = (AlumnoSerial) ois.readObject();
            System.out.println("   Recuperado OK: " + r);
        }

        // ── 2. Simulación de UID incompatible ─────────────────────────────
        System.out.println();
        System.out.println("2. SIMULACIÓN DE CAMBIO DE serialVersionUID");
        System.out.println("─".repeat(50));
        System.out.println("   Parcheando el stream para cambiar el UID almacenado...");

        /*
         * Formato del ObjectStream (primeros bytes):
         *  [0-1]   0xAC 0xED   → STREAM_MAGIC
         *  [2-3]   0x00 0x05   → STREAM_VERSION
         *  [4]     0x73        → TC_OBJECT
         *  [5]     0x72        → TC_CLASSDESC
         *  [6-7]   longitud del nombre de clase (big-endian)
         *  [8 .. 8+len-1]      nombre de clase (UTF-8)
         *  [8+len .. 8+len+7]  serialVersionUID (long, 8 bytes)  ← aquí parcheamos
         */
        byte[] patched = Arrays.copyOf(bytes, bytes.length);

        // Verificación de seguridad: comprobamos que el formato es el esperado
        if (patched[4] == (byte) 0x73 && patched[5] == (byte) 0x72) {
            int classNameLen = AlumnoSerial.class.getName().length();
            int uidOffset    = 8 + classNameLen;

            // Cambiamos los 8 bytes del UID a 0xFF...FF (-1L), diferente de 1L
            for (int i = uidOffset; i < uidOffset + 8; i++) {
                patched[i] = (byte) 0xFF;
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(patched))) {
                ois.readObject();
                System.out.println("   ERROR: no debería llegar aquí.");
            } catch (InvalidClassException e) {
                System.out.println("   ✗ InvalidClassException capturada:");
                System.out.println("     " + e.getMessage());
            }

            System.out.println();
            System.out.println("   → Esto ocurre cuando la clase fue modificada en disco");
            System.out.println("     y su serialVersionUID no coincide con el del stream.");
        }

        // ── 3. Consejo ────────────────────────────────────────────────────
        System.out.println();
        System.out.println("3. CONSEJO");
        System.out.println("─".repeat(50));
        System.out.println("   Declara SIEMPRE serialVersionUID explícitamente.");
        System.out.println("   Sin él, Java calcula un UID automático en función de");
        System.out.println("   la estructura de la clase. Cualquier cambio (nuevo campo,");
        System.out.println("   nuevo método) rompe la compatibilidad con datos anteriores.");
        System.out.println();

        long uid = ObjectStreamClass.lookup(AlumnoSerial.class).getSerialVersionUID();
        System.out.println("   serialVersionUID actual de AlumnoSerial: " + uid);
        System.out.println("\n✓ Demo de versionado completada.");
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: serialVersionUID y compatibilidad");
        System.out.println("═".repeat(55));
    }
}
