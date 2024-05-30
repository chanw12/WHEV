package com.ll.whev.domain.sse.service;

import com.ll.whev.domain.comment.dto.CommentCreateDto;
import com.ll.whev.domain.comment.dto.CommentDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.member.dto.MemberDto;
import com.ll.whev.domain.member.entity.Member;
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
        SseController.ImageCommentSseEmitters.put(key ,sseEmitter);

        sseEmitter.onCompletion(() -> SseController.ImageCommentSseEmitters.remove(key));
        sseEmitter.onTimeout(() -> SseController.ImageCommentSseEmitters.remove(key));
        sseEmitter.onError((e) -> SseController.ImageCommentSseEmitters.remove(key));

        return sseEmitter;
    }

    public void addComment(Long imageId, Comment comment){
        CommentDto commentDto = new CommentDto(comment);
        SseController.ImageCommentSseEmitters.forEach((key, sseEmitter) -> {
            if (key.getLeft().equals(imageId)) {
                try {
                    sseEmitter.send(SseEmitter.event().name("addComment").data(commentDto));
                } catch (Exception e) {
                    SseController.ImageCommentSseEmitters.remove(key);
                }
            }
        });
    }

    public SseEmitter subscribe(Long memberId) {
        Long key = memberId;
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SseController.LoginSseEmitters.put(key ,sseEmitter);
        System.out.println("subscribe--------chan---------");

        sseEmitter.onCompletion(() -> SseController.LoginSseEmitters.remove(key));
        sseEmitter.onTimeout(() -> SseController.LoginSseEmitters.remove(key));
        sseEmitter.onError((e) -> SseController.LoginSseEmitters.remove(key));

        return sseEmitter;
    }


    public void updatePoints(Member member){
        MemberDto memberDto = new MemberDto(member);
        System.out.println("updatePoints--------chan---------");
        SseController.LoginSseEmitters.forEach((key, sseEmitter) -> {
            if (key.equals(member.getId())) {
                try {
                    sseEmitter.send(SseEmitter.event().name("updatePoints").data(memberDto));
                } catch (Exception e) {
                    SseController.LoginSseEmitters.remove(key);
                }
            }
        });
    }
}


