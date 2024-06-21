package com.ll.whev.domain.report.controller;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.domain.report.dto.ReportDto;
import com.ll.whev.domain.report.entity.Report;
import com.ll.whev.domain.report.service.ReportService;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import com.ll.whev.standard.base.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    @PostMapping("/save")
    public RsData<Empty> createReport(@RequestBody ReportDto reportDto){
        reportService.save(reportDto.getImageId(),reportDto.getMemberId(),reportDto.getReason());
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),Msg.E200_1_INQUIRY_SUCCEED.getMsg());
    }

    @GetMapping("/isReport")
    public RsData<Boolean> isReport(@RequestParam Long imageId){
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),Msg.E200_1_INQUIRY_SUCCEED.getMsg(),reportService.isReport(imageId));
    }

}
