package com.ll.whev.domain.member.repository;

import com.ll.whev.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByRefreshToken(String refreshToken);

    List<Member> findAll();

    List<Member> findTop5ByOrderByIdDesc();

    Optional<Member> findByUuid(String uuid);

    Boolean existsMemberByUuid(String uuid);

}
