package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Categoria;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 5 – Modelo completo de la tienda: Categoria → Producto.
 * Demuestra relaciones bidireccionales, consultas que navegan joins
 * y la coherencia al eliminar productos de una categoría.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.relaciones.EjemploTiendaCategorias"
 */
public class EjemploTiendaCategorias {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/tienda.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Crear categorías con productos ───────────────────────────────
            System.out.println("── Crear catálogo por categorías ───────────");
            em.getTransaction().begin();

            Categoria perifericos = new Categoria("Periféricos");
            perifericos.addProducto(new Producto("Teclado mecánico",   79.99, 20));
            perifericos.addProducto(new Producto("Ratón inalámbrico",  34.50, 45));
            perifericos.addProducto(new Producto("Auriculares gaming", 59.95, 25));

            Categoria monitores = new Categoria("Monitores");
            monitores.addProducto(new Producto("Monitor 24\" FHD", 219.00, 10));
            monitores.addProducto(new Producto("Monitor 27\" QHD", 349.00,  6));

            Categoria redes = new Categoria("Redes");
            redes.addProducto(new Producto("Router WiFi 6",    89.99, 15));
            redes.addProducto(new Producto("Switch 8 puertos", 29.99, 20));

            em.persist(perifericos);  // CascadeType.ALL persiste productos
            em.persist(monitores);
            em.persist(redes);
            em.getTransaction().commit();

            // ── Mostrar catálogo completo ────────────────────────────────────
            System.out.println("\n── Catálogo completo por categoría ─────────");
            em.createQuery("SELECT c FROM Categoria c ORDER BY c.nombre", Categoria.class)
              .getResultList()
              .forEach(cat -> {
                  System.out.println("  [" + cat.getNombre() + "]");
                  cat.getProductos().forEach(p ->
                      System.out.printf("    %-25s %.2f €%n", p.getNombre(), p.getPrecio())
                  );
              });

            // ── Productos de una categoría con JPQL (JOIN) ───────────────────
            System.out.println("\n── Productos de 'Periféricos' (JPQL JOIN) ──");
            em.createQuery(
                "SELECT p FROM Producto p WHERE p.categoria.nombre = :cat ORDER BY p.precio",
                Producto.class
            ).setParameter("cat", "Periféricos")
             .getResultList()
             .forEach(p -> System.out.printf("  %-25s %.2f €%n",
                 p.getNombre(), p.getPrecio()));

            // ── Número de productos por categoría ────────────────────────────
            System.out.println("\n── Productos por categoría ─────────────────");
            em.createQuery(
                "SELECT c.nombre, COUNT(p) FROM Categoria c JOIN c.productos p GROUP BY c.nombre",
                Object[].class
            ).getResultList()
             .forEach(r -> System.out.printf("  %-15s %d productos%n", r[0], r[1]));

            // ── Precio más alto de cada categoría ────────────────────────────
            System.out.println("\n── Precio máximo por categoría ─────────────");
            em.createQuery(
                "SELECT c.nombre, MAX(p.precio) FROM Categoria c JOIN c.productos p GROUP BY c.nombre ORDER BY MAX(p.precio) DESC",
                Object[].class
            ).getResultList()
             .forEach(r -> System.out.printf("  %-15s %.2f €%n", r[0], r[1]));

            System.out.println("\n✓ Relaciones de tienda completadas.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Tienda – Categoria / Producto");
        System.out.println("═".repeat(55));
    }
}
