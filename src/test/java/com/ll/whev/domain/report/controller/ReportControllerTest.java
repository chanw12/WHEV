package com.ll.whev.domain.report.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.domain.report.dto.ReportDto;
import com.ll.whev.domain.report.service.ReportService;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rq.Rq;
import com.ll.whev.global.rsData.RsData;
import com.ll.whev.global.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ReportController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class}))
@ActiveProfiles("test")
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private Rq rq;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Mockito 설정
        when(reportService.save(anyLong(), anyLong(), Mockito.anyString()))
                .thenReturn(null);  // 메서드가 void가 아닌 경우, 적절한 값을 반환하도록 설정
    }

    @Test
    @WithMockUser  // 인증된 사용자를 모의
    @DisplayName("POST report 생성 요청")
    public void testCreateReport() throws Exception {
        ReportDto reportDto = new ReportDto();
        reportDto.setImageId(1L);
        reportDto.setMemberId(2L);
        reportDto.setReason("Test Reason");

        // 요청 본문 설정
        String requestJson = objectMapper.writeValueAsString(reportDto);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/report/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))  // CSRF 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode", is(Msg.E200_0_CREATE_SUCCEED.getCode())))
                .andExpect(jsonPath("$.msg", is(Msg.E200_1_INQUIRY_SUCCEED.getMsg())));

        // Print the response content
        MvcResult result = resultActions.andReturn();
        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @Test
    @WithMockUser
    public void testIsReportExists() throws Exception {
        Long imageId = 1L;
        when(reportService.isReport(anyLong())).thenReturn(true);

        mockMvc.perform(get("/api/v1/report/isReport")
                        .param("imageId", imageId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), true))));
    }

    @Test
    @WithMockUser
    public void testIsReportNotExists() throws Exception {
        Long imageId = 2L;
        when(reportService.isReport(anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/v1/report/isReport")
                        .param("imageId", imageId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), false))));
    }
}
