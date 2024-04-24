package com.ll.whev.domain.sse.service;

import com.ll.whev.domain.sse.controller.SseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {


    public SseEmitter subscribe(Long id){
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SseController.sseEmitters.put(id,sseEmitter);

        sseEmitter.onCompletion(() -> SseController.sseEmitters.remove(id));
        sseEmitter.onTimeout(() -> SseController.sseEmitters.remove(id));
        sseEmitter.onError((e) -> SseController.sseEmitters.remove(id));

        return sseEmitter;
    }

}
