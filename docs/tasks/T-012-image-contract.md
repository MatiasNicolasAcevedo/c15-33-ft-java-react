# T-012 · Alinear el contrato de imágenes entre front y back
- Estado: Bloqueada (por T-009; T-003 y T-004 mergeadas) · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `fix/T-012-image-contract` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-003, T-004, T-009 · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
A-9/F: el back guarda `url = "blob:http://127.0.0.1:5500/<id>"` (`ImageServiceImp.java:172-176`) y responde `imageUrls`; el front espera `property.images` (ids) y arma `${API}/img/${images[0]}` (`HostCardItem.jsx:17`, `DetailContainerCard.jsx:27`, `CardContainer.jsx:88,100`). Resultado: ninguna imagen se ve.

## Objetivo
Las cards y el detalle muestran la primera imagen de cada propiedad, servida por `GET /v1/api/img/{id}` sin token.

## Alcance
- Incluye: decidir el contrato (recomendado: `PropertyResponse` expone `imageIds` o URLs relativas `/v1/api/img/{id}`; el front arma la URL con `API_URL`), ajustar back y front, tests en ambos.
- **No incluye:** mover las imágenes fuera de la DB (Backlog P2), miniaturas, `ImageController` que devuelve la entidad (Backlog P2: DTOs).

## Leer antes
- `ImageServiceImp.java:46-65,161-176` · `PropertyServiceImp.java:173-193` · `PropertyResponse.java`
- `src/components/shared/CardContainer/CardContainer.jsx:70-105` · `HostCardItem.jsx` · `DetailContainerCard.jsx`

## Criterios de aceptación
- [ ] `./scripts/verify.sh` pasa y la cobertura no baja
- [ ] Test de integración: crear propiedad con imagen → `GET` del detalle → la URL/ID devuelta sirve la imagen con su `Content-Type` sin token
- [ ] Test de componente (MSW): la card renderiza `<img>` con la URL esperada
- [ ] Manual con `./scripts/dev.sh`: una propiedad creada desde la UI muestra su imagen (captura 375 y 1280 px en el PR)

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Pendientes → …
