package org.example.coffeeshop.controller.api;

import org.example.coffeeshop.service.FileStorageService;
import org.example.coffeeshop.service.MenuService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Controller for serving images stored in the uploads directory.
 * Provides public endpoints for category and menu item images.
 */
@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final MenuService menuService;
    private final FileStorageService fileStorageService;

    public ImageController(MenuService menuService, FileStorageService fileStorageService) {
        this.menuService = menuService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable Long id) {
        return menuService.getCategory(id)
                .map(category -> buildImageResponse(category.getImageFilename()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<byte[]> getItemImage(@PathVariable Long id) {
        return menuService.getMenuItem(id)
                .map(item -> buildImageResponse(item.getImageFilename()))
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<byte[]> buildImageResponse(String filename) {
        if (!StringUtils.hasText(filename)) {
            return ResponseEntity.notFound().build();
        }
        byte[] data;
        try {
            data = fileStorageService.loadFile(filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (data == null || data.length == 0) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic());
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
