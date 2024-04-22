package com.ll.whev.domain.comment.controller;

import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private CommentService commentService;
    @PostMapping("")
    public void createComment(CommentCreateDto commentCreateDto) {

        commentService.createComment(commentCreateDto);
    }

}
