package com.ll.whev.domain.member.dto;


import com.ll.whev.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String name;
    @NonNull
    private String username;
    @NonNull
    private String profileImgUrl;
    @NonNull
    private List<String> authorities;

    @NonNull
    private long cache;

    private String uuid;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.name = member.getNickname();
        this.username = member.getUsername();
        this.profileImgUrl = member.getProfileImgUrlOrDefault();
        this.authorities = member.getAuthoritiesAsStringList();
        this.uuid = member.getUuid();
        this.cache = member.getCache();
    }
}