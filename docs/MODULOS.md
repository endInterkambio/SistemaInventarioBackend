# 📦 Módulos Funcionales

## 📋 Tabla de Contenidos
1. [Módulo de Libros](#módulo-de-libros)
2. [Módulo de Stock](#módulo-de-stock)
3. [Módulo de Clientes](#módulo-de-clientes)
4. [Módulo de Ventas](#módulo-de-ventas)
5. [Módulo de Envíos](#módulo-de-envíos)
6. [Módulo de Pagos](#módulo-de-pagos)
7. [Módulo de Transacciones](#módulo-de-transacciones)
8. [Módulo de Usuarios](#módulo-de-usuarios)

---

## 1. Módulo de Libros

### Descripción
Gestiona el catálogo de libros de la librería, incluyendo información bibliográfica, precios, ofertas y multimedia.

### Entidad Principal: `Book`

**Campos:**
- **Identificación:**
  - `id`: ID único
  - `sku`: Código único del producto
  - `title`: Título del libro
  - `isbn`: ISBN del libro

- **Autoría:**
  - `author`: Autor(es)
  - `publisher`: Editorial

- **Descriptivos:**
  - `description`: Descripción del libro
  - `category`: Categoría (almacenado como String con separador)
  - `subjects`: Temas/materias
  - `format`: Formato (almacenado como String con separador)
  - `language`: Idioma

- **Multimedia:**
  - `imageUrl`: URL de la imagen de portada
  - `websiteUrl`: URL del sitio web relacionado

- **Precios:**
  - `coverPrice`: Precio de portada
  - `purchasePrice`: Precio de compra
  - `sellingPrice`: Precio de venta
  - `fairPrice`: Precio justo
  - `offerPrice`: Precio de oferta
  - `offerStartDate`: Fecha inicio de oferta
  - `offerEndDate`: Fecha fin de oferta
  - `isOfferActive`: Si la oferta está activa

- **Clasificación:**
  - `tag`: Etiqueta
  - `filter`: Filtro
  - `productSaleType`: Tipo de venta del producto

- **Auditoría:**
  - `createdAt`: Fecha de creación
  - `updatedAt`: Fecha de actualización
  - `createdBy`: Usuario que creó
  - `updatedBy`: Usuario que actualizó

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/books` | Listar libros con paginación y filtros | ADMIN, SELLER |
| GET | `/api/books/{id}` | Obtener libro por ID | ADMIN, SELLER |
| GET | `/api/books/sku/{sku}` | Obtener libro por SKU | ADMIN, SELLER |
| POST | `/api/books` | Crear un libro | ADMIN |
| POST | `/api/books/batch` | Crear múltiples libros | ADMIN |
| PUT | `/api/books/{id}` | Actualizar libro completo | ADMIN |
| PATCH | `/api/books/{id}` | Actualización parcial | ADMIN |
| DELETE | `/api/books/{id}` | Eliminar libro | ADMIN |
| GET | `/api/books/export` | Exportar libros a Excel | ADMIN |

### Búsqueda Avanzada

**Criterios de búsqueda (`BookSearchCriteria`):**
- `search`: Búsqueda general (título, autor, ISBN, SKU, categoría, etc.)
- `title`: Filtrar por título
- `author`: Filtrar por autor
- `isbn`: Filtrar por ISBN
- `sku`: Filtrar por SKU
- `publisher`: Filtrar por editorial
- `categories`: Filtrar por categorías (lista)
- `subjects`: Filtrar por temas
- `formats`: Filtrar por formatos (lista)
- `tag`: Filtrar por etiqueta
- `filter`: Filtrar por filtro
- `minPrice` / `maxPrice`: Rango de precios
- `minStock` / `maxStock`: Rango de stock

**Ejemplo de búsqueda:**
```http
GET /api/books?search=harry&categories=Ficción&minPrice=10&maxPrice=50&page=0&size=20
```

### Importación/Exportación

**Importación:**
- Formato: Excel (.xlsx) o CSV
- Endpoint: `/api/upload/books`
- Validaciones automáticas
- Reporte de errores por fila

**Exportación:**
- Formato: Excel (.xlsx)
- Modos:
  - `all`: Todos los libros con todas las ubicaciones de stock
  - `best-stock`: Libros con la mejor ubicación de stock

---

## 2. Módulo de Stock

### Descripción
Gestiona las ubicaciones físicas del inventario de libros en diferentes almacenes.

### Entidad Principal: `BookStockLocation`

**Campos:**
- `id`: ID único
- `book`: Libro asociado
- `warehouse`: Almacén
- `locationType`: Tipo de ubicación (SHOWROOM, STORAGE)
- `bookcase`: Estante
- `bookcaseFloor`: Piso del estante
- `condition`: Condición del libro (NUEVO, USADO, etc.)
- `stock`: Cantidad en stock
- `notes`: Notas adicionales

### Enums

**LocationType:**
- `SHOWROOM`: Sala de exhibición
- `STORAGE`: Almacén

**BookCondition:**
- `NEW`: Nuevo
- `USED`: Usado
- `DAMAGED`: Dañado

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/book-stock-locations` | Listar ubicaciones con filtros | ADMIN, SELLER |
| GET | `/api/book-stock-locations/{id}` | Obtener ubicación por ID | ADMIN, SELLER |
| POST | `/api/book-stock-locations` | Crear ubicación | ADMIN |
| PUT | `/api/book-stock-locations/{id}` | Actualizar ubicación | ADMIN |
| PATCH | `/api/book-stock-locations/{id}/adjust` | Ajustar stock | ADMIN |
| DELETE | `/api/book-stock-locations/{id}` | Eliminar ubicación | ADMIN |

### Reglas de Negocio

1. **Showroom único:** Solo puede existir UN ejemplar en SHOWROOM por libro en Warehouse 1
2. **Stock no negativo:** El stock no puede ser negativo
3. **Ubicación única:** No puede haber ubicaciones duplicadas con los mismos atributos

### Búsqueda de Stock

**Criterios (`BookStockLocationSearchCriteria`):**
- `bookId`: Filtrar por libro
- `warehouseId`: Filtrar por almacén
- `locationType`: Filtrar por tipo de ubicación
- `condition`: Filtrar por condición
- `minStock` / `maxStock`: Rango de stock

---

## 3. Módulo de Clientes

### Descripción
Gestiona la información de clientes (personas y empresas) y sus contactos.

### Entidad Principal: `Customer`

**Campos:**
- `id`: ID único
- `customerType`: Tipo de cliente (PERSON, COMPANY)
- `documentType`: Tipo de documento (DNI, RUC, PASSPORT)
- `documentNumber`: Número de documento (único)
- `name`: Nombre (para personas)
- `companyName`: Razón social (para empresas)
- `email`: Correo electrónico
- `phoneNumber`: Teléfono
- `address`: Dirección
- `contacts`: Lista de contactos adicionales

### Entidad Relacionada: `CustomerContact`

**Campos:**
- `id`: ID único
- `customer`: Cliente asociado
- `name`: Nombre del contacto
- `position`: Cargo
- `email`: Correo electrónico
- `phoneNumber`: Teléfono

### Enums

**CustomerType:**
- `PERSON`: Persona natural
- `COMPANY`: Empresa

**DocumentType:**
- `DNI`: Documento Nacional de Identidad (8 dígitos)
- `RUC`: Registro Único de Contribuyentes (11 dígitos)
- `PASSPORT`: Pasaporte

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/customers` | Listar clientes con filtros | ADMIN, SELLER |
| GET | `/api/customers/{id}` | Obtener cliente por ID | ADMIN, SELLER |
| POST | `/api/customers` | Crear cliente | ADMIN |
| PUT | `/api/customers/{id}` | Actualizar cliente | ADMIN |
| DELETE | `/api/customers/{id}` | Eliminar cliente | ADMIN |
| POST | `/api/customers/{id}/contacts` | Agregar contacto | ADMIN |
| DELETE | `/api/customers/{id}/contacts/{contactId}` | Eliminar contacto | ADMIN |

### Validaciones Específicas

- Documento único en el sistema
- DNI: 8 dígitos numéricos
- RUC: 11 dígitos numéricos
- Email con formato válido
- Teléfono: 7-12 dígitos
- Si es COMPANY, debe tener RUC
- Si es PERSON con DNI, debe tener 8 dígitos

### Importación

- Formato: Excel (.xlsx)
- Endpoint: `/api/upload/customers`
- Validaciones automáticas de documento

---

## 4. Módulo de Ventas

### Descripción
Gestiona las órdenes de venta, items de venta y su ciclo de vida.

### Entidad Principal: `SaleOrder`

**Campos:**
- `id`: ID único
- `orderNumber`: Número de orden (único, ej: SO-00001)
- `orderDate`: Fecha de la orden
- `customer`: Cliente asociado
- `saleChannel`: Canal de venta
- `amount`: Monto de productos
- `amountShipment`: Monto de envío
- `additionalFee`: Tarifa adicional
- `status`: Estado de la orden
- `paymentStatus`: Estado de pago
- `items`: Lista de items de la orden
- `payments`: Lista de pagos recibidos
- `shipment`: Envío asociado (opcional)
- `customerNotes`: Notas del cliente
- `createdBy`: Usuario que creó
- `createdAt`: Fecha de creación

**Métodos de dominio:**
- `getTotalAmount()`: Calcula el total (amount + amountShipment + additionalFee)
- `getTotalPaid()`: Calcula el total pagado

### Entidad Relacionada: `SaleOrderItem`

**Campos:**
- `id`: ID único
- `order`: Orden asociada
- `book`: Libro vendido
- `quantity`: Cantidad
- `unitPrice`: Precio unitario
- `subtotal`: Subtotal (quantity × unitPrice)

### Enums

**SaleOrderStatus:**
- `PENDING`: Pendiente
- `CONFIRMED`: Confirmada
- `PROCESSING`: En proceso
- `SHIPPED`: Enviada
- `DELIVERED`: Entregada
- `CANCELLED`: Cancelada

**PaymentStatus:**
- `UNPAID`: No pagado
- `PARTIAL`: Pago parcial
- `PAID`: Pagado completo

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/sale-orders` | Listar órdenes con filtros | ADMIN, SELLER |
| GET | `/api/sale-orders/{id}` | Obtener orden por ID | ADMIN, SELLER |
| POST | `/api/sale-orders` | Crear orden | ADMIN, SELLER |
| PUT | `/api/sale-orders/{id}` | Actualizar orden | ADMIN |
| PATCH | `/api/sale-orders/{id}/status` | Cambiar estado | ADMIN |
| DELETE | `/api/sale-orders/{id}` | Eliminar orden | ADMIN |

### Búsqueda de Órdenes

**Criterios (`SaleOrderSearchCriteria`):**
- `orderNumber`: Número de orden
- `customerId`: Cliente
- `status`: Estado
- `paymentStatus`: Estado de pago
- `startDate` / `endDate`: Rango de fechas
- `saleChannel`: Canal de venta

### Flujo de Creación de Orden

1. Cliente selecciona productos
2. Sistema valida stock disponible
3. Se crea la orden con estado PENDING
4. Se reserva el stock
5. Cliente realiza el pago
6. Se actualiza paymentStatus
7. Se procesa el envío
8. Se actualiza status a SHIPPED/DELIVERED

---

## 5. Módulo de Envíos

### Descripción
Gestiona los envíos de las órdenes de venta.

### Entidad Principal: `Shipment`

**Campos:**
- `id`: ID único
- `order`: Orden de venta asociada (relación 1:1)
- `shipmentMethod`: Método de envío
- `trackingNumber`: Número de seguimiento
- `shippingAddress`: Dirección de envío
- `shippingDate`: Fecha de envío
- `estimatedDeliveryDate`: Fecha estimada de entrega
- `actualDeliveryDate`: Fecha real de entrega
- `notes`: Notas del envío

### Entidad Relacionada: `ShipmentMethod`

**Campos:**
- `id`: ID único
- `name`: Nombre del método (ej: "Olva Courier", "Shalom")
- `description`: Descripción
- `estimatedDays`: Días estimados de entrega
- `cost`: Costo del envío
- `isActive`: Si está activo

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/shipments` | Listar envíos | ADMIN, SELLER |
| GET | `/api/shipments/{id}` | Obtener envío por ID | ADMIN, SELLER |
| POST | `/api/shipments` | Crear envío | ADMIN |
| PUT | `/api/shipments/{id}` | Actualizar envío | ADMIN |
| PATCH | `/api/shipments/{id}/tracking` | Actualizar tracking | ADMIN |
| DELETE | `/api/shipments/{id}` | Eliminar envío | ADMIN |
| GET | `/api/shipment-methods` | Listar métodos de envío | ADMIN, SELLER |
| POST | `/api/shipment-methods` | Crear método | ADMIN |

---

## 6. Módulo de Pagos

### Descripción
Registra los pagos recibidos de las órdenes de venta.

### Entidad Principal: `PaymentReceived`

**Campos:**
- `id`: ID único
- `saleOrder`: Orden de venta asociada
- `amount`: Monto del pago
- `paymentDate`: Fecha del pago
- `paymentMethod`: Método de pago (ej: "Efectivo", "Transferencia", "Yape")
- `reference`: Referencia del pago
- `notes`: Notas adicionales

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/payments` | Listar pagos con filtros | ADMIN, SELLER |
| GET | `/api/payments/{id}` | Obtener pago por ID | ADMIN, SELLER |
| POST | `/api/payments` | Registrar pago | ADMIN, SELLER |
| PUT | `/api/payments/{id}` | Actualizar pago | ADMIN |
| DELETE | `/api/payments/{id}` | Eliminar pago | ADMIN |

### Búsqueda de Pagos

**Criterios (`PaymentReceivedCriteria`):**
- `saleOrderId`: Orden asociada
- `paymentMethod`: Método de pago
- `startDate` / `endDate`: Rango de fechas
- `minAmount` / `maxAmount`: Rango de montos

### Lógica de Negocio

- Al registrar un pago, se actualiza automáticamente el `paymentStatus` de la orden
- Si `totalPaid >= totalAmount`, el estado cambia a PAID
- Si `totalPaid < totalAmount` y `totalPaid > 0`, el estado es PARTIAL
- Si `totalPaid == 0`, el estado es UNPAID

---

## 7. Módulo de Transacciones

### Descripción
Registra todas las transacciones de inventario (entradas, salidas, ajustes).

### Entidad Principal: `InventoryTransaction`

**Campos:**
- `id`: ID único
- `book`: Libro asociado
- `warehouse`: Almacén
- `transactionType`: Tipo de transacción
- `quantity`: Cantidad (positiva para entrada, negativa para salida)
- `transactionDate`: Fecha de la transacción
- `reason`: Razón de la transacción
- `reference`: Referencia (ej: número de orden)
- `notes`: Notas adicionales
- `createdBy`: Usuario que creó

### Enum: `TransactionType`

- `ENTRY`: Entrada (compra, devolución)
- `EXIT`: Salida (venta, pérdida)
- `ADJUSTMENT`: Ajuste de inventario

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/inventory-transactions` | Listar transacciones | ADMIN, SELLER |
| GET | `/api/inventory-transactions/{id}` | Obtener transacción | ADMIN, SELLER |
| POST | `/api/inventory-transactions` | Crear transacción | ADMIN |

### Búsqueda de Transacciones

**Criterios (`InventoryTransactionSearchCriteria`):**
- `bookId`: Libro
- `warehouseId`: Almacén
- `transactionType`: Tipo
- `startDate` / `endDate`: Rango de fechas

### Casos de Uso

1. **Compra de libros:** ENTRY
2. **Venta de libros:** EXIT (automático al confirmar orden)
3. **Devolución de cliente:** ENTRY
4. **Pérdida/Robo:** EXIT
5. **Ajuste de inventario:** ADJUSTMENT

---

## 8. Módulo de Usuarios

### Descripción
Gestiona usuarios del sistema y sus preferencias.

### Entidad Principal: `User`

**Campos:**
- `id`: ID único
- `username`: Nombre de usuario (único)
- `password`: Contraseña encriptada
- `email`: Correo electrónico
- `roles`: Lista de roles
- `isActive`: Si está activo
- `preferences`: Preferencias del usuario

### Entidad Relacionada: `Role`

**Campos:**
- `id`: ID único
- `name`: Nombre del rol (ADMIN, SELLER, USER)
- `description`: Descripción

### Entidad Relacionada: `UserPreference`

**Campos:**
- `id`: ID único
- `user`: Usuario asociado
- `theme`: Tema de la interfaz
- `language`: Idioma
- `notificationsEnabled`: Si las notificaciones están habilitadas

### Endpoints

| Método | Endpoint | Descripción | Permisos |
|--------|----------|-------------|----------|
| GET | `/api/users` | Listar usuarios | ADMIN |
| GET | `/api/users/{id}` | Obtener usuario | ADMIN |
| POST | `/api/users` | Crear usuario | ADMIN |
| PUT | `/api/users/{id}` | Actualizar usuario | ADMIN |
| DELETE | `/api/users/{id}` | Eliminar usuario | ADMIN |
| GET | `/api/users/me` | Obtener perfil propio | Autenticado |
| PUT | `/api/users/me/preferences` | Actualizar preferencias | Autenticado |

---

## Resumen de Módulos

| Módulo | Entidades | Endpoints | Funcionalidad Principal |
|--------|-----------|-----------|------------------------|
| **Libros** | Book | 9 | Catálogo de productos |
| **Stock** | BookStockLocation | 6 | Ubicaciones de inventario |
| **Clientes** | Customer, CustomerContact | 7 | Gestión de clientes |
| **Ventas** | SaleOrder, SaleOrderItem | 6 | Órdenes de venta |
| **Envíos** | Shipment, ShipmentMethod | 7 | Gestión de envíos |
| **Pagos** | PaymentReceived | 5 | Registro de pagos |
| **Transacciones** | InventoryTransaction | 3 | Historial de inventario |
| **Usuarios** | User, Role, UserPreference | 7 | Gestión de usuarios |

---

## Relaciones entre Módulos

```
User ──┐
       ├──> Book ──> BookStockLocation ──> Warehouse
       │              │
       │              └──> InventoryTransaction
       │
       └──> Customer ──> SaleOrder ──> SaleOrderItem ──> Book
                           │
                           ├──> PaymentReceived
                           │
                           └──> Shipment ──> ShipmentMethod
```
