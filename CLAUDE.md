# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Purpose

Educational repository for the DAW (Desarrollo de Aplicaciones Web) Java programming course, covering RA8 (object-oriented databases) and RA9 (relational database management via JDBC). The goal is to teach Java database connectivity and persistence techniques.

## Build & Run Commands

```bash
# Compile
mvn compile

# Run an example
mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploCRUDProducto"

# Run all tests
mvn test

# Clean build artifacts
mvn clean
```

Replace the class name with any fully-qualified class that has a `main` method. All RA8 examples follow the pattern `pio.daw.ra8.ejemplos.<bloque>.<NombreEjemplo>`.

## Project Structure

```
src/main/java/pio/daw/
├── App.java                        # Placeholder, unused
└── ra8/
    ├── modelo/                     # Shared JPA entities: Categoria, Producto
    ├── util/JPAUtil.java            # Creates EntityManagerFactory; deletes .odb on startup
    └── ejemplos/
        ├── serializacion/          # Block 1: native Java serialization
        ├── jpa/                    # Block 2: basic CRUD with JPA + ObjectDB
        ├── consultas/              # Block 3: JPQL queries
        ├── relaciones/             # Block 4: inheritance and entity relationships
        └── transacciones/          # Block 5: transactions and rollback
src/main/resources/META-INF/
└── persistence.xml                 # ObjectDB persistence unit "ra8DB"
docs/
└── ra8-bbdd-orientadas-objetos.html  # HTML teaching material for RA8
```

## Key Technical Details

- **Java 25** (OpenJDK), Maven build system; `pom.xml` targets `maven.compiler.release=17`
- **JUnit Jupiter 5.11** for testing (junit-jupiter-api + junit-jupiter-params)
- **JPA (Jakarta Persistence 3.1)** + **ObjectDB 2.9.5** for RA8; ObjectDB repo at `https://m2.objectdb.com`
- **H2 Database** (planned) for RA9 via plain JDBC

### ObjectDB quirks — read before touching persistence.xml or JVM flags

1. **`persistence.xml` must use a bare `<persistence>` tag with no `xmlns` attribute.** ObjectDB internally rewrites the tag to add its own JPA 2.0 namespace; if you supply a `jakarta.ee` or `jcp.org` namespace, schema validation fails with `SAXParseException: cvc-elt.1.a`.

2. **Java 21+ requires `-Djavax.xml.accessExternalSchema=all`** so that ObjectDB can load its internal XSD schemas through JAXP. This flag is already set in the `exec-maven-plugin` configuration in `pom.xml`; make sure it stays there if the plugin config is ever modified.

3. **Use `jakarta.persistence.jdbc.url`** (not `javax.persistence.jdbc.url`) as the property key for the database path, both in `persistence.xml` and in `JPAUtil.crearEMF()`.

## Learning Objectives (RA8 & RA9)

Code added to this repo should demonstrate:
- **RA8**: Object-oriented database access (storing/retrieving/updating/deleting Java objects)
- **RA9**: Relational database management — JDBC connections, CRUD operations, data integrity
