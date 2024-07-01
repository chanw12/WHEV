package com.ll.whev.domain.report.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportDto {

    @NonNull
    private String reason;

    @NonNull
    private Long memberId;
    @NonNull
    private Long imageId;
}
