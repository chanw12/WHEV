package com.ll.whev.domain.image.controller;

import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/save")
    public RsData<ImageSaveDto> save(@ModelAttribute ImageSaveDto imageSaveDto){
        System.out.println("imageSaveDto = " + imageSaveDto.getFile());
        imageService.save(imageSaveDto);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

}
