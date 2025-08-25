package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.service.impl.UploadServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final UploadServiceImpl uploadService;

    public UploadController(UploadServiceImpl uploadService) {
        this.uploadService = uploadService;
    }

    // Ahora recibe también el SKU del libro
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sku") String sku) {

        String imageUrl = uploadService.uploadImage(file, sku);

        return ResponseEntity.ok(imageUrl);
    }
}
