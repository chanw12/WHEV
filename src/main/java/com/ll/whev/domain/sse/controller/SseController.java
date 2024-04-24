package com.ll.whev.domain.sse.controller;

import com.ll.whev.domain.sse.service.SseService;
import com.ll.whev.global.rq.Rq;
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
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final Rq rq;

    @GetMapping("/api/sse/subscribe")
    public SseEmitter subscribe(Long id){
        SseEmitter sseEmitter = new SseEmitter();
        return sseEmitter;
    }
}
