package com.ll.whev.domain.purchase.dto;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.purchase.entity.Purchase;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class PurchaseDto {
    @NonNull
    private Long id;
    @NonNull
    private Long member_id;
    @NonNull
    private Long image_id;
    private LocalDateTime createDate;


    public PurchaseDto(Purchase purchase) {
        this.id = purchase.getId();
        this.member_id = purchase.getMember().getId();
        this.image_id = purchase.getImage().getId();
        this.createDate = purchase.getCreateDate();

    }
}
