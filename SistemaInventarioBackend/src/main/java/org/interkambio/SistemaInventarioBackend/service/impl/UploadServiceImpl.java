package org.interkambio.SistemaInventarioBackend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadServiceImpl {

    // Directorio configurable según el perfil
    @Value("${app.upload.dir}")
    private String uploadDir;

    public String uploadImage(MultipartFile file, String sku) {
        try {
            String contentType = file.getContentType();
            if (contentType == null ||
                    !(contentType.equals("image/png") ||
                            contentType.equals("image/jpeg") ||
                            contentType.equals("image/webp"))) {
                throw new RuntimeException("Formato no permitido. Solo PNG, JPEG y WEBP.");
            }

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // crea la carpeta si no existe
            }

            // Extraer extensión
            String extension = switch (contentType) {
                case "image/png" -> ".png";
                case "image/jpeg" -> ".jpg";
                case "image/webp" -> ".webp";
                default -> "";
            };

            // Guardar usando el SKU como nombre de archivo
            File destination = new File(directory, sku + extension);
            file.transferTo(destination);

            // Retornar la URL accesible públicamente
            return "/uploads/" + sku + extension;

        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen: " + e.getMessage(), e);
        }
    }
}
