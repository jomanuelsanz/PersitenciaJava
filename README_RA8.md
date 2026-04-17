# RA8 — Bases de datos orientadas a objetos en Java

> **RA8.** Utiliza bases de datos orientadas a objetos, analizando sus características y aplicando técnicas para mantener la persistencia de la información.

---

## Criterios de evaluación

| | Criterio |
|---|---|
| a | Identificar las características de las BBDD orientadas a objetos |
| b | Analizar su aplicación en el desarrollo con lenguajes OO |
| c | Instalar sistemas gestores de BBDD orientados a objetos |
| d | Clasificar y analizar los métodos soportados para la gestión de la información |
| e | Crear bases de datos y las estructuras para el almacenamiento de objetos |
| f | Programar aplicaciones que almacenen objetos |
| g | Recuperar, actualizar y eliminar objetos de las bases de datos |
| h | Almacenar y gestionar tipos estructurados, compuestos y relacionados |

---

## Tecnologías utilizadas

| Tecnología | Versión | Propósito |
|---|---|---|
| Java SE | 17+ | Lenguaje |
| Jakarta Persistence API (JPA) | 3.1 | Especificación de persistencia |
| ObjectDB | 2.9.5 | Sistema gestor de BBDD orientado a objetos |
| Maven | 3.6+ | Gestión de dependencias y build |

### Dependencias en `pom.xml`

```xml
<dependency>
  <groupId>jakarta.persistence</groupId>
  <artifactId>jakarta.persistence-api</artifactId>
  <version>3.1.0</version>
</dependency>
<dependency>
  <groupId>com.objectdb</groupId>
  <artifactId>objectdb-jk</artifactId>
  <version>2.9.5</version>
</dependency>
```

El repositorio de ObjectDB debe declararse también:

```xml
<repository>
  <id>objectdb</id>
  <url>https://m2.objectdb.com</url>
</repository>
```

---

## Cómo ejecutar los ejemplos

Todos los ejemplos se lanzan con:

```bash
mvn -q exec:java -Dexec.mainClass="<nombre.completo.de.la.Clase>"
```

Cada ejecución crea su propia base de datos en `target/` y la borra al inicio para garantizar un estado limpio.

---

## Bloque 1 — Serialización nativa Java

> Paquete: `pio.daw.ra8.ejemplos.serializacion`
> Criterios: **a, b, d, e, f, g**

Introducción a la persistencia de objetos sin BBDD externa. Sirve como puente conceptual antes de usar un OODB.

### Clases de modelo

| Clase | Descripción |
|---|---|
| `ProductoSerial` | POJO que implementa `Serializable` |
| `AlumnoSerial` | POJO con campo `transient` para demostrar exclusión de campos |

### Ejemplos

| Clase | Qué demuestra | Comando |
|---|---|---|
| `EjemploSerializarProducto` | Guardar y recuperar un objeto en disco | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploSerializarProducto"` |
| `EjemploSerializarLista` | Serializar y deserializar un `List<Alumno>` completo | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploSerializarLista"` |
| `EjemploTransient` | Campos `transient` no se persisten (contraseñas, conexiones…) | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploTransient"` |
| `EjemploVersionado` | `serialVersionUID` y qué ocurre cuando no coincide (`InvalidClassException`) | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploVersionado"` |

### Ejercicios

**Ejercicio 1.1** — Serializar una biblioteca  
Crea la clase `LibroSerial` (título, autor, ISBN, precio, año). Escribe un programa que guarde una lista de libros en `biblioteca.dat` y otro que la recupere y la muestre ordenada por título.

**Ejercicio 1.2** — `transient` en la práctica  
Añade a `LibroSerial` un campo `transient int vecesConsultado`. Verifica que tras serializar y deserializar ese contador vuelve a 0.

**Ejercicio 1.3** — Compatibilidad de versiones  
Añade un nuevo campo `String genero` a `LibroSerial`. Comprueba la diferencia de comportamiento con y sin `serialVersionUID` declarado explícitamente.

---

## Bloque 2 — JPA + ObjectDB: CRUD básico

> Paquete: `pio.daw.ra8.ejemplos.jpa`
> Criterios: **c, d, e, f, g**

Primer contacto con un OODB real. ObjectDB implementa JPA sin mapeo a tablas: los objetos se persisten directamente.

### Entidades del modelo compartido

| Clase | Paquete | Descripción |
|---|---|---|
| `Producto` | `pio.daw.ra8.modelo` | Entidad base usada en la mayoría de ejemplos |
| `Categoria` | `pio.daw.ra8.modelo` | Relación `@OneToMany` con `Producto` |
| `Contacto` | `pio.daw.ra8.ejemplos.jpa` | Entidad exclusiva del gestor de contactos |

### Anotaciones JPA introducidas

| Anotación | Uso |
|---|---|
| `@Entity` | Marca la clase como persistente |
| `@Id` | Campo identificador único |
| `@GeneratedValue` | ObjectDB genera el ID automáticamente |

### Patrón de uso del `EntityManager`

```java
EntityManagerFactory emf = JPAUtil.crearEMF("target/miEjemplo.odb");
EntityManager em = emf.createEntityManager();
try {
    em.getTransaction().begin();
    em.persist(new Producto("Teclado", 79.99, 10));
    em.getTransaction().commit();
} finally {
    em.close();
    emf.close();
}
```

### Ejemplos

| Clase | Qué demuestra | Comando |
|---|---|---|
| `EjemploCRUDProducto` | `persist`, `find`, actualización, `remove` | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploCRUDProducto"` |
| `EjemploGestorContactos` | CRUD completo sobre otra entidad (`Contacto`) | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploGestorContactos"` |
| `EjemploInventario` | `UPDATE` masivo con JPQL, `AVG`, filtros de stock | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.jpa.EjemploInventario"` |

### Ejercicios

**Ejercicio 2.1** — Gestión de películas  
Crea la entidad `Pelicula` (título, director, año, duración en minutos, puntuación 0–10). Implementa un programa que: añada 5 películas, busque una por ID, cambie su puntuación y la elimine.

**Ejercicio 2.2** — Validación antes de persistir  
Añade validación manual en el código de negocio: si el stock de un `Producto` es negativo o el precio es 0, lanza una `IllegalArgumentException` antes de llamar a `persist`.

**Ejercicio 2.3** — Subida masiva de precios  
Dado un catálogo de 10 productos, sube un 15% el precio de todos los que tengan stock menor a 20 usando JPQL `UPDATE`.

---

## Bloque 3 — Consultas JPQL

> Paquete: `pio.daw.ra8.ejemplos.consultas`
> Criterios: **d, g**

JPQL (Jakarta Persistence Query Language) opera sobre clases y atributos Java, no sobre tablas SQL.

### Sintaxis básica

```java
// Consulta tipada con parámetro nombrado
List<Producto> resultado = em.createQuery(
    "SELECT p FROM Producto p WHERE p.precio < :max ORDER BY p.precio",
    Producto.class
).setParameter("max", 100.0).getResultList();
```

> ⚠️ Usar siempre **parámetros nombrados** (`:nombre`) en lugar de concatenar cadenas.

### Ejemplos

| Clase | Qué demuestra | Comando |
|---|---|---|
| `EjemploConsultasBasicas` | `SELECT`, `ORDER BY`, `COUNT`, `MAX`, `MIN`, `AVG` | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.consultas.EjemploConsultasBasicas"` |
| `EjemploParametros` | Parámetros nombrados, `LIKE`, `BETWEEN`, `getSingleResult` | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.consultas.EjemploParametros"` |
| `EjemploPaginacion` | `setFirstResult` + `setMaxResults` para listar por páginas | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.consultas.EjemploPaginacion"` |

### Ejercicios

**Ejercicio 3.1** — Consultas sobre películas  
Sobre la entidad `Pelicula` del ejercicio 2.1:
- Listar todas ordenadas por puntuación descendente.
- Buscar películas de un director concreto (parámetro).
- Contar cuántas películas tienen duración superior a 2 horas.
- Mostrar la película mejor valorada.

**Ejercicio 3.2** — Estadísticas de inventario  
Sobre `Producto`: muestra el nombre, precio y stock de los 3 productos más caros. Muestra también cuántos productos tienen stock agotado (stock = 0).

**Ejercicio 3.3** — Paginación de resultados  
Lista todos los productos de 4 en 4, mostrando el número de página y el total de páginas.

---

## Bloque 4 — Tipos complejos: herencia y relaciones

> Paquete: `pio.daw.ra8.ejemplos.relaciones`
> Criterios: **b, e, f, g, h**

### Entidades del bloque

| Jerarquía | Clases |
|---|---|
| Vehículos | `Vehiculo` (abstracta) → `Coche`, `Moto` |
| Biblioteca | `Autor` `@OneToMany` → `Libro` `@ManyToOne` |
| Tienda | `Categoria` `@OneToMany` → `Producto` `@ManyToOne` |

### Anotaciones JPA introducidas

| Anotación | Uso |
|---|---|
| `@Inheritance(strategy = SINGLE_TABLE)` | Jerarquía en una única tabla lógica |
| `@OneToMany(cascade = ALL, orphanRemoval = true)` | Persistencia y eliminación en cascada |
| `@ManyToOne` | Lado propietario de la relación |

### Ejemplos

| Clase | Qué demuestra | Comando |
|---|---|---|
| `EjemploHerenciaVehiculo` | Consultar por superclase y por subtipo, `instanceof` | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.relaciones.EjemploHerenciaVehiculo"` |
| `EjemploBiblioteca` | Cascada al persistir/eliminar, `GROUP BY` con JOIN | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.relaciones.EjemploBiblioteca"` |
| `EjemploTiendaCategorias` | Modelo completo tienda, navegación bidireccional, agregados por categoría | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.relaciones.EjemploTiendaCategorias"` |

### Ejercicios

**Ejercicio 4.1** — Jerarquía de empleados  
Crea `Empleado` (abstracta: nombre, salario) con subclases `Programador` (lenguaje principal) y `Gerente` (número de personas a cargo). Guarda 5 empleados de distintos tipos y lista todos ordenados por salario.

**Ejercicio 4.2** — Tienda con pedidos  
Añade las entidades `Cliente` y `Pedido`. Un `Cliente` puede tener múltiples `Pedido`s, cada uno con una fecha y una lista de `Producto`s. Implementa: crear un pedido, listar los pedidos de un cliente y calcular el importe total de cada pedido.

**Ejercicio 4.3** — Integridad referencial  
Verifica que al eliminar una `Categoria` con `orphanRemoval = true`, sus `Producto`s también desaparecen. Luego elimina solo un producto de la categoría sin eliminar la categoría.

---

## Bloque 5 — Transacciones e integridad

> Paquete: `pio.daw.ra8.ejemplos.transacciones`
> Criterios: **f, g**

### Patrón transaccional recomendado

```java
EntityTransaction tx = em.getTransaction();
try {
    tx.begin();
    // operaciones...
    tx.commit();
} catch (Exception e) {
    if (tx.isActive()) tx.rollback();
    // gestión del error
}
```

### Ejemplos

| Clase | Qué demuestra | Comando |
|---|---|---|
| `EjemploTransferencia` | Éxito y fallo de una transferencia de stock; rollback automático | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.transacciones.EjemploTransferencia"` |
| `EjemploRollback` | La BBDD queda intacta cuando la transacción falla a mitad | `mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.transacciones.EjemploRollback"` |

### Ejercicios

**Ejercicio 5.1** — Sistema de préstamos de biblioteca  
Implementa el préstamo de un libro: crea el objeto `Prestamo` (libro, alumno, fecha) y marca el libro como no disponible dentro de la misma transacción. Si el alumno ya tiene 3 préstamos activos, cancela con rollback y lanza una excepción descriptiva.

**Ejercicio 5.2** — Cesta de la compra  
Al confirmar un pedido: descuenta el stock de cada producto pedido. Si cualquier producto no tiene stock suficiente, cancela todo el pedido con rollback y muestra qué producto falló.

---

## Recursos oficiales

| Recurso | URL |
|---|---|
| API `java.io.Serializable` (Java 17) | https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/Serializable.html |
| Java Object Serialization Specification | https://docs.oracle.com/en/java/javase/17/docs/specs/serialization/index.html |
| ObjectDB — Manual JPA | https://www.objectdb.com/java/jpa |
| ObjectDB — Getting Started (Maven) | https://www.objectdb.com/tutorial/jpa/start/maven |
| ObjectDB — Guía de consultas JPQL | https://www.objectdb.com/java/jpa/query |
| Jakarta Persistence — Tutorial oficial | https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html |
| Jakarta Persistence — API EntityManager | https://jakarta.ee/specifications/persistence/3.2/apidocs/jakarta.persistence/jakarta/persistence/entitymanager |
