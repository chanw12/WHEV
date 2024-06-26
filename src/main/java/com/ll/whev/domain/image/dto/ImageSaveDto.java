package com.ll.whev.domain.image.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageSaveDto {

    private String content;
    private String path;
    private MultipartFile file;
    private String tags;
    private int price;
}
