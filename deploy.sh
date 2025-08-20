#!/bin/bash

# ----------------------------
# Configuración
# ----------------------------
SERVICE_NAME=glbackend
JAR_NAME=SistemaInventarioBackend-0.0.1-SNAPSHOT.jar
DEPLOY_DIR=/var/www/el.gusanitolector.pe
JAR_PATH=$DEPLOY_DIR/$JAR_NAME
BACKUP_DIR=$DEPLOY_DIR/backups

# ----------------------------
# Crear carpeta de backups si no existe
# ----------------------------
mkdir -p $BACKUP_DIR

# ----------------------------
# Detener servicio
# ----------------------------
echo "Deteniendo servicio $SERVICE_NAME..."
sudo systemctl stop $SERVICE_NAME

# ----------------------------
# Respaldar jar antiguo
# ----------------------------
if [ -f "$JAR_PATH" ]; then
    TIMESTAMP=$(date +"%Y%m%d%H%M%S")
    echo "Respaldando jar antiguo..."
    mv $JAR_PATH $BACKUP_DIR/${JAR_NAME%.jar}-$TIMESTAMP.jar
fi

# ----------------------------
# Copiar nuevo jar (asume que ya lo subiste)
# ----------------------------
echo "Copiando nuevo jar..."
# Ya debería estar en DEPLOY_DIR, si no usar scp desde local
# cp /ruta/local/$JAR_NAME $DEPLOY_DIR/

# ----------------------------
# Reiniciar servicio
# ----------------------------
echo "Iniciando servicio $SERVICE_NAME..."
sudo systemctl start $SERVICE_NAME
sudo systemctl enable $SERVICE_NAME

# ----------------------------
# Verificar estado
# ----------------------------
sudo systemctl status $SERVICE_NAME --no-pager

echo "Despliegue completado ✅"
