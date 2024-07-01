package com.ll.whev.domain.admin.controller;

import com.ll.whev.domain.report.dto.ReportResDTO;
import com.ll.whev.domain.report.entity.Report;
import com.ll.whev.domain.report.service.ReportService;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final ReportService reportService;

    public record getDeviceResponseBody(@NonNull Boolean isMobile){

    }
    @GetMapping("/deviceCheck")
    public RsData<getDeviceResponseBody> getDevice(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        boolean isMobile = userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("ipad");

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), new getDeviceResponseBody(isMobile));
    }

    @GetMapping(value = "/reports/list")
    @Operation(summary = "신고 이미지 목록")
    public RsData<List<ReportResDTO>> getAllReportedPosts() {

        List<Report> reportedPosts = reportService.getImageReports();

        List<ReportResDTO> reportedPostList = reportedPosts.stream()
                .map(ReportResDTO::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                reportedPostList
        );
    }





}
