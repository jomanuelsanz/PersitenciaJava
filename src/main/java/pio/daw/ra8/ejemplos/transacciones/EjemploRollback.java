package pio.daw.ra8.ejemplos.transacciones;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 6 – Rollback: la BBDD queda intacta tras un error a mitad de transacción.
 * Inserta 3 productos en una misma transacción pero provoca una excepción
 * antes del commit; ningún producto debe haberse guardado.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.transacciones.EjemploRollback"
 */
public class EjemploRollback {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/rollback.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Estado inicial (BBDD vacía) ──────────────────────────────────
            mostrarTotal(em, "Antes de la transacción");

            // ── Transacción que falla a mitad ────────────────────────────────
            System.out.println("\n── Transacción con error a mitad ───────────");
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                System.out.println("  persist → Producto 1 (Teclado)");
                em.persist(new Producto("Teclado mecánico", 79.99, 10));

                System.out.println("  persist → Producto 2 (Ratón)");
                em.persist(new Producto("Ratón inalámbrico", 34.50, 20));

                System.out.println("  persist → Producto 3 (precio negativo = error)");
                Producto malo = new Producto("Artículo roto", -1.0, 5);
                // Simulamos una validación de negocio
                if (malo.getPrecio() < 0) {
                    throw new IllegalArgumentException(
                        "El precio no puede ser negativo: " + malo.getPrecio());
                }
                em.persist(malo);

                tx.commit();
                System.out.println("  commit — no debería llegar aquí");

            } catch (IllegalArgumentException e) {
                if (tx.isActive()) tx.rollback();
                System.out.println("  ✗ Error: " + e.getMessage());
                System.out.println("    → rollback ejecutado.");
            }

            // ── Verificar que la BBDD quedó vacía ────────────────────────────
            mostrarTotal(em, "Después del rollback");

            // ── Transacción correcta para contrastar ─────────────────────────
            System.out.println("\n── Transacción correcta (sin errores) ──────");
            tx = em.getTransaction();
            tx.begin();
            em.persist(new Producto("Teclado mecánico",   79.99, 10));
            em.persist(new Producto("Ratón inalámbrico",  34.50, 20));
            em.persist(new Producto("Monitor 24\"",      219.00,  5));
            tx.commit();
            System.out.println("  commit ejecutado.");
            mostrarTotal(em, "Tras commit exitoso");

            System.out.println("\n✓ Demo de rollback completada.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void mostrarTotal(EntityManager em, String etiqueta) {
        em.clear();
        List<Producto> lista = em.createQuery(
            "SELECT p FROM Producto p", Producto.class
        ).getResultList();
        System.out.printf("  [%s] → %d productos en la BBDD%n", etiqueta, lista.size());
        lista.forEach(p -> System.out.println("    " + p));
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Rollback – integridad ante errores");
        System.out.println("═".repeat(55));
    }
}
