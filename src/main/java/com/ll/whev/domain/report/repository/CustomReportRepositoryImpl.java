package com.ll.whev.domain.report.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomReportRepositoryImpl implements CustomReportRepository{

    private final JPAQueryFactory jpaQueryFactory;
}
