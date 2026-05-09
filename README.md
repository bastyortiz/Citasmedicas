# Microservicio Citas Medicas

## Descripcion
Microservicio Spring Boot para administrar citas medicas con CRUD REST, respuestas JSON con HATEOAS y Oracle Cloud como base de datos en ejecucion real.

## Tecnologias
- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring HATEOAS
- Oracle JDBC
- H2 solo para pruebas
- JUnit 5
- Docker
- Postman

## Endpoint base
- Base: `/citas`
- Local: `http://localhost:8082/citas`
- Docker: `http://localhost:8081/citas`

## CRUD disponible
- `GET /citas`
- `GET /citas/{id}`
- `POST /citas`
- `PUT /citas/{id}`
- `DELETE /citas/{id}`

## HATEOAS
Las respuestas mantienen `_embedded`, `_links`, `self` y `collection`.

Ejemplo de listado:

```json
{
  "_embedded": {
    "citaMedicaList": []
  },
  "_links": {
    "self": {
      "href": "http://localhost:8081/citas"
    }
  }
}
```

## Ejecucion local
La ejecucion real usa Oracle Cloud desde `src/main/resources/application.properties`.

```powershell
.\mvnw.cmd spring-boot:run
```

Si el puerto `8082` esta ocupado:

```powershell
$env:SERVER_PORT=8083
.\mvnw.cmd spring-boot:run
```

## Oracle Cloud
La configuracion principal ya usa Oracle Cloud y Wallet.

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:oracle:thin:@mibd_high"
$env:SPRING_DATASOURCE_USERNAME="USER_CITAS"
$env:SPRING_DATASOURCE_PASSWORD="TU_PASSWORD_REAL"
$env:SPRING_DATASOURCE_DRIVER_CLASS_NAME="oracle.jdbc.OracleDriver"
$env:SPRING_JPA_HIBERNATE_DDL_AUTO="none"
$env:SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.OracleDialect"
$env:TNS_ADMIN="C:\Wallet_MIBD"
.\mvnw.cmd spring-boot:run
```

Las pruebas JUnit siguen usando H2 en `src/test/resources/application-test.properties`.

## Pruebas JUnit
```powershell
.\mvnw.cmd test
```

Resultado esperado:

```txt
BUILD SUCCESS
Failures: 0
Errors: 0
```

## Generar JAR
```powershell
.\mvnw.cmd clean package
```

## Docker
Build:

```powershell
docker build -t citasmedicas .
```

Run con Oracle Cloud y Wallet:

```powershell
docker rm -f citasmedicas-app
docker run -d --name citasmedicas-app -p 8081:8082 -e SERVER_PORT=8082 -e TNS_ADMIN=/opt/oracle/wallet -v C:\Wallet_MIBD:/opt/oracle/wallet citasmedicas
```

Verificacion:

```powershell
Invoke-RestMethod http://localhost:8081/citas | ConvertTo-Json -Depth 10
```

Validacion de logs Oracle:

```powershell
docker logs citasmedicas-app 2>&1 | Select-String "Hikari|oracle|Oracle|jdbc|USER_CITAS|Database|Started|ERROR|Exception"
```

## Cloudflare Tunnel
Solo documentado, sin URL fija:

```powershell
docker rm -f citasmedicas-tunnel
docker run -d --name citasmedicas-tunnel cloudflare/cloudflared:latest tunnel --url http://host.docker.internal:8081
docker logs citasmedicas-tunnel
```

## Ejemplos Postman
POST `http://localhost:8081/citas`

```json
{
  "nombrePaciente": "Prueba Video",
  "rutPaciente": "44444444-4",
  "nombreMedico": "Dra. Camila Torres",
  "especialidad": "Medicina General",
  "fechaCita": "2026-05-15",
  "horaCita": "09:30",
  "estado": "PROGRAMADA",
  "motivoConsulta": "Consulta de prueba"
}
```

PUT `http://localhost:8081/citas/1`

```json
{
  "nombrePaciente": "Prueba Video",
  "rutPaciente": "44444444-4",
  "nombreMedico": "Dra. Camila Torres",
  "especialidad": "Medicina General",
  "fechaCita": "2026-05-15",
  "horaCita": "10:00",
  "estado": "REAGENDADA",
  "motivoConsulta": "Consulta reagendada"
}
```

## Script SQL Oracle
Ubicacion: `database/citasmedicas_oracle.sql`

Incluye:
- creacion de tabla
- llave primaria
- campos obligatorios
- 3 inserts
- `COMMIT;`

## Validaciones
Se validan campos obligatorios en `POST` y `PUT` con `@Valid`, incluyendo nombre del paciente, RUT, medico, especialidad, fecha, hora y estado.

## Revision manual
- Ejecutar el script Oracle antes de iniciar el contenedor o la app contra Oracle.
- No subir la Wallet al repositorio.
- No publicar credenciales reales en GitHub.
- Confirmar en logs que no aparezca `jdbc:h2:mem:citasmedicas`.
- Probar el flujo CRUD en Postman para el video final.
