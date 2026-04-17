package pio.daw.ra8.ejemplos.consultas;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;

/**
 * BLOQUE 4 – Consultas JPQL básicas.
 * SELECT, COUNT, MAX, MIN, AVG y ORDER BY sobre la entidad Producto.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.consultas.EjemploConsultasBasicas"
 */
public class EjemploConsultasBasicas {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/consultas.odb");
        EntityManager em = emf.createEntityManager();

        try {
            cargarDatos(em);

            // ── SELECT todos ────────────────────────────────────────────────
            System.out.println("── Todos los productos ─────────────────────");
            em.createQuery("SELECT p FROM Producto p", Producto.class)
              .getResultList()
              .forEach(p -> System.out.println("  " + p));

            // ── ORDER BY precio DESC ────────────────────────────────────────
            System.out.println("\n── Ordenados por precio (mayor a menor) ────");
            em.createQuery(
                "SELECT p FROM Producto p ORDER BY p.precio DESC", Producto.class
            ).getResultList()
             .forEach(p -> System.out.printf("  %-25s → %.2f €%n",
                 p.getNombre(), p.getPrecio()));

            // ── COUNT ───────────────────────────────────────────────────────
            long total = em.createQuery(
                "SELECT COUNT(p) FROM Producto p", Long.class
            ).getSingleResult();
            System.out.println("\n── Total de productos: " + total);

            // ── MAX / MIN / AVG ─────────────────────────────────────────────
            double max = em.createQuery(
                "SELECT MAX(p.precio) FROM Producto p", Double.class
            ).getSingleResult();
            double min = em.createQuery(
                "SELECT MIN(p.precio) FROM Producto p", Double.class
            ).getSingleResult();
            double avg = em.createQuery(
                "SELECT AVG(p.precio) FROM Producto p", Double.class
            ).getSingleResult();

            System.out.printf("── MAX: %.2f €  |  MIN: %.2f €  |  AVG: %.2f €%n",
                max, min, avg);

            System.out.println("\n✓ Consultas básicas completadas.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void cargarDatos(EntityManager em) {
        em.getTransaction().begin();
        em.persist(new Producto("Teclado mecánico",    79.99, 20));
        em.persist(new Producto("Ratón inalámbrico",   34.50, 45));
        em.persist(new Producto("Monitor 24\"",        219.00, 10));
        em.persist(new Producto("Auriculares gaming",  59.95, 25));
        em.persist(new Producto("Webcam HD",            44.99, 30));
        em.persist(new Producto("Hub USB-C",            19.99,  8));
        em.getTransaction().commit();
        System.out.println("── Datos cargados ──────────────────────────\n");
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Consultas JPQL básicas");
        System.out.println("═".repeat(55));
    }
}
