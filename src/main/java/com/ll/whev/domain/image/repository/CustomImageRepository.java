package com.ll.whev.domain.image.repository;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.standard.base.KwTypeCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomImageRepository {
    Page<Image> findAllImages(Pageable pageable);

    Page<Image> findByMemberIdOrderByIdDesc(Long memberId, Pageable pageable);

    Page<Image> findVoteByMemberIdOrderByIdDesc(Long memberId, Pageable pageable);
}
