package pio.daw.ra8.ejemplos.jpa;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;

/**
 * BLOQUE 3 – CRUD completo con JPA y ObjectDB.
 * Cubre: persist, find, actualizar campo, remove.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploCRUDProducto"
 */
public class EjemploCRUDProducto {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/crud.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── CREATE ──────────────────────────────────────────────────────
            System.out.println("── CREATE ──────────────────────────────────");
            em.getTransaction().begin();
            Producto teclado = new Producto("Teclado mecánico", 79.99, 15);
            Producto raton   = new Producto("Ratón inalámbrico", 34.50, 30);
            em.persist(teclado);
            em.persist(raton);
            em.getTransaction().commit();
            System.out.println("  Guardados: " + teclado);
            System.out.println("             " + raton);

            long idTeclado = teclado.getId();

            // ── READ ─────────────────────────────────────────────────────────
            System.out.println("\n── READ ────────────────────────────────────");
            Producto encontrado = em.find(Producto.class, idTeclado);
            System.out.println("  find(id=" + idTeclado + ") → " + encontrado);

            // find con ID inexistente devuelve null (no lanza excepción)
            Producto noExiste = em.find(Producto.class, 9999L);
            System.out.println("  find(id=9999)      → " + noExiste + "  (no existe)");

            // ── UPDATE ───────────────────────────────────────────────────────
            System.out.println("\n── UPDATE ──────────────────────────────────");
            em.getTransaction().begin();
            encontrado.setPrecio(69.99);   // JPA detecta el cambio automáticamente
            encontrado.setStock(encontrado.getStock() - 1);
            em.getTransaction().commit();
            System.out.println("  Precio rebajado → " + em.find(Producto.class, idTeclado));

            // ── DELETE ───────────────────────────────────────────────────────
            System.out.println("\n── DELETE ──────────────────────────────────");
            em.getTransaction().begin();
            em.remove(encontrado);
            em.getTransaction().commit();

            Producto borrado = em.find(Producto.class, idTeclado);
            System.out.println("  Tras remove: find(id=" + idTeclado + ") → " + borrado + "  (eliminado)");

            System.out.println("\n✓ CRUD completado con éxito.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: CRUD básico con JPA + ObjectDB");
        System.out.println("═".repeat(55));
    }
}
