package com.ll.whev.domain.report.dto;

import com.ll.whev.domain.report.entity.Report;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportResDTO {

    @NonNull
    private String reason;

    private Long reporterId;
    @NonNull
    private Long imageId;

    private Long reportedMemberId;

    @NonNull
    private LocalDateTime createDate;


    public ReportResDTO(Report report){
        this.reason = report.getReason();
        this.reporterId = report.getReporter().getId();
        this.imageId  = (report.getImage() != null) ? report.getImage().getId() : null;
        this.reportedMemberId = (report.getReportedUser() != null) ? report.getReportedUser().getId() : null;
        this.createDate = report.getCreateDate();
    }


}
