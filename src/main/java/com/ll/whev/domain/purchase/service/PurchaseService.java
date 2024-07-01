package com.ll.whev.domain.purchase.service;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.domain.purchase.entity.Purchase;
import com.ll.whev.domain.purchase.repository.PurchaseRepository;
import com.ll.whev.domain.sse.service.SseService;
import com.ll.whev.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final MemberService memberService;
    private final Rq rq;
    private final PurchaseRepository purchaseRepository;
    private final SseService sseService;
    @Transactional
    public void puchaseProcess(Image image) {
        memberService.minuscache(rq.getMember(),image.getPrice());
        memberService.pluscache(image.getMember(),image.getPrice());
        sseService.updatePoints(rq.getMember());
        sseService.updatePoints(image.getMember());
        image.setPurchase_count(image.getPurchase_count()+1);
        Purchase purchase = new Purchase().builder()
                .member(rq.getMember())
                .image(image)
                .build();
        purchaseRepository.save(purchase);
    }


    public Boolean findByImageIdAndMemberId(Long imageId) {
        Purchase purchase = purchaseRepository.findByImage_IdAndMember_Id(imageId, rq.getMember().getId()).orElse(null);
        return purchase != null;
    }
}
