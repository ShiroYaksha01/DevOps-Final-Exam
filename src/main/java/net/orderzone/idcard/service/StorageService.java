package net.orderzone.idcard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation;

    public StorageService(@Value("${idcard.storage.location:uploads}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation);
        init();
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new RuntimeException("Invalid file type. Only JPEG and PNG are allowed.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + extension;

        try {
            if (newFilename.contains("..")) {
                throw new RuntimeException("Cannot store file with relative path outside current directory " + newFilename);
            }
            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);
            }
            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
}
