# T-015 · Rotar el secreto JWT en todo entorno que lo haya usado
- Estado: Bloqueada (por T-001) · Prioridad: P0 · Esfuerzo: S · Ejecuta: 👤 dueño
- Fase: P0 Estabilizar
- Rama: — · Depende de: T-001 mergeada · Spec/ADR: [0003](../adr/0003-jwt-stateless-auth.md)
- Revisa: —

## Por qué
🔴 S-1: el secreto de firma estuvo commiteado desde dic-2023 (`JwtService.java:28` en el historial). Borrarlo del código (T-001) no alcanza: sigue en el historial de git y cualquiera puede forjar tokens para un deploy que lo use.

## Pasos
1. Averiguar si el deploy anterior (back en Render o en la VPS que usa el front; front en Vercel) sigue activo. Si **no** hay deploy activo: marcar esta tarea como hecha con esa nota (el secreto viejo queda inservible; **nunca reutilizarlo**).
2. Si hay deploy activo: generar un secreto nuevo **en tu máquina**:

```bash
openssl rand -base64 32
```

3. Cargarlo como variable/secreto `JWT_SECRET` en el proveedor (y `JWT_EXPIRATION=86400000`), redeployar con la versión que incluye T-001. Todos los tokens viejos dejan de valer (los usuarios vuelven a loguearse).
4. Si la base de producción usaba credenciales que alguna vez estuvieron en un archivo compartido, rotarlas también.
5. No pegues el secreto en issues, PRs, docs ni chats.

## Criterios de aceptación
- [ ] Deploy activo: un token firmado con el secreto viejo devuelve 401/403 en producción · o: nota "sin deploy activo" con fecha
- [ ] `docs/STATUS.md` → "Decisiones pendientes" actualizado

## Resultado
- Fecha: … · Notas: …
