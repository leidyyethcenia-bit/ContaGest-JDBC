# ContaGest - Módulo de clientes (JDBC)

Software para un **despacho contable**. Este módulo administra los clientes del despacho
(personas naturales y jurídicas) y se conecta a la base de datos mediante **JDBC**,
con las cuatro operaciones **CRUD**: insertar, consultar, actualizar y eliminar.

- **Evidencia:** GA7-220501096-AA2-EV01 - Codificación de módulos del software
- **Aprendiz:** Leidy Yethcenia Muñoz Rodríguez
- **Lenguaje:** Java 17 o superior
- **Base de datos:** H2 en modo archivo (sin instalación). El driver viene en `lib/`.

## Cómo ejecutarlo

Requisito: tener JDK 17 o superior (GitHub Codespaces ya lo incluye).

```bash
./ejecutar.sh --demo   # demostración automática de INSERT, SELECT, UPDATE y DELETE
./ejecutar.sh          # menú interactivo en consola
```

En Windows: `ejecutar.bat --demo` o `ejecutar.bat`.
Con Maven: `mvn compile exec:java -Dexec.mainClass=com.contagest.Main`.

## Estructura del proyecto (arquitectura por capas)

```
src/main/java/com/contagest/
  Main.java                    punto de entrada
  config/ConexionBD.java       conexión JDBC y creación del esquema
  modelo/Cliente.java          entidad
  modelo/TipoPersona.java      enumeración (Natural / Jurídica)
  dao/CrudDAO.java             interfaz genérica CRUD
  dao/ClienteDAO.java          interfaz específica de clientes
  dao/ClienteDAOImpl.java      implementación JDBC (PreparedStatement)
  servicio/ClienteServicio.java  validaciones y lógica de negocio
  excepcion/                   excepciones propias
  vista/MenuConsola.java       menú interactivo
  vista/DemostracionCrud.java  demostración automática
src/main/resources/
  db.properties                parámetros de conexión
  schema.sql                   script de la tabla clientes
lib/h2-2.2.220.jar             driver JDBC
```

## Operaciones CRUD

| Operación | Método del DAO | Sentencia SQL |
|---|---|---|
| Insertar (Create) | `insertar(cliente)` | `INSERT INTO clientes ...` |
| Consultar (Read) | `consultarPorId`, `consultarTodos`, `buscarPorNit`, `buscarPorRazonSocial` | `SELECT ... FROM clientes` |
| Actualizar (Update) | `actualizar(cliente)` | `UPDATE clientes SET ...` |
| Eliminar (Delete) | `eliminar(id)` | `DELETE FROM clientes WHERE id = ?` |

Todas las consultas usan `PreparedStatement` (evita inyección SQL) y `try-with-resources`
(cierra las conexiones automáticamente).

## Estándar de codificación aplicado

| Elemento | Convención | Ejemplo |
|---|---|---|
| Paquetes | minúscula, sin guiones | `com.contagest.dao` |
| Clases e interfaces | PascalCase, sustantivo | `ClienteDAOImpl`, `ClienteServicio` |
| Métodos | camelCase, inician con verbo | `consultarPorId`, `buscarPorNit` |
| Variables y atributos | camelCase, descriptivos | `razonSocial`, `fechaRegistro` |
| Constantes | MAYUSCULAS_CON_GUION_BAJO | `SQL_INSERTAR`, `PATRON_NIT` |
| Booleanos | prefijo `es`/`is` | `activo` / `isActivo()` |
| Tablas y columnas BD | minúscula, snake_case | `clientes`, `razon_social` |

Además: Javadoc en clases y métodos públicos, una responsabilidad por clase, sangría de
4 espacios y nombres en español sin tildes ni eñes.

## Control de versiones

Repositorio remoto: ver `ENLACE_REPOSITORIO.txt` (en la carpeta comprimida).

```bash
git init
git add .
git commit -m "feat: modulo de clientes con JDBC y CRUD"
git branch -M main
git remote add origin https://github.com/leidyyethcenia-bit/ContaGest-JDBC.git
git push -u origin main
```

## Usar otra base de datos (MySQL / PostgreSQL)

Solo se cambia `db.properties` (driver, URL, usuario y clave) y se agrega el driver
correspondiente al classpath o al `pom.xml`. El código del DAO no cambia.
