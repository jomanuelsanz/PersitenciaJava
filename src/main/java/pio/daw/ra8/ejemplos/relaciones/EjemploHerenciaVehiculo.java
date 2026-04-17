package pio.daw.ra8.ejemplos.relaciones;

import jakarta.persistence.*;
import pio.daw.ra8.util.JPAUtil;
import java.util.List;

/**
 * BLOQUE 5 – Herencia entre entidades JPA.
 * Almacena Coches y Motos (subclases de Vehiculo) y demuestra que se puede
 * consultar por la superclase (todos los vehículos) o por subtipo concreto.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.relaciones.EjemploHerenciaVehiculo"
 */
public class EjemploHerenciaVehiculo {

    public static void main(String[] args) {
        banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/vehiculos.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Persistir vehículos de distintos tipos ───────────────────────
            em.getTransaction().begin();
            em.persist(new Coche("1234 ABC", 2020, "Toyota",  4));
            em.persist(new Coche("5678 DEF", 2018, "Seat",    5));
            em.persist(new Coche("9999 ZZZ", 2022, "BMW",     2));
            em.persist(new Moto ("1111 MMM", 2021, "Honda",   false));
            em.persist(new Moto ("2222 NNN", 2019, "Yamaha",  true));
            em.getTransaction().commit();

            // ── Consultar todos los vehículos (polimorfismo) ─────────────────
            System.out.println("── Todos los vehículos (Coche + Moto) ─────");
            List<Vehiculo> todos = em.createQuery(
                "SELECT v FROM Vehiculo v ORDER BY v.anio DESC",
                Vehiculo.class
            ).getResultList();
            todos.forEach(v -> System.out.println("  " + v));

            // ── Consultar solo coches ────────────────────────────────────────
            System.out.println("\n── Solo Coches ─────────────────────────────");
            em.createQuery("SELECT c FROM Coche c", Coche.class)
              .getResultList()
              .forEach(c -> System.out.println("  " + c));

            // ── Consultar solo motos ─────────────────────────────────────────
            System.out.println("\n── Solo Motos ──────────────────────────────");
            em.createQuery("SELECT m FROM Moto m", Moto.class)
              .getResultList()
              .forEach(m -> System.out.println("  " + m));

            // ── instanceof al recuperar ──────────────────────────────────────
            System.out.println("\n── Tipos de cada vehículo (instanceof) ─────");
            todos.forEach(v -> System.out.println(
                "  " + v.getMatricula() + " → " + v.getClass().getSimpleName()
            ));

            System.out.println("\n✓ Herencia de entidades completada.");

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Herencia JPA – Vehiculo/Coche/Moto");
        System.out.println("═".repeat(55));
    }
}
