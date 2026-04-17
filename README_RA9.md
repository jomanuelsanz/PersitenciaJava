# RA9 — Gestión de bases de datos relacionales con JDBC

> **RA9.** Gestiona información almacenada en bases de datos manteniendo la integridad y consistencia de los datos.

---

## Criterios de evaluación

| | Criterio |
|---|---|
| a | Identificar las características y métodos de acceso a sistemas gestores de BBDD |
| b | Programar conexiones con bases de datos |
| c | Escribir código para almacenar información en bases de datos |
| d | Crear programas para recuperar y mostrar información almacenada |
| e | Efectuar borrados y modificaciones sobre la información almacenada |
| f | Crear aplicaciones que muestren la información almacenada |
| g | Crear aplicaciones para gestionar la información presente en bases de datos |

---

## Tecnologías utilizadas

| Tecnología | Versión | Propósito |
|---|---|---|
| Java SE | 17+ | Lenguaje |
| JDBC | API estándar Java | Acceso a BBDD relacionales |
| H2 Database | 2.x | BBDD relacional embebida (sin instalación) |
| Maven | 3.6+ | Gestión de dependencias y build |

### Dependencias en `pom.xml`

```xml
<!-- H2: base de datos relacional embebida, ideal para desarrollo y pruebas -->
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <version>2.3.232</version>
</dependency>
```

> H2 no requiere instalar ningún servidor. La BBDD se crea automáticamente en un fichero local o en memoria.

---

## Cómo ejecutar los ejemplos

```bash
# Compilar
mvn compile

# Ejecutar un ejemplo concreto
mvn -q exec:java -Dexec.mainClass="pio.daw.ra9.ejemplos.<NombreEjemplo>"
```

---

## Contenidos y ejemplos planificados

### Bloque 1 — Conexión y primeros pasos con JDBC

> Paquete: `pio.daw.ra9.ejemplos.conexion`
> Criterios: **a, b**

Conceptos clave: `DriverManager`, `Connection`, `Statement`, `ResultSet`.

```java
// Patrón básico de conexión con H2
Connection conn = DriverManager.getConnection(
    "jdbc:h2:./target/tienda", "sa", ""
);
```

| Ejemplo (por implementar) | Qué demostrará |
|---|---|
| `EjemploConexion` | Abrir y cerrar una conexión JDBC; comprobar metadatos |
| `EjemploCrearTabla` | Ejecutar DDL (`CREATE TABLE`) con `Statement` |

---

### Bloque 2 — CRUD con JDBC

> Paquete: `pio.daw.ra9.ejemplos.crud`
> Criterios: **b, c, d, e**

Uso de `PreparedStatement` para INSERT, SELECT, UPDATE y DELETE de forma segura (sin inyección SQL).

```java
// Ejemplo de INSERT seguro con PreparedStatement
PreparedStatement ps = conn.prepareStatement(
    "INSERT INTO producto (nombre, precio, stock) VALUES (?, ?, ?)"
);
ps.setString(1, "Teclado");
ps.setDouble(2, 79.99);
ps.setInt(3, 10);
ps.executeUpdate();
```

| Ejemplo (por implementar) | Qué demostrará |
|---|---|
| `EjemploCRUDProducto` | `INSERT`, `SELECT`, `UPDATE`, `DELETE` con `PreparedStatement` |
| `EjemploGestorAlumnos` | CRUD completo sobre tabla `alumno` |
| `EjemploConsultas` | `SELECT` con `WHERE`, `ORDER BY`, `LIMIT`, funciones de agregado |

---

### Bloque 3 — Relaciones entre tablas

> Paquete: `pio.daw.ra9.ejemplos.relaciones`
> Criterios: **d, f, g**

Claves foráneas, `JOIN` y navegación de relaciones directamente en SQL.

```sql
SELECT p.nombre, c.nombre AS categoria
FROM producto p
JOIN categoria c ON p.categoria_id = c.id
WHERE c.nombre = ?
```

| Ejemplo (por implementar) | Qué demostrará |
|---|---|
| `EjemploTiendaJOIN` | `INNER JOIN` entre `Categoria` y `Producto` |
| `EjemploPedidos` | Modelo completo con `cliente`, `pedido` y `linea_pedido` |

---

### Bloque 4 — Transacciones JDBC

> Paquete: `pio.daw.ra9.ejemplos.transacciones`
> Criterios: **g**

Control manual de transacciones con `conn.setAutoCommit(false)`, `commit()` y `rollback()`.

```java
conn.setAutoCommit(false);
try {
    // varias operaciones...
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
}
```

| Ejemplo (por implementar) | Qué demostrará |
|---|---|
| `EjemploTransferencia` | Transferencia de saldo entre cuentas; rollback si falla |
| `EjemploRollback` | Interrupción a mitad de transacción; BBDD sin cambios |

---

## Ejercicios

### Bloque 1 — Conexión

**Ejercicio 1.1** — Primera conexión  
Abre una conexión H2 al fichero `target/ejercicios.db`, crea la tabla `alumno (id INT PRIMARY KEY AUTO_INCREMENT, nombre VARCHAR(100), nota DOUBLE, curso VARCHAR(20))` y cierra la conexión. Verifica que el fichero se ha creado.

**Ejercicio 1.2** — Metadatos de la BBDD  
Usando `conn.getMetaData()`, imprime: nombre del producto (base de datos), versión del driver JDBC y todas las tablas de tipo `TABLE` existentes.

---

### Bloque 2 — CRUD

**Ejercicio 2.1** — Gestión de alumnos  
Implementa un programa que:
1. Inserte 5 alumnos con `PreparedStatement`.
2. Liste todos los alumnos ordenados por nota descendente.
3. Actualice la nota de un alumno buscado por nombre.
4. Elimine a los alumnos con nota inferior a 5.

**Ejercicio 2.2** — Sin inyección SQL  
Demuestra la diferencia entre una consulta vulnerable (concatenación de cadenas) y una segura (`PreparedStatement`) intentando inyectar `' OR '1'='1` como nombre.

**Ejercicio 2.3** — Importar datos desde CSV  
Lee el fichero `alumnos.csv` línea a línea e inserta cada alumno en la BBDD dentro de una única transacción. Si alguna línea tiene formato incorrecto, cancela toda la importación con rollback.

---

### Bloque 3 — Relaciones

**Ejercicio 3.1** — Tienda con categorías  
Crea las tablas `categoria` y `producto` con clave foránea. Inserta categorías y productos. Lista todos los productos con el nombre de su categoría usando `JOIN`.

**Ejercicio 3.2** — Pedidos  
Crea las tablas `cliente`, `pedido` y `linea_pedido`. Implementa: registrar un pedido completo (cabecera + líneas), listar los pedidos de un cliente y calcular el total de cada pedido.

**Ejercicio 3.3** — Informe de ventas  
Usando `GROUP BY` y funciones de agregado, genera un informe que muestre: total de pedidos por cliente, importe medio por pedido y el producto más vendido.

---

### Bloque 4 — Transacciones

**Ejercicio 4.1** — Transferencia bancaria  
Crea la tabla `cuenta (id, titular, saldo)`. Implementa la transferencia de importe entre dos cuentas: descuenta el saldo del origen y lo suma al destino en la misma transacción. Si el saldo del origen es insuficiente, haz rollback.

**Ejercicio 4.2** — Cesta de la compra  
Al confirmar un pedido, descuenta el stock de cada producto en la misma transacción. Si cualquier producto no tiene stock suficiente, cancela todo el pedido con rollback y muestra cuál fue el producto conflictivo.

**Ejercicio 4.3** — Niveles de aislamiento  
Investiga los niveles de aislamiento JDBC (`TRANSACTION_READ_COMMITTED`, `TRANSACTION_SERIALIZABLE`…). Escribe un programa que cambie el nivel y explique en un comentario qué fenómenos de concurrencia previene cada uno.

---

## Comparativa RA8 vs RA9

| Aspecto | RA8 — BBDD OO (ObjectDB/JPA) | RA9 — BBDD Relacional (JDBC) |
|---|---|---|
| Unidad de datos | Objeto Java | Fila en una tabla SQL |
| Lenguaje de consulta | JPQL | SQL estándar |
| Gestión de relaciones | Referencias directas entre objetos | Claves foráneas + JOIN |
| Herencia | Nativa (`@Inheritance`) | Requiere estrategia de mapeo manual |
| Configuración | `persistence.xml` | Cadena de conexión JDBC |
| Curva de aprendizaje | Más natural para OO | Más habitual en el sector |

---

## Recursos oficiales

| Recurso | URL |
|---|---|
| JDBC Overview — Oracle | https://docs.oracle.com/javase/tutorial/jdbc/overview/index.html |
| API `java.sql.Connection` (Java 17) | https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html |
| API `java.sql.PreparedStatement` (Java 17) | https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html |
| API `java.sql.ResultSet` (Java 17) | https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/ResultSet.html |
| H2 Database — Documentación oficial | https://h2database.com/html/main.html |
| H2 — Tutorial de conexión | https://h2database.com/html/tutorial.html |
