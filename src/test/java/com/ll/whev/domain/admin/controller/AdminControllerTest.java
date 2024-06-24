package com.ll.whev.domain.admin.controller;

import com.ll.whev.domain.comment.service.CommentService;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.domain.report.controller.ReportController;
import com.ll.whev.domain.report.dto.ReportResDTO;
import com.ll.whev.domain.report.entity.Report;
import com.ll.whev.domain.report.service.ReportService;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rq.Rq;
import com.ll.whev.global.rsData.RsData;
import com.ll.whev.global.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static com.ll.whev.global.app.AppConfig.objectMapper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ReportService reportService;

    @MockBean
    private MemberService memberService;

    @Test
    void testGetDeviceForMobile() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/deviceCheck")
                        .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.isMobile").value(true));
    }

    @Test
    public void testGetDeviceForDesktop() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/deviceCheck")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.isMobile").value(false));
    }

    @Test
    public void testGetReportList() throws Exception {
        Member member = new Member();
        member.setId(1L);
        Image image = new Image();
        image.setId(1L);

        List<Report> mockReports = Arrays.asList(
                new Report(image,member,"사유 1"),
                new Report(image,member,"사유 2"),
                new Report(image,member,"사유 3")
        );
        Mockito.when(reportService.getReports()).thenReturn(mockReports);
        List<ReportResDTO> list = mockReports.stream()
                .map(ReportResDTO::new)
                .toList();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/reports/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), list))));

    }

}