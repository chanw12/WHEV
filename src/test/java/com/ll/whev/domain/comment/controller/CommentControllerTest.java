package com.ll.whev.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.comment.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;



    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void createCommentTest() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setContent("test content");
        commentCreateDto.setImageId(1L);
        // Set properties for commentCreateDto


        mockMvc.perform(post("/api/v1/comment/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentCreateDto)))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}