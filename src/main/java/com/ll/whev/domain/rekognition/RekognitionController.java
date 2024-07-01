package com.ll.whev.domain.rekognition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class RekognitionController {

    @Autowired
    private RekognitionService rekognitionService;


    @PostMapping("/detect-labels")
    public List<RekognitionService.TranslatedLabel> detectLabels(@RequestParam("file") MultipartFile file) throws IOException {
        return rekognitionService.detectLabelsInImage(file);
    }
}
