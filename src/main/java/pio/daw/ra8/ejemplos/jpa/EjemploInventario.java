package pio.daw.ra8.ejemplos.jpa;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 3 – Gestión de inventario.
 * Demuestra actualizaciones masivas con JPQL UPDATE y consultas de resumen.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploInventario"
 */
public class EjemploInventario {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/inventario.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Cargar catálogo inicial ──────────────────────────────────────
            System.out.println("── Catálogo inicial ────────────────────────");
            em.getTransaction().begin();
            em.persist(new Producto("Teclado mecánico",    79.99, 20));
            em.persist(new Producto("Ratón inalámbrico",   34.50, 45));
            em.persist(new Producto("Monitor 24\"",        219.00, 10));
            em.persist(new Producto("Auriculares gaming",  59.95, 25));
            em.persist(new Producto("Webcam HD",            44.99, 30));
            em.getTransaction().commit();
            listar(em);

            // ── Subida de precio del 10% ─────────────────────────────────────
            System.out.println("\n── Subida de precios +10% (UPDATE masivo) ──");
            em.getTransaction().begin();
            int actualizados = em.createQuery(
                "UPDATE Producto p SET p.precio = p.precio * 1.10"
            ).executeUpdate();
            em.getTransaction().commit();
            // Limpiar la caché de primer nivel para ver los nuevos valores
            em.clear();
            System.out.println("  " + actualizados + " productos actualizados.");
            listar(em);

            // ── Productos con stock bajo ─────────────────────────────────────
            System.out.println("\n── Productos con stock < 15 ────────────────");
            List<Producto> bajStock = em.createQuery(
                "SELECT p FROM Producto p WHERE p.stock < :limite ORDER BY p.stock",
                Producto.class
            ).setParameter("limite", 15).getResultList();
            bajStock.forEach(p -> System.out.printf("  %-25s stock=%d%n",
                p.getNombre(), p.getStock()));

            // ── Precio medio del catálogo ────────────────────────────────────
            double media = em.createQuery(
                "SELECT AVG(p.precio) FROM Producto p", Double.class
            ).getSingleResult();
            System.out.printf("%n── Precio medio del catálogo: %.2f €%n", media);

            System.out.println("\n✓ Gestión de inventario completada.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void listar(EntityManager em) {
        em.createQuery("SELECT p FROM Producto p ORDER BY p.nombre", Producto.class)
          .getResultList()
          .forEach(p -> System.out.printf("  %-25s %.2f € (stock=%d)%n",
              p.getNombre(), p.getPrecio(), p.getStock()));
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Gestión de Inventario");
        System.out.println("═".repeat(55));
    }
}
