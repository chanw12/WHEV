package com.ll.whev.domain.comment.controller;

import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.dto.CommentDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.comment.service.CommentService;
import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/save")
    public RsData<CommentCreateDto> createComment(@RequestBody CommentCreateDto commentCreateDto) {
        commentService.createComment(commentCreateDto);
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/get")
    @Operation(summary = "댓글 조회")
    public RsData<List<CommentDto>> getComment(@RequestParam Long imageId) {
        List<CommentDto> comments = commentService.getComment(imageId);
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(),comments);
    }


}
