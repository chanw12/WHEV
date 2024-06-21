package com.ll.whev.domain.report.repository;

import com.ll.whev.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
    public Boolean existsReportByImageIdAndMemberId(Long imageId,Long memberId);
}
