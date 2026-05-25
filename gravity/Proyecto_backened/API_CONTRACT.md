# 📋 CONTRATO DE API - Backend (Momento 3)

**Proyecto:** Inventario y Trazabilidad - Cosmo Schools  
**Versión:** 1.0  
**Fecha:** 2026-05-24  
**Documentación Interactiva:** `http://localhost:8080/swagger-ui.html`

---

## 🎯 Introducción

Este documento define el **contrato definitivo** entre el **Backend (Java Spring Boot)** y el **Frontend (React)** para la consumición de la API REST.

Todos los endpoints aceptan y devuelven datos en formato **JSON**. Las peticiones deben incluir el header:
```
Content-Type: application/json
```

El CORS está habilitado solo para: `http://localhost:5173` o quizas sea otro, la vdd no tengo ni idea y es mejorar mirar el react (Desarrollo React)

---

## 📡 ENDPOINTS DISPONIBLES

### 🔵 **1. USUARIOS - Gestión de Usuarios**

#### 1.1 GET - Listar todos los usuarios

```
GET /api/usuarios
```

**Descripción:** Retorna la lista de todos los usuarios registrados en el sistema.

**Rol requerido:** PROFESOR/ADMIN (recomendado validar en frontend)

**Response - Status: 200 OK**

```json
[
  {
    "idUsuario": 1,
    "nombre": "Juan Pérez",
    "correo": "juan.perez@cosmo.edu.co",
    "rol": "ESTUDIANTE"
  },
  {
    "idUsuario": 2,
    "nombre": "María González",
    "correo": "maria.gonzalez@cosmo.edu.co",
    "rol": "PROFESOR"
  }
]
```

**Códigos de estado esperados:**
- `200 OK` - Lista obtenida correctamente
- `500 Internal Server Error` - Error del servidor

---

#### 1.2 POST - Registrar un nuevo usuario

```
POST /api/usuarios
```

**Descripción:** Crea un nuevo usuario en el sistema. Si no se especifica rol, se asigna ESTUDIANTE por defecto.

**Roles permitidos:** ESTUDIANTE, PROFESOR

**Request - Body (JSON)**

```json
{
  "nombre": "Carlos López",
  "documento": "987654321",
  "correo": "carlos.lopez@cosmo.edu.co",
  "rol": "ESTUDIANTE"
}
```

**Campos del Request:**
| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `nombre` | String | ✅ Sí | Nombre completo del usuario |
| `documento` | String | ✅ Sí | Número de documento de identidad |
| `correo` | String | ✅ Sí | Correo electrónico institucional |
| `rol` | String | ❌ No | ESTUDIANTE o PROFESOR (por defecto: ESTUDIANTE) |

**Response - Status: 201 Created**

```json
{
  "idUsuario": 3,
  "nombre": "Carlos López",
  "correo": "carlos.lopez@cosmo.edu.co",
  "rol": "ESTUDIANTE"
}
```

**Códigos de estado esperados:**
- `201 Created` - Usuario creado exitosamente
- `400 Bad Request` - Datos inválidos (ej: rol inválido)
- `500 Internal Server Error` - Error del servidor

---

### 🟢 **2. MATERIALES - Gestión del Inventario**

#### 2.1 GET - Listar todos los materiales

```
GET /api/materiales
```

**Descripción:** Retorna la lista completa del inventario de materiales.

**Rol requerido:** ESTUDIANTE y PROFESOR (acceso público a lectura)

**Response - Status: 200 OK**

```json
[
  {
    "id": 1,
    "name": "Laptop Dell XPS 13",
    "condition": "EXCELENTE",
    "nombreUsuario": "Juan Pérez"
  },
  {
    "id": 2,
    "name": "Monitor LG 24 pulgadas",
    "condition": "BUENA",
    "nombreUsuario": "María González"
  },
  {
    "id": 3,
    "name": "Mouse Logitech",
    "condition": "REGULAR",
    "nombreUsuario": null
  }
]
```

**Códigos de estado esperados:**
- `200 OK` - Lista obtenida correctamente
- `500 Internal Server Error` - Error del servidor

---

#### 2.2 GET - Buscar un material por ID

```
GET /api/materiales/{id}
```

**Descripción:** Retorna la información detallada de un material específico.

**Parámetros:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `id` | Integer | ID numérico del material (ej: 1, 2, 3) |

**Ejemplo de llamada:**
```
GET /api/materiales/1
```

**Response - Status: 200 OK**

```json
{
  "id": 1,
  "name": "Laptop Dell XPS 13",
  "condition": "EXCELENTE",
  "nombreUsuario": "Juan Pérez"
}
```

**Códigos de estado esperados:**
- `200 OK` - Material encontrado
- `404 Not Found` - Material no existe (id inválido)
- `500 Internal Server Error` - Error del servidor

---

#### 2.3 POST - Registrar un nuevo material

```
POST /api/materiales
```

**Descripción:** Crea un nuevo material en el inventario. **Solo los Profesores** pueden usar este endpoint.

**Rol requerido:** PROFESOR

**Request - Body (JSON)**

```json
{
  "name": "Teclado Mecánico RGB",
  "condition": "EXCELENTE",
  "date": "2026-05-24",
  "returndate": "2026-06-24",
  "usuarioId": 1
}
```

**Campos del Request:**
| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `name` | String | ✅ Sí | Nombre del material |
| `condition` | String | ✅ Sí | Estado del material (ej: EXCELENTE, BUENA, REGULAR) |
| `date` | String | ✅ Sí | Fecha de ingreso (formato: YYYY-MM-DD) |
| `returndate` | String | ✅ Sí | Fecha de devolución esperada (formato: YYYY-MM-DD) |
| `usuarioId` | Long | ❌ No | ID del usuario asignado al material (opcional) |

**Response - Status: 201 Created**

```json
{
  "id": 4,
  "name": "Teclado Mecánico RGB",
  "condition": "EXCELENTE",
  "nombreUsuario": "Juan Pérez"
}
```

**Códigos de estado esperados:**
- `201 Created` - Material creado exitosamente
- `400 Bad Request` - Datos inválidos o faltantes
- `404 Not Found` - El usuario indicado (usuarioId) no existe
- `500 Internal Server Error` - Error del servidor

---

## 🔑 Códigos de Estado HTTP Esperados

| Código | Significado | Cuándo ocurre |
|--------|------------|---------------|
| **200** | OK | Petición exitosa (GET sin problemas) |
| **201** | Created | Recurso creado exitosamente (POST exitoso) |
| **400** | Bad Request | Datos inválidos en el request |
| **404** | Not Found | Recurso no encontrado |
| **500** | Internal Server Error | Error del servidor |

---

## 🔐 Configuración de CORS

El backend **solo acepta peticiones desde:**
```
http://localhost:5173
```

Si intentas acceder desde otro origen, recibirás un error de CORS:
```
Access to XMLHttpRequest blocked by CORS policy
```

---

## 📝 Ejemplo Completo: Flujo Usuario + Material

### Paso 1: Registrar un usuario

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana García",
    "documento": "555666777",
    "correo": "ana.garcia@cosmo.edu.co",
    "rol": "ESTUDIANTE"
  }'
```

**Response (201 Created):**
```json
{
  "idUsuario": 4,
  "nombre": "Ana García",
  "correo": "ana.garcia@cosmo.edu.co",
  "rol": "ESTUDIANTE"
}
```

### Paso 2: Crear un material asignado a ese usuario

```bash
curl -X POST http://localhost:8080/api/materiales \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Audífonos Sony WH-CH720",
    "condition": "NUEVA",
    "date": "2026-05-24",
    "returndate": "2026-06-30",
    "usuarioId": 4
  }'
```

**Response (201 Created):**
```json
{
  "id": 5,
  "name": "Audífonos Sony WH-CH720",
  "condition": "NUEVA",
  "nombreUsuario": "Ana García"
}
```

### Paso 3: Consultar todos los materiales

```bash
curl -X GET http://localhost:8080/api/materiales
```

**Response (200 OK):** Lista completa incluyendo el nuevo material.

---

## 🧪 Pruebas en Swagger

Para realizar pruebas interactivas de todos los endpoints:

1. **Inicia el backend:**
   ```bash
   cd Proyecto_backened
   mvnw spring-boot:run
   ```

2. **Accede a Swagger UI:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Expande cada endpoint y haz clic en "Try it out"**
4. **Ingresa los datos JSON de ejemplo**
5. **Valida que los códigos HTTP devueltos sean los esperados (200, 201, 404, etc.)**

---

## 📌 Notas Importantes

- **Validaciones:** El backend valida que los roles sean solo ESTUDIANTE o PROFESOR
- **DTOs:** Se utilizan DTOs separados para Request y Response (seguridad)
- **Mapeo:** Los datos de la entidad se mapean a DTOs usando MapStruct
- **Swagger:** Todos los endpoints están documentados con anotaciones OpenAPI 3.0

---

**Documento generado para:** Equipo de Frontend  
**Última actualización:** 2026-05-24  

