# 📚 Documentación del Sistema de Inventario Backend

## 📋 Tabla de Contenidos
1. [Información General](#información-general)
2. [Tecnologías y Versiones](#tecnologías-y-versiones)
3. [Estructura del Proyecto](#estructura-del-proyecto)
4. [Arquitectura y Capas](#arquitectura-y-capas)

Para información detallada adicional, consulta:
- [VALIDACIONES.md](./docs/VALIDACIONES.md) - Validaciones implementadas
- [SEGURIDAD.md](./docs/SEGURIDAD.md) - Seguridad y autenticación
- [MODULOS.md](./docs/MODULOS.md) - Módulos funcionales
- [CONFIGURACION.md](./docs/CONFIGURACION.md) - Configuración del sistema

---

## 🎯 Información General

**Nombre del Proyecto:** Sistema de Inventario Backend  
**Organización:** Interkambio  
**Descripción:** Backend del sistema de inventario para GusanitoLector  
**Versión:** 0.0.1-SNAPSHOT

Este sistema es una API REST desarrollada para gestionar el inventario de libros, ventas, clientes, envíos y transacciones de inventario de una librería.

---

## 🛠️ Tecnologías y Versiones

### Framework Principal
- **Spring Boot:** 3.5.4
- **Java:** 17
- **Maven:** Gestor de dependencias

### Dependencias Principales

| Librería | Versión | Propósito |
|----------|---------|-----------|
| `spring-boot-starter-web` | 3.5.4 | API REST y controladores web |
| `spring-boot-starter-data-jpa` | 3.5.4 | Persistencia de datos con JPA/Hibernate |
| `spring-boot-starter-validation` | 3.5.4 | Validación de datos con Jakarta Validation |
| `spring-boot-starter-security` | 3.5.4 | Seguridad y autenticación |
| `spring-boot-starter-actuator` | 3.5.4 | Monitoreo y métricas de la aplicación |
| `mysql-connector-j` | (runtime) | Conector para base de datos MySQL |
| `lombok` | (latest) | Reducción de código boilerplate |
| `mapstruct` | 1.6.3 | Mapeo entre entidades y DTOs |
| `java-jwt` (Auth0) | 4.4.0 | Generación y validación de tokens JWT |
| `poi-ooxml` (Apache POI) | 5.2.3 | Lectura/escritura de archivos Excel |
| `opencsv` | 5.7.1 | Lectura/escritura de archivos CSV |
| `jackson-datatype-jsr310` | (latest) | Soporte para tipos de fecha/hora Java 8+ |

### Base de Datos
- **Motor:** MySQL
- **Configuración:** Variables de entorno para conexión
  - `DB_HOST`: Host de la base de datos
  - `DB_PORT`: Puerto de la base de datos
  - `DB_NAME`: Nombre de la base de datos
  - `DB_USER`: Usuario de la base de datos
  - `DB_PASSWORD`: Contraseña de la base de datos

---

## 📁 Estructura del Proyecto

```
SistemaInventarioBackend/
│
├── src/main/java/org/interkambio/SistemaInventarioBackend/
│   ├── SistemaInventarioBackendApplication.java  # Clase principal
│   │
│   ├── config/                    # Configuraciones del sistema
│   │   ├── DataInitializer.java          # Inicialización de datos
│   │   ├── JwtAuthenticationFilter.java  # Filtro de autenticación JWT
│   │   └── SecurityConfig.java           # Configuración de seguridad
│   │
│   ├── controller/                # Controladores REST (17 archivos)
│   ├── DTO/                       # Data Transfer Objects
│   ├── model/                     # Entidades JPA (22 archivos)
│   ├── repository/                # Repositorios JPA (14 archivos)
│   ├── service/                   # Servicios de negocio
│   ├── mapper/                    # Mappers (MapStruct) (15 archivos)
│   ├── specification/             # Especificaciones JPA para búsquedas
│   ├── criteria/                  # Criterios de búsqueda (8 archivos)
│   ├── exception/                 # Manejo de excepciones
│   ├── security/                  # Componentes de seguridad
│   ├── importer/                  # Importadores de archivos
│   ├── exporter/                  # Exportadores de archivos
│   └── util/                      # Utilidades
│
├── src/main/resources/
│   ├── application.properties         # Configuración principal
│   ├── application-dev.properties     # Configuración de desarrollo
│   └── application-prod.properties    # Configuración de producción
│
└── pom.xml                        # Configuración de Maven
```

### Descripción de Carpetas Principales

#### **`controller/`** - Controladores REST
Expone los endpoints de la API REST. Recibe peticiones HTTP y delega la lógica a los servicios.

**Controladores principales:**
- `AuthController` - Autenticación (login, refresh token)
- `BookController` - Gestión de libros
- `BookStockLocationController` - Ubicaciones de stock
- `CustomerController` - Gestión de clientes
- `SaleOrderController` - Órdenes de venta
- `ShipmentController` - Envíos
- `PaymentReceivedController` - Pagos recibidos
- `InventoryTransactionController` - Transacciones de inventario
- `UserController` - Gestión de usuarios
- `WarehouseController` - Almacenes

#### **`DTO/`** - Data Transfer Objects
Objetos de transferencia de datos organizados por módulo:
- **`auth/`**: Autenticación (LoginRequestDTO, LoginResponseDTO, etc.)
- **`common/`**: DTOs compartidos (SimpleIdNameDTO, ImportResult)
- **`inventory/`**: Inventario (BookDTO, BookStockLocationDTO, etc.)
- **`sales/`**: Ventas (CustomerDTO, SaleOrderDTO, ShipmentDTO, etc.)
- **`user/`**: Usuarios (UserDTO, RoleDTO, UserPreferenceDTO)

#### **`model/`** - Entidades JPA
Representan las tablas de la base de datos:
- `Book`, `BookStockLocation`, `Customer`, `SaleOrder`, `Shipment`, `PaymentReceived`, `InventoryTransaction`, `User`, `Warehouse`, etc.
- **Enums**: `BookCondition`, `LocationType`, `SaleOrderStatus`, `PaymentStatus`, `TransactionType`, `CustomerType`, `DocumentType`

#### **`repository/`** - Repositorios JPA
Interfaces que extienden `JpaRepository` para acceso a datos:
- Operaciones CRUD automáticas
- Consultas personalizadas con `@Query`
- Soporte para Specifications

#### **`service/`** - Servicios de Negocio
Contiene la lógica de negocio. Subdirectorio `impl/` con implementaciones.

#### **`mapper/`** - Mappers (MapStruct)
Convierte entidades a DTOs y viceversa usando MapStruct.

#### **`specification/`** - Especificaciones JPA
Define criterios de búsqueda dinámicos para consultas complejas.

#### **`criteria/`** - Criterios de Búsqueda
Objetos que encapsulan parámetros de búsqueda del cliente.

#### **`exception/`** - Manejo de Excepciones
- `GlobalExceptionHandler` - Manejo centralizado de errores
- Excepciones personalizadas

#### **`security/`** - Seguridad
- `JwtProvider` - Generación y validación de tokens JWT

#### **`importer/` y `exporter/`** - Importación/Exportación
Manejo de archivos Excel y CSV para importar/exportar datos.

#### **`util/`** - Utilidades
Clases de utilidad y validadores reutilizables.

---

## 🏗️ Arquitectura y Capas

El proyecto sigue una **arquitectura en capas** (Layered Architecture):

### 1. **Capa de Presentación (Controller)**
- Expone endpoints REST
- Valida datos de entrada
- Control de acceso con `@PreAuthorize`
- Delega lógica a servicios

### 2. **Capa de Transferencia (DTO)**
- Desacopla presentación de persistencia
- Validaciones con Jakarta Validation
- Transformación de datos

### 3. **Capa de Modelo (Entity)**
- Entidades JPA
- Relaciones entre entidades
- Auditoría automática

### 4. **Capa de Repositorio (Repository)**
- Acceso a base de datos
- Operaciones CRUD
- Consultas personalizadas

### 5. **Capa de Servicio (Service)**
- Lógica de negocio
- Coordinación entre repositorios
- Transformación Entity ↔ DTO

### 6. **Capa de Mapeo (Mapper)**
- Conversión Entity ↔ DTO
- Uso de MapStruct
- Generación automática de código

### 7. **Capa de Especificaciones (Specification)**
- Búsquedas dinámicas
- Construcción de consultas complejas
- Filtros opcionales

### 8. **Capa de Excepciones (Exception)**
- Manejo centralizado de errores
- Respuestas HTTP consistentes
- Mensajes de error claros

### 9. **Capa de Configuración (Config)**
- Configuración de seguridad
- Filtros JWT
- Inicialización de datos

### 10. **Capa de Seguridad (Security)**
- Generación de tokens JWT
- Validación de tokens
- Extracción de información

---

## 🔄 Flujo de una Petición HTTP

```
Cliente → Controller → Service → Repository → Base de Datos
                ↓         ↓          ↓
              DTO    Mapper    Entity
                ↓         ↓          ↓
         Validación  Lógica   Persistencia
```

**Ejemplo: Crear un libro**
1. Cliente envía POST `/api/books` con `BookDTO`
2. `BookController` valida el DTO con `@Valid`
3. `BookController` verifica permisos con `@PreAuthorize("hasAuthority('ADMIN')")`
4. `BookController` llama a `bookService.save(bookDTO)`
5. `BookService` convierte `BookDTO` a `Book` usando `BookMapper`
6. `BookService` aplica lógica de negocio
7. `BookService` guarda `Book` usando `BookRepository`
8. `BookRepository` persiste en la base de datos
9. `BookService` convierte `Book` a `BookDTO`
10. `BookController` retorna `ResponseEntity<BookDTO>`

---

## 📚 Convenciones de Código

- **Nombres de clases**: PascalCase (ej: `BookController`)
- **Nombres de métodos**: camelCase (ej: `findBySku`)
- **Nombres de constantes**: UPPER_SNAKE_CASE (ej: `MAX_STOCK`)
- **Nombres de paquetes**: lowercase (ej: `controller`, `service`)
- **Anotaciones**: Lombok para reducir boilerplate
- **Mapeo**: MapStruct para conversión Entity ↔ DTO
- **Transacciones**: `@Transactional` en servicios
- **Validaciones**: Jakarta Validation en DTOs

---

## 📖 Documentación Adicional

Para más detalles, consulta los siguientes documentos:

- **[VALIDACIONES.md](./docs/VALIDACIONES.md)** - Todas las validaciones implementadas
- **[SEGURIDAD.md](./docs/SEGURIDAD.md)** - Configuración de seguridad y JWT
- **[MODULOS.md](./docs/MODULOS.md)** - Descripción de cada módulo funcional
- **[CONFIGURACION.md](./docs/CONFIGURACION.md)** - Configuración y despliegue
