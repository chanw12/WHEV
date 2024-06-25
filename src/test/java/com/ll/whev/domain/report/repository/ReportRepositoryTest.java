package com.ll.whev.domain.report.repository;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.repository.ImageRepository;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.repository.MemberRepository;
import com.ll.whev.domain.report.entity.Report;
import com.ll.whev.global.app.AppConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ImageRepository imageRepository;

    @MockBean
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveReport(){
        Image image = new Image();
        image.setPath("http://localhost:8090/gen/0dv9ty2024-04-11T13:48:02.354273%E1%84%80%E1%85%A1%E1%86%BC%E1%84%8E%E1%85%A1%E1%86%AB%E1%84%8B%E1%85%AE-%E1%84%8B%E1%85%A7%E1%84%80%E1%85%AF%E1%86%AB.jpeg");
        image = imageRepository.save(image);

        Member member = new Member();
        member.setUsername("chan");
        memberRepository.save(member);




        Report report = new Report();
        report.setId(1L);
        report.setImage(image);
        report.setReporter(member);
        report.setReason("홍보성 글입니다");
        report.setCreateDate(LocalDateTime.now());

        Report savedReport = reportRepository.save(report);

        Optional<Report> foundReport = reportRepository.findById(savedReport.getId());

        assertTrue(foundReport.isPresent());
        assertEquals(report.getReporter(), foundReport.get().getReporter());
        assertEquals(report.getReason(),foundReport.get().getReason());
        assertEquals(image.getId(),foundReport.get().getImage().getId());
    }


    @Test
    void testIsReport(){
        Image image = new Image();
        image.setPath("http://localhost:8090/gen/0dv9ty2024-04-11T13:48:02.354273%E1%84%80%E1%85%A1%E1%86%BC%E1%84%8E%E1%85%A1%E1%86%AB%E1%84%8B%E1%85%AE-%E1%84%8B%E1%85%A7%E1%84%80%E1%85%AF%E1%86%AB.jpeg");
        image = imageRepository.save(image);


        Member member = new Member();
        member.setUsername("chan");
        memberRepository.save(member);

        Report report = new Report();
        report.setId(1L);
        report.setImage(image);
        report.setReporter(member);
        report.setReason("홍보성 글입니다");
        report.setCreateDate(LocalDateTime.now());

        Report savedReport = reportRepository.save(report);

        Boolean isReport = reportRepository.existsReportByImageIdAndReporterId(1L,1L);

        assertTrue(isReport);
    }

}