package pio.daw.ra8.ejemplos.consultas;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 4 – Parámetros nombrados en JPQL.
 * Muestra el uso de :parametro para evitar concatenaciones peligrosas,
 * LIKE, rangos y getSingleResult vs getResultList.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.consultas.EjemploParametros"
 */
public class EjemploParametros {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/parametros.odb");
        EntityManager em = emf.createEntityManager();

        try {
            cargarDatos(em);

            // ── Filtrar por precio máximo ────────────────────────────────────
            double maxPrecio = 60.0;
            System.out.println("── Productos con precio ≤ " + maxPrecio + " € ──────────");
            List<Producto> baratos = em.createQuery(
                "SELECT p FROM Producto p WHERE p.precio <= :max ORDER BY p.precio",
                Producto.class
            ).setParameter("max", maxPrecio).getResultList();
            baratos.forEach(p -> System.out.printf("  %-25s %.2f €%n",
                p.getNombre(), p.getPrecio()));

            // ── Buscar por nombre (LIKE) ─────────────────────────────────────
            String patron = "%cánico%";
            System.out.println("\n── LIKE '" + patron + "' ──────────────────────────");
            em.createQuery(
                "SELECT p FROM Producto p WHERE p.nombre LIKE :patron",
                Producto.class
            ).setParameter("patron", patron)
             .getResultList()
             .forEach(p -> System.out.println("  " + p));

            // ── Rango de precios ─────────────────────────────────────────────
            System.out.println("\n── Rango de precio: 30 € – 100 € ──────────");
            em.createQuery(
                "SELECT p FROM Producto p WHERE p.precio BETWEEN :min AND :max ORDER BY p.precio",
                Producto.class
            ).setParameter("min", 30.0).setParameter("max", 100.0)
             .getResultList()
             .forEach(p -> System.out.printf("  %-25s %.2f €%n",
                 p.getNombre(), p.getPrecio()));

            // ── getSingleResult ──────────────────────────────────────────────
            System.out.println("\n── Producto más barato ─────────────────────");
            Producto masBarato = em.createQuery(
                "SELECT p FROM Producto p ORDER BY p.precio ASC",
                Producto.class
            ).setMaxResults(1).getSingleResult();
            System.out.println("  " + masBarato);

            System.out.println("\n✓ Consultas con parámetros completadas.");

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
        System.out.println("  EJEMPLO: Parámetros nombrados en JPQL");
        System.out.println("═".repeat(55));
    }
}
