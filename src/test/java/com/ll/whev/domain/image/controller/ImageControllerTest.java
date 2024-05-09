package com.ll.whev.domain.image.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.global.rq.Rq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = "USER")
class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;
    @MockBean
    private Rq rq;

    @MockBean
    private Member member;

    @Test
    public void testSave() throws Exception{
        ImageSaveDto imageSaveDto = new ImageSaveDto();

        ObjectMapper objectMapper = new ObjectMapper();
        String imageSaveDtoJson = objectMapper.writeValueAsString(imageSaveDto);
        MockMultipartFile file = new MockMultipartFile("imageSaveDto","", "application/json", imageSaveDtoJson.getBytes());
        when(rq.getHeader("Authorization", null)).thenReturn("Bearer dummyAccessToken");

        mockMvc.perform(multipart("/api/v1/image/save").file(file))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultCode\":\"200-0\",\"statusCode\":200,\"msg\":\"등록 성공\",\"data\":{},\"fail\":false,\"success\":true}"));
        verify(imageService, times(1)).save(any(ImageSaveDto.class));
    }


    @Test
    public void testGetImages() throws Exception {
        Image image = new Image();
        image.setId(1L); // 이미지 ID 설정
        image.setMember(member);
        image.setCreateDate(LocalDateTime.now());

        Page<Image> images = new PageImpl<>(List.of(image));
        when(imageService.findAllByOrderByIdDesc(any())).thenReturn(images);
        when(rq.getHeader("Authorization", null)).thenReturn("Bearer dummyAccessToken");
        // Act & Assert
        mockMvc.perform(get("/api/v1/image"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultCode\":\"200-1\",\"statusCode\":200,\"msg\":\"조회 성공\",\"data\":{\"items\":{\"content\":[{\"id\":1,\"member_id\":0,\"content\":null,\"path\":null,\"tags\":null}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalElements\":1,\"totalPages\":1,\"first\":true,\"size\":1,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"numberOfElements\":1,\"empty\":false}},\"fail\":false,\"success\":true}"));
        verify(imageService, times(1)).findAllByOrderByIdDesc(any());
    }


    @Test
    public void testGetImageByMemberId() throws Exception{
        Image image = new Image();
        image.setId(1L); // 이미지 ID 설정
        image.setMember(member);
        image.setCreateDate(LocalDateTime.now());
        Page<Image> images = new PageImpl<>(List.of(image));
        when(imageService.findByMemberIdOrderByIdDesc(anyLong(), any())).thenReturn(images);
        when(rq.getHeader("Authorization", null)).thenReturn("Bearer dummyAccessToken");
        // Act & Assert
        mockMvc.perform(get("/api/v1/image/0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultCode\":\"200-1\",\"statusCode\":200,\"msg\":\"조회 성공\",\"data\":{\"items\":{\"content\":[{\"id\":1,\"member_id\":0,\"content\":null,\"path\":null,\"tags\":null}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalElements\":1,\"totalPages\":1,\"first\":true,\"size\":1,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"numberOfElements\":1,\"empty\":false}},\"fail\":false,\"success\":true}"));
        verify(imageService, times(1)).findByMemberIdOrderByIdDesc(anyLong(), any());
    }
}