package org.example.coffeeshop.service;

import org.example.coffeeshop.exception.InvalidFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.unit.DataSize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    private FileStorageService newService() throws IOException {
        return new FileStorageService(
                tempDir.toString(),
                DataSize.ofMegabytes(5),
                "image/jpeg,image/png,image/gif,image/webp");
    }

    private MockMultipartFile createPng(String filename) throws IOException {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return new MockMultipartFile("image", filename, "image/png", out.toByteArray());
    }

    @Test
    @DisplayName("storeFile should save valid images to disk")
    void storeFile_shouldSaveImage() throws IOException {
        FileStorageService service = newService();
        MockMultipartFile file = createPng("coffee.png");

        String filename = service.storeFile(file);

        assertThat(filename).isNotNull();
        Path stored = tempDir.resolve(filename);
        assertThat(Files.exists(stored)).isTrue();
    }

    @Test
    @DisplayName("storeFile should reject invalid content types")
    void storeFile_shouldRejectInvalidType() throws IOException {
        FileStorageService service = newService();
        MockMultipartFile file = new MockMultipartFile(
                "image", "hack.exe", "application/x-msdownload", "data".getBytes());

        assertThatThrownBy(() -> service.storeFile(file))
                .isInstanceOf(InvalidFileException.class);
    }

    @Test
    @DisplayName("storeFile should reject oversized files")
    void storeFile_shouldRejectOversize() throws IOException {
        FileStorageService service = newService();
        byte[] payload = new byte[(int) DataSize.ofMegabytes(6).toBytes()];
        MockMultipartFile file = new MockMultipartFile(
                "image", "big.jpg", "image/jpeg", payload);

        assertThatThrownBy(() -> service.storeFile(file))
                .isInstanceOf(InvalidFileException.class);
    }
}
