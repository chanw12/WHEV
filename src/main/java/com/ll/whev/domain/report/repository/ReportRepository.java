package com.ll.whev.domain.report.repository;

import com.ll.whev.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long>,CustomReportRepository {
    public Boolean existsReportByImageIdAndReporterId(Long imageId,Long memberId);
}
