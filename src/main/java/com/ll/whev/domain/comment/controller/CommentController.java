package com.ll.whev.domain.comment.controller;

import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.service.CommentService;
import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/save")
    public RsData<CommentCreateDto> createComment(CommentCreateDto commentCreateDto) {
        commentService.createComment(commentCreateDto);
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }




}
