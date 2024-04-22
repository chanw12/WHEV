package com.ll.whev.domain.image.entity;

import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString(callSuper = true)
public class Image extends BaseEntity {


    @ManyToOne
    private Member member;

    @Setter
    private String path;

    private String content;

    private String tags;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE)
    private Set<Comment> comments;


    public void setId(Long id){
        this.id = id;
    }

}
