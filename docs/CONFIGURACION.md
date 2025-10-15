# ⚙️ Configuración y Despliegue

## 📋 Tabla de Contenidos
1. [Configuración del Entorno](#configuración-del-entorno)
2. [Variables de Entorno](#variables-de-entorno)
3. [Perfiles de Configuración](#perfiles-de-configuración)
4. [Base de Datos](#base-de-datos)
5. [Compilación y Ejecución](#compilación-y-ejecución)
6. [Despliegue](#despliegue)

---

## 1. Configuración del Entorno

### Requisitos Previos

| Componente | Versión Requerida | Propósito |
|------------|-------------------|-----------|
| **Java JDK** | 17 o superior | Entorno de ejecución |
| **Maven** | 3.6+ | Gestión de dependencias |
| **MySQL** | 8.0+ | Base de datos |
| **Git** | 2.0+ | Control de versiones |

### Instalación de Java 17

**Windows:**
```bash
# Descargar desde Oracle o usar SDKMAN
sdk install java 17.0.8-oracle
```

**Linux/Mac:**
```bash
# Usando SDKMAN
curl -s "https://get.sdkman.io" | bash
sdk install java 17.0.8-oracle
```

**Verificar instalación:**
```bash
java -version
# Debe mostrar: java version "17.x.x"
```

---

### Instalación de Maven

**Windows:**
```bash
# Descargar desde https://maven.apache.org/download.cgi
# Agregar al PATH: C:\apache-maven-3.x.x\bin
```

**Linux/Mac:**
```bash
# Usando SDKMAN
sdk install maven 3.9.5
```

**Verificar instalación:**
```bash
mvn -version
# Debe mostrar: Apache Maven 3.x.x
```

---

### Instalación de MySQL

**Windows:**
```bash
# Descargar desde https://dev.mysql.com/downloads/installer/
# Instalar MySQL Server 8.0+
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo mysql_secure_installation
```

**Mac:**
```bash
brew install mysql
brew services start mysql
```

---

## 2. Variables de Entorno

### Variables Requeridas

El sistema utiliza variables de entorno para configuración sensible:

```bash
# Base de Datos
DB_HOST=localhost
DB_PORT=3306
DB_NAME=inventario_db
DB_USER=root
DB_PASSWORD=tu_password_seguro

# JWT
JWT_SECRET=tu_clave_secreta_muy_larga_y_segura_minimo_256_bits
JWT_ACCESS_EXPIRATION=3600000      # 1 hora en ms
JWT_REFRESH_EXPIRATION=604800000   # 7 días en ms

# Perfil activo
SPRING_PROFILES_ACTIVE=dev  # dev, prod
```

---

### Configurar Variables de Entorno

**Windows (PowerShell):**
```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="inventario_db"
$env:DB_USER="root"
$env:DB_PASSWORD="password"
$env:JWT_SECRET="mi_clave_secreta_super_segura_de_256_bits_minimo"
```

**Windows (CMD):**
```cmd
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=inventario_db
set DB_USER=root
set DB_PASSWORD=password
set JWT_SECRET=mi_clave_secreta_super_segura_de_256_bits_minimo
```

**Linux/Mac:**
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=inventario_db
export DB_USER=root
export DB_PASSWORD=password
export JWT_SECRET=mi_clave_secreta_super_segura_de_256_bits_minimo
```

**Archivo `.env` (recomendado para desarrollo):**
```bash
# Crear archivo .env en la raíz del proyecto
DB_HOST=localhost
DB_PORT=3306
DB_NAME=inventario_db
DB_USER=root
DB_PASSWORD=password
JWT_SECRET=mi_clave_secreta_super_segura_de_256_bits_minimo
SPRING_PROFILES_ACTIVE=dev
```

---

## 3. Perfiles de Configuración

El sistema tiene tres archivos de configuración:

### application.properties (Principal)

```properties
# Configuración JWT
jwt.secret=${JWT_SECRET}
jwt.access.expiration=${JWT_ACCESS_EXPIRATION:3600000}
jwt.refresh.expiration=${JWT_REFRESH_EXPIRATION:604800000}

# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Configuración de Jackson (JSON)
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=America/Lima

# Tamaño máximo de archivos
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized
```

---

### application-dev.properties (Desarrollo)

```properties
# Perfil de desarrollo
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging más detallado
logging.level.org.springframework=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.interkambio=DEBUG
```

**Activar perfil de desarrollo:**
```bash
export SPRING_PROFILES_ACTIVE=dev
# o
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

### application-prod.properties (Producción)

```properties
# Perfil de producción
spring.jpa.show-sql=false

# Logging mínimo
logging.level.org.springframework=WARN
logging.level.org.interkambio=INFO

# Actuator solo health
management.endpoints.web.exposure.include=health
```

**Activar perfil de producción:**
```bash
export SPRING_PROFILES_ACTIVE=prod
# o
java -jar target/SistemaInventarioBackend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## 4. Base de Datos

### Crear Base de Datos

```sql
-- Conectar a MySQL
mysql -u root -p

-- Crear base de datos
CREATE DATABASE inventario_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuario (opcional)
CREATE USER 'inventario_user'@'localhost' IDENTIFIED BY 'password_seguro';
GRANT ALL PRIVILEGES ON inventario_db.* TO 'inventario_user'@'localhost';
FLUSH PRIVILEGES;

-- Verificar
SHOW DATABASES;
USE inventario_db;
```

---

### Migración de Esquema

El sistema usa **Hibernate** con `ddl-auto=validate`, lo que significa que NO crea ni modifica tablas automáticamente.

**Opciones de `ddl-auto`:**
- `validate`: Solo valida que el esquema coincida (recomendado para producción)
- `update`: Actualiza el esquema automáticamente (desarrollo)
- `create`: Crea el esquema desde cero (borra datos existentes)
- `create-drop`: Crea y borra al finalizar (testing)
- `none`: No hace nada

**Para desarrollo inicial:**
```properties
# application-dev.properties
spring.jpa.hibernate.ddl-auto=update
```

**Para producción:**
```properties
# application-prod.properties
spring.jpa.hibernate.ddl-auto=validate
```

---

### Scripts SQL

Si prefieres gestionar el esquema manualmente, puedes generar los scripts:

```bash
# Generar DDL desde las entidades
mvn clean compile
# Los scripts se pueden generar con herramientas como Hibernate Schema Export
```

---

## 5. Compilación y Ejecución

### Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd SistemaInventarioBackend
```

---

### Compilar el Proyecto

```bash
# Limpiar y compilar
mvn clean compile

# Ejecutar tests
mvn test

# Empaquetar (genera JAR)
mvn clean package

# Saltar tests al empaquetar
mvn clean package -DskipTests
```

El archivo JAR se genera en: `target/SistemaInventarioBackend-0.0.1-SNAPSHOT.jar`

---

### Ejecutar en Desarrollo

**Opción 1: Con Maven**
```bash
mvn spring-boot:run
```

**Opción 2: Con Maven y perfil específico**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Opción 3: Ejecutar JAR**
```bash
java -jar target/SistemaInventarioBackend-0.0.1-SNAPSHOT.jar
```

**Opción 4: Con variables de entorno**
```bash
DB_HOST=localhost DB_PORT=3306 DB_NAME=inventario_db \
DB_USER=root DB_PASSWORD=password \
JWT_SECRET=mi_clave_secreta \
mvn spring-boot:run
```

---

### Verificar que el Sistema Está Corriendo

```bash
# Health check
curl http://localhost:8080/actuator/health

# Respuesta esperada:
# {"status":"UP"}
```

**Endpoints disponibles:**
- API: `http://localhost:8080/api/`
- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`

---

## 6. Despliegue

### Despliegue en Servidor Linux

#### 1. Preparar el Servidor

```bash
# Actualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Java 17
sudo apt install openjdk-17-jdk -y

# Verificar
java -version
```

#### 2. Transferir el JAR

```bash
# Desde tu máquina local
scp target/SistemaInventarioBackend-0.0.1-SNAPSHOT.jar user@servidor:/opt/inventario/
```

#### 3. Crear Servicio Systemd

```bash
# Crear archivo de servicio
sudo nano /etc/systemd/system/inventario.service
```

**Contenido del archivo:**
```ini
[Unit]
Description=Sistema Inventario Backend
After=syslog.target network.target

[Service]
User=inventario
Group=inventario
WorkingDirectory=/opt/inventario
ExecStart=/usr/bin/java -jar /opt/inventario/SistemaInventarioBackend-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

# Variables de entorno
Environment="DB_HOST=localhost"
Environment="DB_PORT=3306"
Environment="DB_NAME=inventario_db"
Environment="DB_USER=inventario_user"
Environment="DB_PASSWORD=password_seguro"
Environment="JWT_SECRET=clave_secreta_muy_larga"
Environment="SPRING_PROFILES_ACTIVE=prod"

[Install]
WantedBy=multi-user.target
```

#### 4. Iniciar el Servicio

```bash
# Recargar systemd
sudo systemctl daemon-reload

# Habilitar inicio automático
sudo systemctl enable inventario

# Iniciar servicio
sudo systemctl start inventario

# Ver estado
sudo systemctl status inventario

# Ver logs
sudo journalctl -u inventario -f
```

---

### Despliegue con Docker

#### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/SistemaInventarioBackend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: inventario_db
      MYSQL_USER: inventario_user
      MYSQL_PASSWORD: password_seguro
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: inventario_db
      DB_USER: inventario_user
      DB_PASSWORD: password_seguro
      JWT_SECRET: mi_clave_secreta_super_segura
      SPRING_PROFILES_ACTIVE: prod
    depends_on:
      - mysql

volumes:
  mysql_data:
```

#### Comandos Docker

```bash
# Construir imagen
docker build -t inventario-backend .

# Ejecutar con docker-compose
docker-compose up -d

# Ver logs
docker-compose logs -f backend

# Detener
docker-compose down
```

---

### Despliegue en Railway

El proyecto incluye un script `deploy.sh` para despliegue en Railway.

```bash
# Ejecutar script de despliegue
./deploy.sh
```

**Configurar variables en Railway:**
1. Ir a tu proyecto en Railway
2. Settings → Variables
3. Agregar:
   - `DB_HOST`
   - `DB_PORT`
   - `DB_NAME`
   - `DB_USER`
   - `DB_PASSWORD`
   - `JWT_SECRET`
   - `SPRING_PROFILES_ACTIVE=prod`

---

### Nginx como Reverse Proxy

```nginx
server {
    listen 80;
    server_name api.tudominio.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

**Habilitar HTTPS con Let's Encrypt:**
```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d api.tudominio.com
```

---

## 🔧 Troubleshooting

### Error: "Could not find or load main class"

**Solución:**
```bash
mvn clean package
java -jar target/SistemaInventarioBackend-0.0.1-SNAPSHOT.jar
```

---

### Error: "Access denied for user"

**Solución:**
```sql
-- Verificar usuario y permisos
SHOW GRANTS FOR 'inventario_user'@'localhost';

-- Otorgar permisos
GRANT ALL PRIVILEGES ON inventario_db.* TO 'inventario_user'@'localhost';
FLUSH PRIVILEGES;
```

---

### Error: "Table doesn't exist"

**Solución:**
```properties
# Cambiar temporalmente a update para crear tablas
spring.jpa.hibernate.ddl-auto=update
```

---

### Error: "Port 8080 already in use"

**Solución:**
```properties
# Cambiar puerto en application.properties
server.port=8081
```

---

### Ver Logs Detallados

```properties
# application.properties
logging.level.org.springframework=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.interkambio=DEBUG
```

---

## 📊 Monitoreo

### Spring Boot Actuator

**Endpoints disponibles:**
- `/actuator/health` - Estado de salud
- `/actuator/info` - Información de la aplicación
- `/actuator/metrics` - Métricas

**Habilitar más endpoints:**
```properties
management.endpoints.web.exposure.include=health,info,metrics,env
```

---

## 🔐 Seguridad en Producción

### Checklist

- [ ] Usar HTTPS (SSL/TLS)
- [ ] Cambiar `JWT_SECRET` por uno seguro
- [ ] Usar contraseñas fuertes para BD
- [ ] Configurar firewall (solo puertos necesarios)
- [ ] Actualizar dependencias regularmente
- [ ] Hacer backups de la base de datos
- [ ] Configurar logs de auditoría
- [ ] Limitar intentos de login
- [ ] Usar variables de entorno (no hardcodear)
- [ ] Configurar CORS correctamente
