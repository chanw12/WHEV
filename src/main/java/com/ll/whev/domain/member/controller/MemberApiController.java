package com.ll.whev.domain.member.controller;

import com.ll.whev.domain.member.dto.MemberDto;
import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rq.Rq;
import com.ll.whev.global.rsData.RsData;
import com.ll.whev.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController {
    private final Rq rq;
    private final MemberService memberService;

    public record MeResponseBody(@NonNull MemberDto item) {
    }
    @GetMapping("/me")
    public RsData<MeResponseBody> getMe() {
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new MeResponseBody(
                        new MemberDto(rq.getMember())
                )
        );
    }
    @PostMapping("/logout")
    public RsData<Empty> logout() {
        rq.setLogout();

        return RsData.of(Msg.E200_6_LOGOUT_SUCCEED.getCode(),
                Msg.E200_6_LOGOUT_SUCCEED.getMsg());
    }
    @PostMapping("/minusecache")
    public RsData<Empty> minuscache(@RequestParam int cache){
        try {
            Member member = rq.getMember();
            memberService.minuscache(member,cache);
            return RsData.of(Msg.E200_9_MINUS_CACHE_SUCCEED.getCode(),Msg.E200_9_MINUS_CACHE_SUCCEED.getMsg());
        } catch (IllegalArgumentException e) {
            return RsData.of(Msg.E200_9_MINUS_CACHE_SUCCEED.getCode(),Msg.E200_9_MINUS_CACHE_SUCCEED.getMsg());
        }
    }

    public record LoginResponseBody(@NonNull MemberDto item) {
    }

    public record LoginRequestBody(@NotBlank String username, @NotBlank String password) {
    }

    @PostMapping(value = "/login")
    @Operation(summary = "로그인, accessToken, refreshToken 쿠키 생성됨")
    public RsData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody body) {
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username,
                body.password
        );

        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());

        return authAndMakeTokensRs.newDataOf(
                new LoginResponseBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }
    public record isAdminResponseBody(@NonNull Boolean isAdmin){
    }

    @GetMapping("/isAdmin")
    @Operation(summary = "관리자 여부 확인")
    public RsData<isAdminResponseBody> isAdmin(){

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),Msg.E200_1_INQUIRY_SUCCEED.getMsg(),new isAdminResponseBody(rq.isAdmin()));
    }

    public record isLoginResponseBody(@NonNull Boolean isLogin){
    }

    @GetMapping("/isLogin")
    @Operation(summary = "로그인 여부 확인")
    public RsData<isLoginResponseBody> isLogin(){

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),Msg.E200_1_INQUIRY_SUCCEED.getMsg(),new isLoginResponseBody(rq.isLogin()));
    }

}
