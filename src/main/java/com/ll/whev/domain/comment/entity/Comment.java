package com.ll.whev.domain.comment.entity;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Comment extends BaseEntity {


        private String content;

        @ManyToOne
        private Image image;

        @ManyToOne
        private Member member;

        private int vote;

        public void addVote() {
            this.vote++;
        }

        public void removeVote() {
            this.vote--;
        }




}
