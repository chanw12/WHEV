package com.ll.whev.domain.report.dto;

import com.ll.whev.domain.report.entity.Report;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
public class ReportResDTO {

    @NonNull
    private String reason;

    @NonNull
    private Long memberId;
    @NonNull
    private Long imageId;

    public ReportResDTO(Report report){
        this.reason = report.getReason();
        this.memberId = report.getMember().getId();
        this.imageId = report.getImage().getId();
    }


}
