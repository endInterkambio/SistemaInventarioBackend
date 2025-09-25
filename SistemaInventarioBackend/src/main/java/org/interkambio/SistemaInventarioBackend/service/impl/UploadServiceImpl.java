package org.interkambio.SistemaInventarioBackend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadServiceImpl {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.payments.dir}")
    private String paymentsDir;

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
                directory.mkdirs();
            }

            String extension = switch (contentType) {
                case "image/png" -> ".png";
                case "image/jpeg" -> ".jpg";
                case "image/webp" -> ".webp";
                default -> "";
            };

            File destination = new File(directory, sku + extension);
            file.transferTo(destination);

            // Devuelve la URL como antes
            return "/" + sku + extension;

        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen: " + e.getMessage(), e);
        }
    }

    public String uploadPaymentProof(MultipartFile file, Long paymentId, Long orderId) {
        try {
            String contentType = file.getContentType();
            if (contentType == null ||
                    !(contentType.equals("image/png") ||
                            contentType.equals("image/jpeg") ||
                            contentType.equals("image/webp"))) {
                throw new RuntimeException("Formato no permitido. Solo PNG, JPEG y WEBP.");
            }

            // Carpeta de la orden
            File orderDirectory = new File(paymentsDir, "order-" + orderId);
            if (!orderDirectory.exists()) {
                orderDirectory.mkdirs();
            }

            // Nombre del archivo: payment-{paymentId}.ext
            String extension = switch (contentType) {
                case "image/png" -> ".png";
                case "image/jpeg" -> ".jpg";
                case "image/webp" -> ".webp";
                default -> "";
            };

            File destination = new File(orderDirectory, "payment-" + paymentId + extension);
            file.transferTo(destination);

            // Devuelve la URL relativa
            return "/order-" + orderId + "/payment-" + paymentId + extension;

        } catch (Exception e) {
            throw new RuntimeException("Error al subir comprobante: " + e.getMessage(), e);
        }
    }

}

