package com.ll.whev.domain.table.repository;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.table.entity.ImageVoter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageVoterRepository extends JpaRepository<ImageVoter,Long> {

    boolean existsByImageAndMember(Image image, Member member);

    void deleteByImageAndMember(Image image, Member member);
}
