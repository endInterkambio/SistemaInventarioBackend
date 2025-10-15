# 🔐 Seguridad y Autenticación

## 📋 Tabla de Contenidos
1. [Autenticación JWT](#autenticación-jwt)
2. [Configuración de Seguridad](#configuración-de-seguridad)
3. [Roles y Permisos](#roles-y-permisos)
4. [Encriptación](#encriptación)
5. [CORS y CSRF](#cors-y-csrf)

---

## 1. Autenticación JWT

El sistema utiliza **JSON Web Tokens (JWT)** para autenticación stateless (sin estado en el servidor).

### Flujo de Autenticación

```
┌─────────┐                                    ┌─────────┐
│ Cliente │                                    │ Servidor│
└────┬────┘                                    └────┬────┘
     │                                              │
     │  1. POST /api/auth/login                    │
     │     { username, password }                  │
     ├────────────────────────────────────────────>│
     │                                              │
     │                                              │ 2. Valida credenciales
     │                                              │    contra la BD
     │                                              │
     │  3. Retorna tokens                          │
     │     { accessToken, refreshToken }           │
     │<────────────────────────────────────────────┤
     │                                              │
     │  4. GET /api/books                          │
     │     Authorization: Bearer <accessToken>     │
     ├────────────────────────────────────────────>│
     │                                              │
     │                                              │ 5. Valida token JWT
     │                                              │    Extrae usuario y roles
     │                                              │
     │  6. Retorna datos                           │
     │<────────────────────────────────────────────┤
     │                                              │
     │  7. POST /api/auth/refresh                  │
     │     { refreshToken }                        │
     ├────────────────────────────────────────────>│
     │                                              │
     │                                              │ 8. Valida refresh token
     │                                              │    Genera nuevo access token
     │                                              │
     │  9. Retorna nuevo access token              │
     │<────────────────────────────────────────────┤
     │                                              │
```

---

### Tipos de Tokens

#### Access Token
- **Propósito:** Acceso a recursos protegidos
- **Duración:** 1 hora (3600000 ms)
- **Uso:** Se envía en cada petición HTTP en el header `Authorization`
- **Formato:** `Bearer <token>`

#### Refresh Token
- **Propósito:** Renovar el access token cuando expira
- **Duración:** 7 días (604800000 ms)
- **Uso:** Se envía solo al endpoint `/api/auth/refresh`
- **Seguridad:** Debe almacenarse de forma segura en el cliente

---

### Configuración de Tokens

```properties
# application.properties
jwt.secret=<clave_secreta_segura>
jwt.access.expiration=3600000      # 1 hora (ms)
jwt.refresh.expiration=604800000   # 7 días (ms)
```

**⚠️ Importante:** La clave secreta debe ser:
- Larga (mínimo 256 bits)
- Aleatoria
- Almacenada de forma segura (variables de entorno)
- Nunca expuesta en el código fuente

---

### Endpoints de Autenticación

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

**Respuesta de error (401 Unauthorized):**
```json
{
  "timestamp": "2025-10-14T16:50:12",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciales inválidas",
  "path": "/api/auth/login"
}
```

---

#### Refresh Token
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

### Uso del Access Token

Todas las peticiones a endpoints protegidos deben incluir el access token:

```http
GET /api/books
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 2. Configuración de Seguridad

### SecurityConfig.java

```java
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de CORS
            .cors(cors -> cors.configurationSource(request -> {
                var config = new CorsConfiguration();
                config.setAllowedOrigins(List.of(
                    "http://localhost:5173",           // Desarrollo
                    "sistema.agentedepaz.com",         // Producción
                    "https://el.gusanitolector.pe"     // Producción
                ));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            
            // Deshabilitar CSRF (no necesario con JWT)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Política de sesiones: STATELESS
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Reglas de autorización
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            
            // Filtro JWT
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtProvider), 
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
```

---

### JwtAuthenticationFilter

Filtro que intercepta todas las peticiones HTTP y valida el token JWT:

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. Extraer token del header Authorization
        String token = extractToken(request);
        
        if (token != null && jwtProvider.validateToken(token)) {
            // 2. Extraer información del token
            String username = jwtProvider.getUsernameFromToken(token);
            List<String> roles = jwtProvider.getRolesFromToken(token);
            
            // 3. Crear autenticación
            Authentication auth = new UsernamePasswordAuthenticationToken(
                username, 
                null, 
                roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList())
            );
            
            // 4. Establecer autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        // 5. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
    
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

---

### JwtProvider

Componente que genera y valida tokens JWT:

```java
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    // Generar access token
    public String generateAccessToken(String username, List<String> roles) {
        return JWT.create()
            .withSubject(username)
            .withClaim("roles", roles)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + accessExpiration))
            .sign(Algorithm.HMAC256(secret));
    }

    // Generar refresh token
    public String generateRefreshToken(String username) {
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration))
            .sign(Algorithm.HMAC256(secret));
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // Extraer username del token
    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    // Extraer roles del token
    public List<String> getRolesFromToken(String token) {
        return JWT.decode(token).getClaim("roles").asList(String.class);
    }
}
```

---

## 3. Roles y Permisos

### Roles del Sistema

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| **ADMIN** | Administrador del sistema | Acceso completo a todos los módulos (CRUD) |
| **SELLER** | Vendedor | Lectura en libros, clientes, órdenes. Creación de ventas. |
| **USER** | Usuario básico | Acceso limitado a funcionalidades específicas |

---

### Matriz de Permisos

| Módulo | Operación | ADMIN | SELLER | USER |
|--------|-----------|-------|--------|------|
| **Libros** | Crear | ✅ | ❌ | ❌ |
| | Leer | ✅ | ✅ | ❌ |
| | Actualizar | ✅ | ❌ | ❌ |
| | Eliminar | ✅ | ❌ | ❌ |
| | Exportar | ✅ | ❌ | ❌ |
| **Clientes** | Crear | ✅ | ❌ | ❌ |
| | Leer | ✅ | ✅ | ❌ |
| | Actualizar | ✅ | ❌ | ❌ |
| | Eliminar | ✅ | ❌ | ❌ |
| **Órdenes de Venta** | Crear | ✅ | ✅ | ❌ |
| | Leer | ✅ | ✅ | ❌ |
| | Actualizar | ✅ | ❌ | ❌ |
| | Eliminar | ✅ | ❌ | ❌ |
| **Stock** | Crear | ✅ | ❌ | ❌ |
| | Leer | ✅ | ✅ | ❌ |
| | Actualizar | ✅ | ❌ | ❌ |
| | Eliminar | ✅ | ❌ | ❌ |
| **Usuarios** | Gestión | ✅ | ❌ | ❌ |
| **Pagos** | Registrar | ✅ | ✅ | ❌ |
| | Consultar | ✅ | ✅ | ❌ |

---

### Control de Acceso a Nivel de Método

Se utiliza `@PreAuthorize` para control granular:

```java
// Solo ADMIN
@PreAuthorize("hasAuthority('ADMIN')")
@PostMapping
public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) { ... }

// ADMIN o SELLER
@PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
@GetMapping
public Page<BookDTO> getBooks(...) { ... }

// Cualquier usuario autenticado
@PreAuthorize("isAuthenticated()")
@GetMapping("/profile")
public ResponseEntity<UserDTO> getProfile() { ... }
```

---

## 4. Encriptación

### Contraseñas

Las contraseñas se almacenan encriptadas usando **BCrypt**:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**Características de BCrypt:**
- Algoritmo de hash adaptativo
- Incluye salt automático
- Resistente a ataques de fuerza bruta
- Configurable en complejidad

**Ejemplo de uso:**
```java
// Al registrar un usuario
String rawPassword = "password123";
String hashedPassword = passwordEncoder.encode(rawPassword);
// Resultado: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

// Al validar login
boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);
```

---

## 5. CORS y CSRF

### CORS (Cross-Origin Resource Sharing)

Permite peticiones desde orígenes específicos:

```java
config.setAllowedOrigins(List.of(
    "http://localhost:5173",           // Frontend en desarrollo
    "sistema.agentedepaz.com",         // Frontend en producción
    "https://el.gusanitolector.pe"     // Frontend en producción
));
config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
config.setAllowedHeaders(List.of("*"));
config.setAllowCredentials(true);
```

**¿Por qué es necesario?**
- Los navegadores bloquean peticiones entre diferentes orígenes por seguridad
- CORS permite especificar qué orígenes pueden acceder a la API

---

### CSRF (Cross-Site Request Forgery)

**Deshabilitado** porque se usa autenticación JWT (stateless):

```java
.csrf(AbstractHttpConfigurer::disable)
```

**¿Por qué se deshabilita?**
- CSRF protege contra ataques en aplicaciones con sesiones basadas en cookies
- JWT no usa cookies de sesión, por lo que CSRF no es necesario
- Cada petición debe incluir explícitamente el token JWT

---

## 🔒 Mejores Prácticas de Seguridad

### ✅ Implementadas

1. **Autenticación JWT stateless**
   - No se almacena estado en el servidor
   - Tokens con expiración

2. **Contraseñas encriptadas con BCrypt**
   - Hash adaptativo
   - Salt automático

3. **Control de acceso basado en roles**
   - Permisos granulares por endpoint
   - Validación a nivel de método

4. **CORS configurado**
   - Solo orígenes permitidos
   - Headers y métodos específicos

5. **Validación de tokens**
   - Verificación de firma
   - Verificación de expiración

6. **Sesiones stateless**
   - No se almacenan sesiones en el servidor
   - Escalabilidad horizontal

---

### 🔐 Recomendaciones Adicionales

1. **HTTPS en producción**
   - Todos los tokens deben transmitirse por HTTPS
   - Evita man-in-the-middle attacks

2. **Rotación de claves**
   - Cambiar `jwt.secret` periódicamente
   - Implementar versionado de tokens

3. **Rate limiting**
   - Limitar intentos de login
   - Prevenir ataques de fuerza bruta

4. **Logging de seguridad**
   - Registrar intentos de login fallidos
   - Registrar accesos no autorizados

5. **Tokens de un solo uso**
   - Implementar blacklist de tokens
   - Invalidar tokens al logout

6. **Refresh token rotation**
   - Generar nuevo refresh token al renovar
   - Invalidar refresh token anterior

7. **Auditoría**
   - Registrar quién hace qué y cuándo
   - Campos `createdBy` y `updatedBy` en entidades
