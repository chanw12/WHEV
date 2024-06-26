package com.ll.whev.domain.image.entity;

import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.table.entity.ImageVoter;
import com.ll.whev.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

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

    private int price;

    @Builder.Default
    private int purchase_count = 0;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE)
    private Set<ImageVoter> voters;


    public void setId(Long id){
        this.id = id;
    }

}
