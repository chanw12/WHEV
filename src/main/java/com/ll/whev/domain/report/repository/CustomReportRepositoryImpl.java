package com.ll.whev.domain.report.repository;

import com.ll.whev.domain.report.entity.QReport;
import com.ll.whev.domain.report.entity.Report;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.whev.domain.report.entity.QReport.*;

@RequiredArgsConstructor
public class CustomReportRepositoryImpl implements CustomReportRepository{


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Report> findReportsImage() {


        List<Report> fetch = jpaQueryFactory.selectFrom(report)
                .where(report.image.isNotNull())
                .fetch();



        return fetch;
    }

    @Override
    public List<Report> findReportsUser() {


        List<Report> fetch = jpaQueryFactory.selectFrom(report)
                .where(report.reportedUser.isNotNull())
                .fetch();



        return fetch;
    }
}
