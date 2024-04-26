package com.ll.whev.domain.sse.controller;

import com.ll.whev.domain.sse.service.SseService;
import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;
    public static Map<Pair<Long,Long>, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping("/api/sse/subscribe")
    public SseEmitter subscribe(Long imageId,Long memberId){
        SseEmitter sseEmitter = sseService.subscribe(imageId,memberId);
        return sseEmitter;
    }
}
