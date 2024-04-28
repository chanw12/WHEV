package com.ll.whev.domain.image.dto;

import com.ll.whev.domain.image.entity.Image;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class ImageDto {
    @NonNull
    private Long id;
    @NonNull
    private Long member_id;
    @NonNull
    private String content;
    @NonNull
    private String path;
    @NonNull
    private String tags;
    @NonNull
    private String member_nickname;
    @NonNull
    private String date;

    public ImageDto(Image image) {
        this.id = image.getId();
        this.member_id = image.getMember().getId();
        this.content = image.getContent();
        this.path = image.getPath();
        this.tags = image.getTags();
        this.member_nickname = image.getMember().getNickname();
        this.date = image.getCreateDate().toString();
    }
}
