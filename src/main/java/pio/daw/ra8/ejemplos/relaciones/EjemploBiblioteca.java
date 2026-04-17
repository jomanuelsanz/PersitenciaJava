package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 5 – Relación @OneToMany / @ManyToOne con cascada.
 * Un Autor tiene varios Libros. Demuestra persistencia en cascada,
 * navegación de la relación y eliminación en cascada (orphanRemoval).
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.relaciones.EjemploBiblioteca"
 */
public class EjemploBiblioteca {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/biblioteca.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Crear autores con libros (cascada) ───────────────────────────
            System.out.println("── Insertar autores con libros (cascada) ───");
            em.getTransaction().begin();

            Autor cervantes = new Autor("Miguel de Cervantes", "Española");
            cervantes.addLibro(new Libro("Don Quijote I",  "978-84-01", 12.50, 1605));
            cervantes.addLibro(new Libro("Don Quijote II", "978-84-02", 12.50, 1615));
            cervantes.addLibro(new Libro("La Galatea",     "978-84-03",  9.95, 1585));
            em.persist(cervantes);  // los Libros se persisten por CascadeType.ALL

            Autor lorca = new Autor("Federico García Lorca", "Española");
            lorca.addLibro(new Libro("Romancero gitano", "978-84-10", 8.95, 1928));
            lorca.addLibro(new Libro("La casa de Bernarda Alba", "978-84-11", 7.50, 1945));
            em.persist(lorca);

            em.getTransaction().commit();
            System.out.println("  Autores y libros guardados.");

            long idCervantes = cervantes.getId();
            long idLorca     = lorca.getId();

            // ── Consultar libros de un autor ─────────────────────────────────
            System.out.println("\n── Libros de Cervantes ─────────────────────");
            List<Libro> librosCervantes = em.createQuery(
                "SELECT l FROM Libro l WHERE l.autor.id = :idAutor ORDER BY l.anioPublicacion",
                Libro.class
            ).setParameter("idAutor", idCervantes).getResultList();
            librosCervantes.forEach(l -> System.out.println("  " + l));

            // ── Navegar la relación desde el Autor ───────────────────────────
            System.out.println("\n── Libros de Lorca (desde Autor.getLibros()) ");
            Autor autorLorca = em.find(Autor.class, idLorca);
            autorLorca.getLibros().forEach(l ->
                System.out.println("  " + l.getTitulo() + " (" + l.getAnioPublicacion() + ")")
            );

            // ── Contar libros por autor ──────────────────────────────────────
            System.out.println("\n── Libros por autor ────────────────────────");
            em.createQuery(
                "SELECT a.nombre, COUNT(l) FROM Autor a JOIN a.libros l GROUP BY a.nombre",
                Object[].class
            ).getResultList()
             .forEach(r -> System.out.printf("  %-30s %d libros%n", r[0], r[1]));

            // ── Eliminar autor → los libros se eliminan en cascada ───────────
            System.out.println("\n── Eliminar Lorca (cascade + orphanRemoval) ");
            em.getTransaction().begin();
            em.remove(em.find(Autor.class, idLorca));
            em.getTransaction().commit();

            long librosRestantes = em.createQuery(
                "SELECT COUNT(l) FROM Libro l", Long.class
            ).getSingleResult();
            System.out.println("  Libros restantes en BBDD: " + librosRestantes
                               + "  (solo los de Cervantes)");

            System.out.println("\n✓ Relaciones OneToMany/ManyToOne completadas.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Biblioteca – Autor / Libro");
        System.out.println("═".repeat(55));
    }
}
