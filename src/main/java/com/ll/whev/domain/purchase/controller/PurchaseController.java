package com.ll.whev.domain.purchase.controller;

import com.ll.whev.domain.image.dto.ImageDto;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.purchase.entity.Purchase;
import com.ll.whev.domain.purchase.service.PurchaseService;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final ImageService imageService;
    @PostMapping("/imagepurchase")
    public RsData<ImageDto> imageDownload(@RequestParam Long imageId) {
        Image image = imageService.findById(imageId);
        purchaseService.puchaseProcess(image);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(), new ImageDto(image));
    }

    @GetMapping("/isPurchase")
    public RsData<Boolean> isPurchase(@RequestParam Long imageId) {

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(),purchaseService.findByImageIdAndMemberId(imageId));
    }
}
