package com.ll.whev.domain.image.controller;

import com.ll.whev.domain.image.dto.ImageDto;
import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.table.service.ImageVoterService;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rq.Rq;
import com.ll.whev.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
    private final ImageService imageService;
    private final Rq rq;
    private final ImageVoterService imageVoterService;

    @PostMapping("/save")
    public RsData<ImageSaveDto> save(@ModelAttribute ImageSaveDto imageSaveDto) {
        imageService.save(imageSaveDto);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    public record GetImagesResponseBody(Page<ImageDto> items) {
    }

    @GetMapping("")
    public RsData<GetImagesResponseBody> getImages(@RequestParam(defaultValue = "1") int page) {
        List<Sort.Order> sorts = List.of(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Image> allByOrderByIdDesc = imageService.findAllByOrderByIdDesc(pageable);
        Page<ImageDto> imageDtos = allByOrderByIdDesc.map(ImageDto::new);
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetImagesResponseBody(imageDtos));
    }

    @PutMapping("/vote")
    public RsData<ImageDto> vote(@RequestParam Long imageId) {
        Image image = imageService.findById(imageId);
        System.out.println("controller : " + rq.getMember());
        imageVoterService.vote(imageId);
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(), new ImageDto(image));
    }

    @GetMapping("/vote/isvote")
    public RsData<Boolean> isVote(@RequestParam Long imageId) {
        boolean isVote = imageVoterService.isVote(imageId);
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(), isVote);
    }

}