package pio.daw.ra8.util;

import jakarta.persistence.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para crear y cerrar EntityManagerFactory en ejemplos standalone.
 * Cada llamada a crearEMF() elimina el fichero .odb existente para
 * garantizar que cada ejecución empieza con una base de datos limpia.
 */
public class JPAUtil {

    /**
     * Crea un EntityManagerFactory apuntando al fichero indicado.
     * Borra el fichero previo si existe (inicio limpio en cada demo).
     */
    public static EntityManagerFactory crearEMF(String fichero) {
        eliminarBBDD(fichero);
        // Crear directorio padre si no existe (p.e. target/)
        File f = new File(fichero);
        if (f.getParentFile() != null) {
            f.getParentFile().mkdirs();
        }
        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", fichero);
        return Persistence.createEntityManagerFactory("ra8DB", props);
    }

    /** Elimina el fichero .odb y el fichero de diario asociado ($). */
    public static void eliminarBBDD(String fichero) {
        new File(fichero).delete();
        new File(fichero + "$").delete();
    }

    public static void cerrar(EntityManagerFactory emf) {
        if (emf != null && emf.isOpen()) emf.close();
    }
}
