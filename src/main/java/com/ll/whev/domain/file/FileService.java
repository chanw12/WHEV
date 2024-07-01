package com.ll.whev.domain.file;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DependsOn("appConfig")
public class FileService {

    private final Path root;

    public FileService() {
        if (AppConfig.genFileDirPath == null) {
            throw new IllegalArgumentException("genFileDirPath in AppConfig is null");
        }
        this.root = Paths.get(AppConfig.genFileDirPath);
    }

    public String save(MultipartFile file) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
            String filename = Ut.generateRandomString() + LocalDateTime.now().toString()+file.getOriginalFilename();

            Path path = root.resolve(filename);
            Files.copy(file.getInputStream(), path);

            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}