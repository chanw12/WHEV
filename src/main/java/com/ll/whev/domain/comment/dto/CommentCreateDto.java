package com.ll.whev.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDto {
    private String content;
    private Long ImageId;

    public CommentCreateDto() {
    }
}
