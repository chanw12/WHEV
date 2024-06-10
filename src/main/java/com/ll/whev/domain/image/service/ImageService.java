package com.ll.whev.domain.image.service;

import com.ll.whev.domain.file.FileService;
import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.domain.image.entity.Image;
import com.ll.whev.domain.image.repository.ImageRepository;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;
    private final MemberService memberService;
    private final Rq rq;

    @Transactional
    public void save(ImageSaveDto imageSaveDto) {
        String path = fileService.save(imageSaveDto.getFile());
        path = path.replace(AppConfig.getGenFileDirPath(),"");
        Image image = new Image().builder()
                .content(imageSaveDto.getContent())
                .tags(imageSaveDto.getTags())
                .path(path)
                .member(rq.getMember())
                .price(imageSaveDto.getPrice())
                .build();

        imageRepository.save(image);
    }

    public Page<Image> findAllByOrderByIdDesc(Pageable page) {
        return imageRepository.findAllImages(page);
    }

    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow();
    }

    public Page<Image> findByMemberIdOrderByIdDesc(Long MemberId,Pageable pageable) {
        return imageRepository.findByMemberIdOrderByIdDesc(MemberId,pageable);
    }


    public Page<Image> findVoteByMemberIdOrderByIdDesc(Long memberId, Pageable pageable) {
        return imageRepository.findVoteByMemberIdOrderByIdDesc(memberId,pageable);
    }

    public Page<Image> findBymemberUUID(String memberUUID, Pageable pageable) {
        return imageRepository.findBymemberUUID(memberUUID,pageable);
    }
}