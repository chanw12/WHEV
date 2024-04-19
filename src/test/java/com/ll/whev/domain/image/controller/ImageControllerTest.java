package com.ll.whev.domain.image.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
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
    public void testGetImages() throws Exception {
        Image image = new Image();
        image.setId(1L); // 이미지 ID 설정
        image.setMember(member);
        Page<Image> images = new PageImpl<>(List.of(image));
        when(imageService.findAllByOrderByIdDesc(any())).thenReturn(images);

        // Act & Assert
        mockMvc.perform(get("/api/v1/image"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultCode\":\"200-1\",\"statusCode\":200,\"msg\":\"조회 성공\",\"data\":{\"items\":{\"content\":[{\"id\":1,\"member_id\":0,\"content\":null,\"path\":null,\"tags\":null}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalElements\":1,\"totalPages\":1,\"first\":true,\"size\":1,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"numberOfElements\":1,\"empty\":false}},\"fail\":false,\"success\":true}"));
        verify(imageService, times(1)).findAllByOrderByIdDesc(any());
    }

}