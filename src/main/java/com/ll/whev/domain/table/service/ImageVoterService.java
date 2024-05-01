package com.ll.whev.domain.table.service;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.domain.table.entity.ImageVoter;
import com.ll.whev.domain.table.repository.ImageVoterRepository;
import com.ll.whev.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageVoterService {
    private final ImageVoterRepository imageVoterRepository;
    private final ImageService imageService;
    private final MemberService memberService;
    private final Rq rq;
    @Transactional
    public void vote(Long imageId) {
        // 이미지에 투표한 사용자인지 확인
        // 이미지에 투표한 사용자라면 투표 취소
        // 이미지에 투표한 사용자가 아니라면 투표
//        Member member = memberService.findById(memberId);
        Image image = imageService.findById(imageId);
        ImageVoter imageVoter = new ImageVoter(image, rq.getMember());
        System.out.println("service : " + rq.getMember());
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        if (imageVoterRepository.existsByImageAndMember(image, rq.getMember())) {
            imageVoterRepository.deleteByImageAndMember(image, rq.getMember());
        }
        else {
            imageVoterRepository.save(imageVoter);
        }
    }
}
