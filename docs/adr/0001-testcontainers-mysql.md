# 0001 · Tests de integración con Testcontainers + MySQL (sin H2)

- Estado: Aceptado · Fecha: 2026-10-10 · Decide: 🧠 (auditoría T-000)

## Contexto
La app usa MySQL (`mysql-connector-j`, dialecto MySQL, `LONGBLOB`/`LONGTEXT` en `Image.java:24` y `User.java:50`). El único test (`contextLoads`) necesita una MySQL externa y config manual, así que no es determinista ni corre en CI.

## Decisión
Los tests de integración (`@SpringBootTest`, `@DataJpaTest`) usan **Testcontainers con `mysql:8.4`**, la misma versión que `docker-compose.yml`, vía `@ServiceConnection`. Los unitarios y `@WebMvcTest` no usan DB.

## Alternativas
- **H2 en modo MySQL:** rápido, pero no soporta igual `LONGBLOB`/`columnDefinition` ni el comportamiento real de constraints; da falsos verdes.
- **DB de compose compartida:** ya funciona en `verify.sh`, pero depende del estado del volumen y del orden de tests.

## Consecuencias
- Docker es requisito para `./mvnw verify` (ya lo es para levantar la DB).
- El primer run descarga la imagen (~1 min). CI necesita Docker (ubuntu-latest lo tiene).
- `verify.sh` deja de necesitar la DB de compose para el back cuando T-002 termine.
