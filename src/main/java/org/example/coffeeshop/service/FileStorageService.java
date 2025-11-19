package org.example.coffeeshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

@Service
public class FileStorageService {
    private final Path uploadDir;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private static final float JPEG_QUALITY = 0.85f;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) throws IOException {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        try (InputStream is = file.getInputStream()) {
            BufferedImage src = ImageIO.read(is);
            if (src == null) {
                throw new IllegalArgumentException("Uploaded file is not a valid image");
            }
            BufferedImage resized = resizeToFit(src, MAX_WIDTH, MAX_HEIGHT);
            String filename = UUID.randomUUID().toString() + ".jpg";
            Path target = this.uploadDir.resolve(filename).normalize();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(Files.newOutputStream(target, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(JPEG_QUALITY);
                }
                writer.setOutput(ios);
                writer.write(null, new IIOImage(resized, null, null), param);
                writer.dispose();
            }
            return filename;
        }
    }

    public boolean deleteFile(String filename) {
        if (filename == null) return false;
        try {
            Path target = this.uploadDir.resolve(filename).normalize();
            return Files.deleteIfExists(target);
        } catch (IOException e) {
            return false;
        }
    }

    private BufferedImage resizeToFit(BufferedImage src, int maxW, int maxH) {
        int w = src.getWidth();
        int h = src.getHeight();
        double scale = Math.min((double) maxW / w, (double) maxH / h);
        if (scale >= 1.0) {
            if (src.getType() == BufferedImage.TYPE_INT_RGB) return src;
            BufferedImage copy = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = copy.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(src, 0, 0, null);
            g.dispose();
            return copy;
        }
        int newW = (int) Math.round(w * scale);
        int newH = (int) Math.round(h * scale);
        BufferedImage out = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, newW, newH);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src, 0, 0, newW, newH, null);
        g.dispose();
        return out;
    }
}
