package pio.daw.ra8.ejemplos.transacciones;

import jakarta.persistence.*;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;

/**
 * BLOQUE 6 – Transacciones: transferencia de stock entre productos.
 * Escenario 1: transferencia exitosa (ambos cambios se confirman juntos).
 * Escenario 2: stock insuficiente → excepción → rollback automático.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.transacciones.EjemploTransferencia"
 */
public class EjemploTransferencia {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/transferencia.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Preparar datos ───────────────────────────────────────────────
            em.getTransaction().begin();
            Producto almacenA = new Producto("Teclado (Almacén A)", 79.99, 20);
            Producto almacenB = new Producto("Teclado (Almacén B)", 79.99,  5);
            em.persist(almacenA);
            em.persist(almacenB);
            em.getTransaction().commit();

            long idA = almacenA.getId();
            long idB = almacenB.getId();
            mostrarStock(em, idA, idB, "Estado inicial");

            // ── Escenario 1: transferencia exitosa ───────────────────────────
            System.out.println("\n── Escenario 1: transferir 8 unidades A→B ──");
            transferir(em, idA, idB, 8);
            mostrarStock(em, idA, idB, "Tras transferencia exitosa");

            // ── Escenario 2: stock insuficiente → rollback ───────────────────
            System.out.println("\n── Escenario 2: transferir 20 unidades A→B ─");
            System.out.println("  (A solo tiene " + em.find(Producto.class, idA).getStock()
                               + " unidades → debe fallar)");
            transferir(em, idA, idB, 20);
            mostrarStock(em, idA, idB, "Tras intento fallido (BBDD sin cambios)");

            System.out.println("\n✓ Demo de transacciones completada.");

        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * Transfiere {@code cantidad} unidades del producto {@code idOrigen} al {@code idDestino}.
     * Si no hay suficiente stock, lanza una excepción y hace rollback.
     */
    private static void transferir(EntityManager em, long idOrigen, long idDestino, int cantidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Producto origen  = em.find(Producto.class, idOrigen);
            Producto destino = em.find(Producto.class, idDestino);

            if (origen.getStock() < cantidad) {
                throw new IllegalStateException(
                    "Stock insuficiente: se piden " + cantidad
                    + " pero solo hay " + origen.getStock());
            }

            origen.setStock(origen.getStock()   - cantidad);
            destino.setStock(destino.getStock() + cantidad);

            tx.commit();
            System.out.println("  ✓ Transferencia de " + cantidad + " unidades confirmada.");

        } catch (IllegalStateException e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("  ✗ Transferencia cancelada: " + e.getMessage());
            System.out.println("    → rollback ejecutado. La BBDD no ha cambiado.");
        }
    }

    private static void mostrarStock(EntityManager em, long idA, long idB, String titulo) {
        em.clear(); // refrescar desde la BBDD
        System.out.println("  [" + titulo + "]");
        System.out.println("    Almacén A: stock=" + em.find(Producto.class, idA).getStock());
        System.out.println("    Almacén B: stock=" + em.find(Producto.class, idB).getStock());
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Transacciones – transferencia de stock");
        System.out.println("═".repeat(55));
    }
}
