package com.ll.whev.domain.comment.dto;

import com.ll.whev.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private String content;
    private Long commentId;
    private String memberName;
    private int vote;
    private String createDate;

    public CommentDto(Comment comment){
        this.content = comment.getContent();
        this.commentId = comment.getId();
        this.memberName = comment.getMember().getName();
        this.vote = comment.getVote();
        this.createDate = comment.getCreateDate().toString();
    }
}
