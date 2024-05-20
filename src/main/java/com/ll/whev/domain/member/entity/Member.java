package com.ll.whev.domain.member.entity;

import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.table.entity.ImageVoter;
import com.ll.whev.global.jpa.entity.BaseEntity;
import com.ll.whev.standard.util.Ut;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {


    @Column(unique = true, length = 100)
    private String username;

    @Column(length = 72)
    private String password;

    @Column(unique = true, length = 50)
    private String nickname;

    @Column(unique = true)
    private String refreshToken;

    private String profileImgUrl;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Image> images;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<ImageVoter> voters;

    private String uuid;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<Comment> comments;

    private long cache;
    public String getProfileImgUrlOrDefault() {
        return Ut.str.hasLength(profileImgUrl) ? profileImgUrl : "https://placehold.co/640x640?text=O_O";
    }

    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Transient
    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        authorities.add("ROLE_MEMBER");

        if (List.of("system", "admin").contains(username)) {
            authorities.add("ROLE_ADMIN");
        }

        return authorities;
    }

    public String getName() {
        return username;
    }
}
