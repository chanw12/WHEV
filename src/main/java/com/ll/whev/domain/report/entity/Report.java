package com.ll.whev.domain.report.entity;


import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString(callSuper = true)
public class Report extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String reason;


}
