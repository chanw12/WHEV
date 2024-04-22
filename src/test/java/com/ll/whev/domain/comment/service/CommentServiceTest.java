package com.ll.whev.domain.comment.service;

import com.ll.whev.domain.comment.Repository.CommentRepository;
import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.global.rq.Rq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
class CommentServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;
    @MockBean
    private Rq rq;

    @MockBean
    private Member member;

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    public void testCreateComment() {
        // Arrange
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setImageId(1L);
        commentCreateDto.setContent("test content");

        Image image = new Image();
        when(imageService.findById(anyLong())).thenReturn(image);
        Member member = new Member();

        when(rq.getMember()).thenReturn(member);

        // Act
        when(imageService.findById(commentCreateDto.getImageId())).thenReturn(image);
        when(rq.getMember()).thenReturn(member);

        commentService.createComment(commentCreateDto);

        // Assert

        // Act
        verify(imageService, times(1)).findById(commentCreateDto.getImageId());

        verify(rq, times(1)).getMember();
        // Assert
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}