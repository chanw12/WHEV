package com.ll.whev.domain.admin.controller;

import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    public record getDeviceResponseBody(@NonNull Boolean isMobile){

    }
    @GetMapping("/deviceCheck")
    public RsData<getDeviceResponseBody> getDevice(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        boolean isMobile = userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("ipad");

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), new getDeviceResponseBody(isMobile));
    }
}
