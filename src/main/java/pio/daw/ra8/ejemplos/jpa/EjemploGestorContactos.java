package pio.daw.ra8.ejemplos.jpa;

import jakarta.persistence.*;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 3 – Gestor de contactos (agenda personal).
 * Aplica CRUD sobre una entidad diferente a Producto para consolidar el patrón.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploGestorContactos"
 */
public class EjemploGestorContactos {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/contactos.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Insertar contactos ───────────────────────────────────────────
            System.out.println("── Insertando contactos ────────────────────");
            em.getTransaction().begin();
            em.persist(new Contacto("Ana Martínez",  "612 345 678", "ana@email.com"));
            em.persist(new Contacto("Luis García",   "699 111 222", "luis@email.com"));
            em.persist(new Contacto("Sara Jiménez",  "633 987 654", "sara@email.com"));
            Contacto carlos = new Contacto("Carlos López", "644 000 111", "carlos@email.com");
            em.persist(carlos);
            em.getTransaction().commit();
            long idCarlos = carlos.getId();
            System.out.println("  4 contactos guardados.");

            // ── Buscar por ID ────────────────────────────────────────────────
            System.out.println("\n── Buscar por ID ───────────────────────────");
            Contacto encontrado = em.find(Contacto.class, idCarlos);
            System.out.println("  " + encontrado);

            // ── Actualizar teléfono ──────────────────────────────────────────
            System.out.println("\n── Actualizar teléfono de Carlos ───────────");
            em.getTransaction().begin();
            encontrado.setTelefono("644 999 888");
            em.getTransaction().commit();
            System.out.println("  " + em.find(Contacto.class, idCarlos));

            // ── Eliminar un contacto ─────────────────────────────────────────
            System.out.println("\n── Eliminar contacto de Carlos ─────────────");
            em.getTransaction().begin();
            em.remove(encontrado);
            em.getTransaction().commit();

            // ── Listar todos ─────────────────────────────────────────────────
            System.out.println("\n── Contactos restantes ─────────────────────");
            List<Contacto> todos = em.createQuery(
                "SELECT c FROM Contacto c ORDER BY c.nombre", Contacto.class
            ).getResultList();
            todos.forEach(c -> System.out.println("  " + c));

            System.out.println("\n✓ Gestor de contactos completado.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Gestor de Contactos (agenda)");
        System.out.println("═".repeat(55));
    }
}
