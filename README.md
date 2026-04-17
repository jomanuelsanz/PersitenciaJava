# Bases de datos en Java — DAW · Programación

Repositorio de ejemplos y ejercicios para los **Resultados de Aprendizaje 8 y 9** de la asignatura de Programación en Java del ciclo formativo de Desarrollo de Aplicaciones Web (DAW).

---

## ¿Qué contiene este repositorio?

| Resultado de Aprendizaje | Tema | Estado |
|---|---|---|
| [RA8](README_RA8.md) | Bases de datos **orientadas a objetos** (Serialización Java + JPA/ObjectDB) | ✅ Implementado |
| [RA9](README_RA9.md) | Bases de datos **relacionales** con JDBC | 🔜 En desarrollo |

---

## Requisitos previos

- **Java 17+** (probado con OpenJDK 17 y 25)
- **Maven 3.6+**
- Conexión a Internet la primera vez (para descargar dependencias de Maven)

## Arranque rápido

```bash
# Clonar y compilar
git clone <url-del-repo>
cd javabbdd
mvn compile

# Ejecutar cualquier ejemplo de RA8
mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploCRUDProducto"
```

---

## Estructura del proyecto

```
src/main/java/pio/daw/
├── App.java                        # Punto de entrada vacío (placeholder)
└── ra8/
    ├── modelo/                     # Entidades JPA compartidas (Categoria, Producto)
    ├── util/JPAUtil.java            # Utilidad para crear EntityManagerFactory
    └── ejemplos/
        ├── serializacion/          # Bloque 1: Serialización nativa Java
        ├── jpa/                    # Bloque 2: CRUD básico con JPA + ObjectDB
        ├── consultas/              # Bloque 3: Consultas JPQL
        ├── relaciones/             # Bloque 4: Herencia y relaciones entre entidades
        └── transacciones/          # Bloque 5: Transacciones y rollback
```

---

## Documentación detallada

- 📘 [README_RA8.md](README_RA8.md) — Bases de datos orientadas a objetos: serialización, JPA, ObjectDB, ejercicios.
- 📗 [README_RA9.md](README_RA9.md) — Gestión de BBDD relacionales con JDBC: conexiones, CRUD, transacciones, ejercicios.
