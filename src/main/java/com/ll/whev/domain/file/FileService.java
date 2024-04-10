package com.ll.whev.domain.file;
import com.ll.whev.global.app.AppConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final Path root = Paths.get(AppConfig.genFileDirPath);


    public String save(MultipartFile file) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }

            Path path = root.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), path);

            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}