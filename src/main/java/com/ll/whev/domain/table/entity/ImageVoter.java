package com.ll.whev.domain.table.entity;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "image_member")
public class ImageVoter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Image image;

    @ManyToOne
    private Member member;

    public ImageVoter() {
    }
    public ImageVoter(Image image, Member member) {
        this.image = image;
        this.member = member;
    }


}
