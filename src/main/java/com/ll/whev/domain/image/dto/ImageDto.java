package com.ll.whev.domain.image.dto;

import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.table.entity.ImageVoter;
import com.ll.whev.global.jpa.entity.BaseEntity;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @NonNull
    private int price;
    @NonNull
    private int downloadCount;

    @NonNull
    private Set<Long> voters;


    public ImageDto(Image image) {
        this.id = image.getId();
        this.member_id = image.getMember().getId();
        this.content = image.getContent();
        this.path = image.getPath();
        this.tags = image.getTags();
        this.member_nickname = image.getMember().getNickname();
        this.date = image.getCreateDate().toString();
        this.price= image.getPrice();
        this.downloadCount = image.getDownloadCount();
        this.voters = image.getVoters().stream().map(ImageVoter::getMember).map(BaseEntity::getId).collect(Collectors.toSet());
    }
}
