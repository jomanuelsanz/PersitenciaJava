package pio.daw.ra8.ejemplos.consultas;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 4 – Paginación con setFirstResult / setMaxResults.
 * Útil para mostrar listas largas divididas en páginas.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.consultas.EjemploPaginacion"
 */
public class EjemploPaginacion {

    private static final int PAGINA_SIZE = 3;

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/paginacion.odb");
        EntityManager em = emf.createEntityManager();

        try {
            cargarDatos(em);

            long total = em.createQuery(
                "SELECT COUNT(p) FROM Producto p", Long.class
            ).getSingleResult();

            int totalPaginas = (int) Math.ceil((double) total / PAGINA_SIZE);
            System.out.printf("Total: %d productos  |  %d por página  |  %d páginas%n%n",
                total, PAGINA_SIZE, totalPaginas);

            // ── Iterar por páginas ───────────────────────────────────────────
            for (int pagina = 0; pagina < totalPaginas; pagina++) {
                System.out.println("── Página " + (pagina + 1) + " de " + totalPaginas
                                   + " ─────────────────────────");

                List<Producto> productos = em.createQuery(
                    "SELECT p FROM Producto p ORDER BY p.nombre",
                    Producto.class
                ).setFirstResult(pagina * PAGINA_SIZE)
                 .setMaxResults(PAGINA_SIZE)
                 .getResultList();

                productos.forEach(p -> System.out.printf("  %-25s %.2f €%n",
                    p.getNombre(), p.getPrecio()));
            }

            System.out.println("\n✓ Paginación completada.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void cargarDatos(EntityManager em) {
        em.getTransaction().begin();
        String[][] datos = {
            {"Auriculares gaming", "59.95"}, {"Disco SSD 1TB",    "89.00"},
            {"Hub USB-C",          "19.99"}, {"Monitor 24\"",    "219.00"},
            {"Micrófono USB",      "49.99"}, {"Ratón inalámbrico","34.50"},
            {"Switch 8 puertos",   "29.99"}, {"Teclado mecánico", "79.99"},
            {"Webcam HD",          "44.99"}, {"Alfombrilla XL",   "14.99"},
        };
        for (String[] d : datos) {
            em.persist(new Producto(d[0], Double.parseDouble(d[1]), 10));
        }
        em.getTransaction().commit();
        System.out.println("── Datos cargados ──────────────────────────\n");
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Paginación con JPQL");
        System.out.println("═".repeat(55));
    }
}
