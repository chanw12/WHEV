package com.ll.whev.domain.comment.service;

import com.ll.whev.domain.comment.Repository.CommentRepository;
import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final Rq rq;
    public void createComment(CommentCreateDto commentCreateDto) {
        Comment comment = Comment.builder().image(imageService.findById(commentCreateDto.getImageId())).content(commentCreateDto.getContent()).member(rq.getMember()).build();
        commentRepository.save(comment);
    }
}
