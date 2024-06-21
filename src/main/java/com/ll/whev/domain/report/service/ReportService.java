package com.ll.whev.domain.report.service;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.repository.ImageRepository;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.repository.MemberRepository;
import com.ll.whev.domain.report.entity.Report;
import com.ll.whev.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public Report save(Long imageId, Long memberId, String reason) {
        System.out.println("Fetching image with ID: " + imageId);
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid image ID"));

        System.out.println("Fetching member with ID: " + memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Report report = new Report(image, member, reason);

        return reportRepository.save(report);
    }

    public Boolean isReport(Long imageId,Long memberId) {
        return reportRepository.existsReportByImageIdAndMemberId(imageId,memberId);

    }
}
