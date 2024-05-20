package com.ll.whev.domain.payment.controller;

import com.ll.whev.domain.payment.PaymentDto;
import com.ll.whev.domain.payment.PaymentResDto;
import com.ll.whev.domain.payment.dto.PaymentFailDto;
import com.ll.whev.domain.payment.dto.PaymentSuccessDto;
import com.ll.whev.domain.payment.service.PaymentService;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.global.exceptions.CodeMsg;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
//    private final TossPaymentConfig tossPaymentConfig;
//    private final PaymentMapper mapper;


    @PostMapping("/toss")
    public RsData<PaymentResDto> requestTossPayment(@RequestBody @Valid PaymentDto paymentDto) {
        PaymentResDto paymentResDto = paymentService.requestTossPayment(paymentDto.toEntity()).toPaymentResDto();
        paymentResDto.setSuccessUrl(paymentDto.getYourSuccessUrl() == null? AppConfig.getSuccessUrl():paymentDto.getYourSuccessUrl());
        paymentResDto.setFailUrl(paymentDto.getYourFailUrl() == null? AppConfig.getFailUrl():paymentDto.getYourFailUrl());
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(),paymentResDto);
    }

    @GetMapping("/toss/success")
    public RsData<PaymentSuccessDto> tossPaymentSuccess(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount){
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(),paymentService.tossPaymentSuccess(paymentKey,orderId,amount));
    }

    @GetMapping("/toss/fail")
    public RsData<PaymentFailDto> tossPaymentFail( @RequestParam String code,
                                                   @RequestParam String message,
                                                   @RequestParam String orderId){
        paymentService.tossPaymentFail(code,message,orderId);
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(),PaymentFailDto.builder()
                .errorCode(code)
                .errorMessage(message)
                .orderId(orderId)
                .build());
    }
}
