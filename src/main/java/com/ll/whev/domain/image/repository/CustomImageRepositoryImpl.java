package com.ll.whev.domain.image.repository;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.entity.QImage;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ll.whev.domain.image.entity.QImage.image;

@RequiredArgsConstructor
public class CustomImageRepositoryImpl implements CustomImageRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Image> findAllImages(Pageable pageable) {
        List<Image> fetch = jpaQueryFactory.selectFrom(image)
                .orderBy(image.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = jpaQueryFactory.select(image)
                .from(image)
                .fetchCount();
        return new PageImpl<>(fetch,pageable, count);
    }

    @Override
    public Page<Image> findByMemberIdOrderByIdDesc(Long memberId, Pageable pageable) {
        List<Image> fetch = jpaQueryFactory.selectFrom(image)
                .where(image.member.id.eq(memberId))
                .orderBy(image.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = jpaQueryFactory.select(image)
                .from(image)
                .fetchCount();
        return new PageImpl<>(fetch,pageable, count);
    }

    @Override
    public Page<Image> findVoteByMemberIdOrderByIdDesc(Long memberId, Pageable pageable) {
        List<Image> fetch = jpaQueryFactory.selectFrom(image)
                .where(image.voters.any().member.id.eq(memberId))
                .orderBy(image.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long count = jpaQueryFactory.select(image)
                .from(image)
                .fetchCount();

        return new PageImpl<>(fetch,pageable, count);
    }
}
