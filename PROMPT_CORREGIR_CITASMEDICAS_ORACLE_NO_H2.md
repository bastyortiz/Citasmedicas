# PROMPT CODEX - CORREGIR CITASMEDICAS PARA USAR ORACLE CLOUD EN EJECUCIÓN REAL

## Objetivo

Corregir el microservicio `citasmedicas` porque actualmente la ejecución real en Docker está usando H2 en memoria, cuando debe usar Oracle Cloud con Wallet.

El problema detectado en logs fue:

```txt
HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:citasmedicas user=SA
```

Eso significa que el contenedor `citasmedicas-app` NO está conectado a Oracle Cloud, sino a H2.

La meta es dejar la configuración así:

```txt
src/main/resources/application.properties       -> Oracle Cloud
src/test/resources/application-test.properties  -> H2 solo para pruebas JUnit
```

No rehacer el proyecto. No cambiar endpoints. No romper HATEOAS. No romper Docker. No romper las pruebas.

---

## Estado actual del problema

Actualmente `src/main/resources/application.properties` contiene configuración H2:

```properties
spring.application.name=citasmedicas
server.port=${SERVER_PORT:8082}

spring.datasource.url=jdbc:h2:mem:citasmedicas;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
spring.jpa.defer-datasource-initialization=true

spring.jackson.serialization.indent_output=true
```

Esto está mal para ejecución real porque hace que Docker use H2.

---

## Cambio requerido 1: application.properties debe usar Oracle Cloud

Reemplaza el contenido de:

```txt
src/main/resources/application.properties
```

por una configuración Oracle Cloud similar a esta:

```properties
spring.application.name=citasmedicas
server.port=${SERVER_PORT:8082}

spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:oracle:thin:@mibd_high}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:USER_CITAS}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:Citas12345}
spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER_CLASS_NAME:oracle.jdbc.OracleDriver}

spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:none}
spring.jpa.show-sql=true
spring.jpa.database-platform=${SPRING_JPA_DATABASE_PLATFORM:org.hibernate.dialect.OracleDialect}
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

spring.jackson.serialization.indent_output=true
```

Notas:

- Mantener `server.port=${SERVER_PORT:8082}` porque el proyecto ya funciona internamente en puerto 8082.
- Usar `jdbc:oracle:thin:@mibd_high` porque el proyecto trabaja con Oracle Cloud Wallet.
- Mantener usuario `USER_CITAS`.
- Mantener `ddl-auto=none` para no borrar ni recrear tablas en Oracle.
- No usar H2 en este archivo principal.

---

## Cambio requerido 2: H2 debe quedar solo para pruebas

Crea o corrige este archivo:

```txt
src/test/resources/application-test.properties
```

Debe contener H2 para que los tests no dependan de Oracle Cloud:

```properties
spring.application.name=citasmedicas-test
server.port=0

spring.datasource.url=jdbc:h2:mem:citasmedicas;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
spring.jpa.defer-datasource-initialization=true

spring.jackson.serialization.indent_output=true
```

Este archivo sí debe usar H2.

---

## Cambio requerido 3: tests deben usar perfil test

Revisar todas las clases de prueba en:

```txt
src/test/java
```

Las pruebas deben usar:

```java
@ActiveProfiles("test")
```

Ejemplo:

```java
@SpringBootTest
@ActiveProfiles("test")
class CitasmedicasApplicationTests {
}
```

Si hay tests de controlador o servicio, también agregar `@ActiveProfiles("test")` cuando corresponda.

Objetivo:

- `mvnw.cmd test` debe usar H2.
- Docker y ejecución real deben usar Oracle Cloud.

---

## Cambio requerido 4: revisar dependencia Oracle JDBC

Verifica que el `pom.xml` tenga el driver Oracle.

Debe existir algo similar a:

```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <scope>runtime</scope>
</dependency>
```

Si el proyecto usa Java 17, `ojdbc11` está bien.

No eliminar la dependencia de H2 si se usa para pruebas.

H2 puede quedar con scope test:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

Si H2 se usa solo para tests, no debe ser la base principal.

---

## Cambio requerido 5: revisar carga de datos

Si existe un archivo `data.sql` en:

```txt
src/main/resources
```

revisar que no esté pensado para H2 y que no rompa Oracle.

Si el proyecto usa Oracle real, los datos deberían estar en un script aparte:

```txt
database/citasmedicas_oracle.sql
```

y no depender de `data.sql` en ejecución real.

Si existe `src/test/resources/data.sql`, puede mantenerse para pruebas H2.

---

## Cambio requerido 6: conservar Docker

No cambiar el puerto actual.

El contenedor debe seguir ejecutándose así:

```powershell
docker run -d `
  --name citasmedicas-app `
  -p 8081:8082 `
  -e TNS_ADMIN=/opt/oracle/wallet `
  -v C:\Wallet_MIBD:/opt/oracle/wallet `
  citasmedicas
```

El endpoint externo debe seguir siendo:

```txt
http://localhost:8081/citas
```

El puerto interno de Spring sigue siendo:

```txt
8082
```

---

## Comandos de validación que deben funcionar

Después de aplicar los cambios, debo poder ejecutar:

```powershell
.\mvnw.cmd test
```

Debe terminar con:

```txt
BUILD SUCCESS
```

Luego:

```powershell
.\mvnw.cmd clean package
```

Luego:

```powershell
docker build -t citasmedicas .
```

Luego:

```powershell
docker rm -f citasmedicas-app
```

Luego:

```powershell
docker run -d `
  --name citasmedicas-app `
  -p 8081:8082 `
  -e TNS_ADMIN=/opt/oracle/wallet `
  -v C:\Wallet_MIBD:/opt/oracle/wallet `
  citasmedicas
```

---

## Validación de logs esperada

Después de levantar Docker, este comando:

```powershell
docker logs citasmedicas-app 2>&1 | Select-String "Hikari|oracle|Oracle|jdbc|USER_CITAS|Database|Started|ERROR|Exception"
```

NO debe mostrar:

```txt
jdbc:h2:mem:citasmedicas
```

Debe mostrar algo relacionado con Oracle, por ejemplo:

```txt
HikariPool-1 - Starting...
oracle.jdbc
jdbc:oracle:thin:@mibd_high
Oracle
Started CitasmedicasApplication
```

---

## Validación del endpoint

Después de confirmar Oracle en logs, debe funcionar:

```powershell
Invoke-RestMethod http://localhost:8081/citas | ConvertTo-Json -Depth 10
```

Debe devolver JSON con:

```txt
_embedded
_links
citaMedicaList
```

---

## Prueba definitiva contra Oracle

Después de levantar Docker conectado a Oracle, debo poder crear una cita:

```powershell
$body = @{
    nombrePaciente = "Prueba Oracle Real"
    rutPaciente = "99999999-9"
    nombreMedico = "Dr. Conexion"
    especialidad = "Medicina General"
    fechaCita = "2026-06-01"
    horaCita = "11:00"
    estado = "PROGRAMADA"
    motivoConsulta = "Validar conexion real con Oracle"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/citas" `
  -Method Post `
  -ContentType "application/json" `
  -Body $body | ConvertTo-Json -Depth 10
```

Luego en SQL Developer, conectado como `USER_CITAS`, debo poder ejecutar:

```sql
SELECT *
FROM citas_medicas
WHERE rut_paciente = '99999999-9';
```

Si aparece el registro, la conexión Oracle Cloud está confirmada.

---

## Checklist final

Confirma al terminar:

- [ ] `application.properties` principal usa Oracle Cloud.
- [ ] H2 fue eliminado del `application.properties` principal.
- [ ] `application-test.properties` usa H2.
- [ ] Las pruebas usan `@ActiveProfiles("test")`.
- [ ] `mvnw.cmd test` funciona.
- [ ] `mvnw.cmd clean package` funciona.
- [ ] Docker build funciona.
- [ ] Docker run funciona con `-p 8081:8082`.
- [ ] Logs Docker muestran Oracle y no H2.
- [ ] Endpoint `http://localhost:8081/citas` responde.
- [ ] HATEOAS sigue visible.
- [ ] POST desde API escribe en Oracle Cloud.
- [ ] No se rompió el endpoint `/citas`.

---

## Restricciones

- No cambiar el endpoint `/citas`.
- No cambiar el mapeo Docker `8081:8082`.
- No cambiar el nombre del servicio si no es necesario.
- No quitar HATEOAS.
- No quitar validaciones.
- No romper pruebas JUnit.
- No dejar H2 como base principal.
- No dejar `ddl-auto=create-drop` en ejecución real con Oracle.
- No incluir el Wallet Oracle dentro del repositorio.
- No subir credenciales reales a repositorios públicos.

---

## Resultado que debes devolver

Al terminar, responde:

1. Archivos modificados.
2. Cambios aplicados.
3. Confirmación de que `application.properties` usa Oracle.
4. Confirmación de que `application-test.properties` usa H2.
5. Confirmación de tests con perfil `test`.
6. Comandos exactos para reconstruir Docker.
7. Comando exacto para validar logs Oracle.
8. Cualquier punto que deba revisar manualmente.
