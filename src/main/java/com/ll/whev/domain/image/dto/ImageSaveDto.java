package com.ll.whev.domain.image.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageSaveDto {

    private String title;
    private String path;
    private MultipartFile file;
}
