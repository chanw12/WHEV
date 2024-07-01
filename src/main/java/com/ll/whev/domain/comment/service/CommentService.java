package com.ll.whev.domain.comment.service;

import com.ll.whev.domain.comment.Repository.CommentRepository;
import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.dto.CommentDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.sse.controller.SseController;
import com.ll.whev.domain.sse.service.SseService;
import com.ll.whev.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final Rq rq;
    private final SseService sseService;

    @Transactional
    public void createComment(CommentCreateDto commentCreateDto) {
        Comment comment = Comment.builder().image(imageService.findById(commentCreateDto.getImageId())).content(commentCreateDto.getContent()).member(rq.getMember()).build();
        commentRepository.save(comment);
        sseService.addComment(commentCreateDto.getImageId(),comment);

    }


    public List<CommentDto> getComment(Long imageid) {
        List<Comment> commentsByImageId = commentRepository.getCommentsByImageId(imageid);
        List<CommentDto> list = commentsByImageId.stream().map(CommentDto::new).toList();
        return list;
    }
}
