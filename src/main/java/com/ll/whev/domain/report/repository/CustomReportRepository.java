package com.ll.whev.domain.report.repository;

import com.ll.whev.domain.report.entity.Report;

import java.util.List;

public interface CustomReportRepository {

    public List<Report> findReportsImage();
    public List<Report> findReportsUser();

}
