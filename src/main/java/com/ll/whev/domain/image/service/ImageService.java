package com.ll.whev.domain.image.service;

import com.ll.whev.domain.file.FileService;
import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.repository.ImageRepository;
import com.ll.whev.global.rq.Rq;
import org.springframework.stereotype.Service;


@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;
    private Rq rq;

    public ImageService(ImageRepository imageRepository, FileService fileService) {
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }

    public void save(ImageSaveDto imageSaveDto) {
        String path = fileService.save(imageSaveDto.getFile());
        Image image = new Image().builder()
                .title(imageSaveDto.getTitle())
                .path(path)
                .member(rq.getMember())
                .build();

        imageRepository.save(image);
    }
}