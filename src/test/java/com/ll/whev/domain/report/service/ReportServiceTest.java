package com.ll.whev.domain.report.service;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.repository.ImageRepository;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.repository.MemberRepository;
import com.ll.whev.domain.report.entity.Report;
import com.ll.whev.domain.report.repository.ReportRepository;
import com.ll.whev.domain.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageService imageService;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReportService reportService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService(reportRepository, memberRepository, imageRepository,imageService);
    }

    @Test
    void testCreateReport() {
        // 새로운 이미지 생성 및 목 객체 설정
        Image image = new Image();
        image.setId(1L);
        image.setPath("http://example.com/image.jpg");
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        // 새로운 회원 생성 및 목 객체 설정
        Member member = new Member();
        member.setId(1L); // id 수동 설정
        member.setUsername("testUser");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // 새로운 신고 생성 및 목 객체 설정
        Report report = new Report(image, member, "Inappropriate content");

        // Repository의 save 메서드 모킹
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        // 서비스 메서드 호출 전 로그 출력
        System.out.println("Calling reportService.save");
        Report savedReport = reportService.save(1L, 1L, "Inappropriate content");

        // Mockito 검증
        verify(imageRepository).findById(1L);
        verify(memberRepository).findById(1L);

        // 결과 검증
        assertNotNull(savedReport);
        assertEquals(member.getId(), savedReport.getMember().getId());
        assertEquals("Inappropriate content", savedReport.getReason());
        assertEquals(image.getId(), savedReport.getImage().getId());
    }

    @Test
    public void testIsReportExists() {
        Long imageId = 1L;
        Long memberId = 1L;

        // Mock the behavior of the reportRepository
        when(reportRepository.existsReportByImageIdAndMemberId(imageId,memberId)).thenReturn(true);

        // Call the method to test
        Boolean result = reportService.isReport(imageId,memberId);

        // Verify the result
        assertTrue(result);
    }

    @Test
    public void testIsReportNotExists() {
        Long imageId = 2L;
        Long memberId = 2L;

        // Mock the behavior of the reportRepository
        when(reportRepository.existsReportByImageIdAndMemberId(imageId,memberId)).thenReturn(false);

        // Call the method to test
        Boolean result = reportService.isReport(imageId,memberId);

        // Verify the result
        assertFalse(result);
    }

}
