# ✅ Validaciones Implementadas

## 📋 Tabla de Contenidos
1. [Validaciones de DTOs](#validaciones-de-dtos)
2. [Validaciones de Negocio](#validaciones-de-negocio)
3. [Validaciones de Integridad](#validaciones-de-integridad)
4. [Validaciones de Autorización](#validaciones-de-autorización)

---

## 1. Validaciones de DTOs (Jakarta Validation)

El sistema utiliza anotaciones de **Jakarta Validation** para validar datos de entrada en los DTOs.

### CustomerDTO

```java
@NotBlank(message = "El tipo de cliente es obligatorio")
private String customerType; // PERSON o COMPANY

@NotBlank(message = "El tipo de documento es obligatorio")
private String documentType; // DNI, RUC, PASSPORT

@NotBlank(message = "El número de documento es obligatorio")
@Size(min = 8, max = 11, message = "El número de documento debe tener entre 8 y 11 caracteres")
@Pattern(regexp = "\\d+", message = "El número de documento solo puede contener dígitos")
private String documentNumber;

@Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
private String name;

@Size(max = 150, message = "La razón social no debe superar los 150 caracteres")
private String companyName;

@Email(message = "El correo electrónico no tiene un formato válido")
@Size(max = 120, message = "El correo electrónico no debe superar los 120 caracteres")
private String email;

@Pattern(regexp = "\\+?\\d{7,12}", message = "El teléfono debe contener entre 7 y 12 dígitos")
private String phoneNumber;

@Size(max = 200, message = "La dirección no debe superar los 200 caracteres")
private String address;
```

### Anotaciones de Validación Disponibles

| Anotación | Descripción | Ejemplo |
|-----------|-------------|---------|
| `@NotNull` | El campo no puede ser null | `@NotNull private Long id;` |
| `@NotBlank` | El campo no puede estar vacío (null, "", " ") | `@NotBlank private String name;` |
| `@NotEmpty` | Colección/String no puede estar vacío | `@NotEmpty private List<String> items;` |
| `@Size(min, max)` | Longitud mínima y máxima | `@Size(min=8, max=11) private String dni;` |
| `@Min(value)` | Valor numérico mínimo | `@Min(0) private Integer stock;` |
| `@Max(value)` | Valor numérico máximo | `@Max(100) private Integer discount;` |
| `@Positive` | Número positivo (> 0) | `@Positive private BigDecimal price;` |
| `@PositiveOrZero` | Número positivo o cero (>= 0) | `@PositiveOrZero private Integer quantity;` |
| `@Negative` | Número negativo (< 0) | `@Negative private BigDecimal debt;` |
| `@Email` | Formato de email válido | `@Email private String email;` |
| `@Pattern(regexp)` | Expresión regular | `@Pattern(regexp="\\d+") private String phone;` |
| `@DecimalMin(value)` | Valor decimal mínimo | `@DecimalMin("0.0") private BigDecimal amount;` |
| `@DecimalMax(value)` | Valor decimal máximo | `@DecimalMax("999.99") private BigDecimal price;` |
| `@Past` | Fecha en el pasado | `@Past private LocalDate birthDate;` |
| `@Future` | Fecha en el futuro | `@Future private LocalDate expiryDate;` |
| `@Valid` | Validación en cascada | `@Valid private AddressDTO address;` |

---

## 2. Validaciones de Negocio

### StockLocationValidator

Valida reglas de negocio específicas para ubicaciones de stock.

#### Regla: No duplicar SHOWROOM

**Descripción:** Solo puede existir UN ejemplar en SHOWROOM por libro en Warehouse 1, independientemente de la condición del libro (NUEVO, USADO, etc.).

**Código:**
```java
public static void validateShowroomDuplicate(
    BookStockLocation entity, 
    BookStockLocationRepository repository
) {
    Long warehouseId = entity.getWarehouse().getId();
    
    // Solo Warehouse 1 permite SHOWROOM
    if (warehouseId == 1 && entity.getLocationType() == LocationType.SHOWROOM) {
        boolean exists = repository.existsByBookIdAndWarehouseIdAndLocationTypeAndIdNot(
            entity.getBook().getId(),
            warehouseId,
            LocationType.SHOWROOM,
            entity.getId() != null ? entity.getId() : -1
        );
        
        if (exists) {
            throw new RuntimeException(
                "Ya existe un ejemplar de este libro en SHOWROOM. " +
                "No se puede duplicar, aunque tenga distinta condición."
            );
        }
    }
}
```

**Cuándo se aplica:**
- Al crear una nueva ubicación de stock
- Al actualizar una ubicación existente
- Solo para Warehouse ID = 1
- Solo para LocationType = SHOWROOM

---

### Validaciones en Servicios

#### BookService

**Validación de SKU único:**
```java
// Al crear un libro, el SKU debe ser único
if (bookRepository.existsBySku(bookDTO.getSku())) {
    throw new IllegalArgumentException("Ya existe un libro con el SKU: " + bookDTO.getSku());
}
```

**Validación de stock negativo:**
```java
// El stock no puede ser negativo
if (stock < 0) {
    throw new IllegalArgumentException("El stock no puede ser negativo");
}
```

#### SaleOrderService

**Validación de stock disponible:**
```java
// Al crear una orden, debe haber stock suficiente
if (availableStock < requestedQuantity) {
    throw new IllegalArgumentException(
        "Stock insuficiente. Disponible: " + availableStock + 
        ", Solicitado: " + requestedQuantity
    );
}
```

**Validación de cliente:**
```java
// La orden debe tener un cliente asociado
if (saleOrderDTO.getCustomerId() == null) {
    throw new IllegalArgumentException("La orden debe tener un cliente asociado");
}
```

#### CustomerService

**Validación de documento único:**
```java
// El documento del cliente debe ser único
if (customerRepository.existsByDocumentNumber(documentNumber)) {
    throw new IllegalArgumentException(
        "Ya existe un cliente con el documento: " + documentNumber
    );
}
```

**Validación de tipo de cliente vs documento:**
```java
// Si es COMPANY, debe tener RUC (11 dígitos)
if (customerType == CustomerType.COMPANY && documentNumber.length() != 11) {
    throw new IllegalArgumentException("Las empresas deben tener RUC de 11 dígitos");
}

// Si es PERSON con DNI, debe tener 8 dígitos
if (customerType == CustomerType.PERSON && documentType == DocumentType.DNI 
    && documentNumber.length() != 8) {
    throw new IllegalArgumentException("El DNI debe tener 8 dígitos");
}
```

---

## 3. Validaciones de Integridad

El `GlobalExceptionHandler` captura violaciones de integridad de datos de la base de datos.

### Restricción única en ubicaciones de stock

**Mensaje de error:**
```
"Ya existe una ubicación para este libro con los mismos datos (estante, piso, condición, etc.)."
```

**Código:**
```java
if (ex.getMessage().contains("book_stock_locations_unique")) {
    message = "Ya existe una ubicación para este libro con los mismos datos " +
              "(estante, piso, condición, etc.).";
}
```

**Cuándo ocurre:**
- Al intentar crear una ubicación duplicada con los mismos atributos
- La base de datos tiene una restricción UNIQUE en la combinación de campos

---

### Restricción de clave foránea

**Mensaje de error:**
```
"No se puede eliminar este libro porque tiene ubicaciones asociadas. Elimina primero las ubicaciones."
```

**Código:**
```java
if (ex.getMessage().contains("fk_book_stock_locations_book_id")) {
    message = "No se puede eliminar este libro porque tiene ubicaciones asociadas. " +
              "Elimina primero las ubicaciones.";
}
```

**Cuándo ocurre:**
- Al intentar eliminar un libro que tiene ubicaciones de stock asociadas
- La base de datos tiene una restricción de clave foránea

---

### Documento de cliente duplicado

**Mensaje de error:**
```
"Ya existe un cliente registrado con el mismo documento."
```

**Código:**
```java
if (ex.getMessage().contains("customers.dni")) {
    message = "Ya existe un cliente registrado con el mismo documento.";
}
```

**Cuándo ocurre:**
- Al intentar crear un cliente con un documento que ya existe
- La base de datos tiene una restricción UNIQUE en el campo `document_number`

---

### Otras violaciones de integridad

**Mensaje genérico:**
```
"Error de integridad de datos: [detalle del error]"
```

**Código:**
```java
else {
    message = "Error de integridad de datos: " + ex.getMostSpecificCause().getMessage();
}
```

---

## 4. Validaciones de Autorización

El sistema utiliza `@PreAuthorize` para validar permisos de acceso a nivel de método.

### Roles del Sistema

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| **ADMIN** | Administrador | Acceso completo (CRUD en todos los módulos) |
| **SELLER** | Vendedor | Solo lectura en la mayoría de módulos |
| **USER** | Usuario básico | Acceso limitado |

---

### Ejemplos de Validaciones de Autorización

#### Solo ADMIN

```java
@PreAuthorize("hasAuthority('ADMIN')")
@PostMapping
public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
    return ResponseEntity.ok(bookService.save(bookDTO));
}
```

**Endpoints que requieren ADMIN:**
- Crear libros: `POST /api/books`
- Actualizar libros: `PUT /api/books/{id}`
- Eliminar libros: `DELETE /api/books/{id}`
- Crear clientes: `POST /api/customers`
- Crear órdenes: `POST /api/sale-orders`
- Gestión de usuarios: `/api/users/**`

---

#### ADMIN o SELLER

```java
@PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
@GetMapping
public Page<BookDTO> getBooks(
    @ModelAttribute BookSearchCriteria criteria,
    Pageable pageable
) {
    return bookService.searchBooks(criteria, pageable);
}
```

**Endpoints que permiten ADMIN o SELLER:**
- Ver libros: `GET /api/books`
- Buscar libros: `GET /api/books?search=...`
- Ver clientes: `GET /api/customers`
- Ver órdenes: `GET /api/sale-orders`
- Ver stock: `GET /api/book-stock-locations`

---

#### Endpoint público

```java
// Sin @PreAuthorize - Acceso público
@PostMapping("/api/auth/login")
public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
    return ResponseEntity.ok(authService.login(request));
}
```

**Endpoints públicos:**
- Login: `POST /api/auth/login`

---

### Manejo de Errores de Autorización

Cuando un usuario intenta acceder a un recurso sin permisos:

**Código de estado HTTP:** `403 Forbidden`

**Respuesta:**
```json
{
  "timestamp": "2025-10-14T16:50:12",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/books"
}
```

---

## Resumen de Validaciones

### Validaciones de Entrada (DTOs)
✅ Campos obligatorios (`@NotBlank`, `@NotNull`)  
✅ Longitud de cadenas (`@Size`)  
✅ Formatos específicos (`@Email`, `@Pattern`)  
✅ Rangos numéricos (`@Min`, `@Max`, `@Positive`)  
✅ Fechas (`@Past`, `@Future`)

### Validaciones de Negocio (Servicios)
✅ SKU único  
✅ Stock no negativo  
✅ Stock suficiente para ventas  
✅ Documento de cliente único  
✅ Showroom no duplicado  
✅ Tipo de cliente vs documento

### Validaciones de Integridad (Base de Datos)
✅ Restricciones UNIQUE  
✅ Restricciones de clave foránea  
✅ Restricciones NOT NULL  
✅ Restricciones CHECK

### Validaciones de Autorización (Seguridad)
✅ Roles de usuario (ADMIN, SELLER, USER)  
✅ Permisos por endpoint  
✅ Tokens JWT válidos  
✅ Sesiones stateless
