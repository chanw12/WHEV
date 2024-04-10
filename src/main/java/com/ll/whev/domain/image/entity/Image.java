package com.ll.whev.domain.image.entity;

import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString(callSuper = true)
public class Image extends BaseEntity {

    private String title;

    @ManyToOne
    private Member member;

    private String path;


    public void setPath(String path) {
            this.path = path;
        }

}
