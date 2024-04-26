package com.ll.whev.domain.sse.service;

import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.dto.CommentDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.sse.controller.SseController;
import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {


    public SseEmitter subscribe(Long imageId,Long memberId){
        Pair<Long,Long> key = Pair.of(imageId,memberId);
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SseController.sseEmitters.put(key ,sseEmitter);

        sseEmitter.onCompletion(() -> SseController.sseEmitters.remove(key));
        sseEmitter.onTimeout(() -> SseController.sseEmitters.remove(key));
        sseEmitter.onError((e) -> SseController.sseEmitters.remove(key));

        return sseEmitter;
    }

    public void addComment(Long imageId, Comment comment){
        CommentDto commentDto = new CommentDto(comment);
        SseController.sseEmitters.forEach((key, sseEmitter) -> {
            if (key.getLeft().equals(imageId)) {
                try {
                    sseEmitter.send(SseEmitter.event().name("addComment").data(commentDto));
                } catch (Exception e) {
                    SseController.sseEmitters.remove(key);
                }
            }
        });
    }

}


