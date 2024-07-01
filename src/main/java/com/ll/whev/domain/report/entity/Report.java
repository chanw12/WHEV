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
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "reportedMember_id_")
    private Member reportedUser;

    private String reason;


    public Report(Image image, Member reporter,String reason){
        this.image = image;
        this.reason = reason;
        this.reporter = reporter;
    }
    public Report(Member reporter,Member reportedUser, String reason){
        this.reporter = reporter;
        this.reason = reason;
        this.reportedUser = reportedUser;
    }

}
