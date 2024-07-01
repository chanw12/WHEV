package com.ll.whev.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDto {
    private String content;
    private Long imageId;

    public CommentCreateDto() {
    }
    public CommentCreateDto(String content,Long imageId) {
        this.content = content;
        this.imageId = imageId;
    }
}
