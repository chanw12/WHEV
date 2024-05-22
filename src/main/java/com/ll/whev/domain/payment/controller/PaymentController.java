package com.ll.whev.domain.payment.controller;

import com.ll.whev.domain.payment.Payment;
import com.ll.whev.domain.payment.PaymentDto;
import com.ll.whev.domain.payment.PaymentResDto;
import com.ll.whev.domain.payment.dto.ChargingHistoryDto;
import com.ll.whev.domain.payment.dto.PaymentFailDto;
import com.ll.whev.domain.payment.dto.PaymentSuccessDto;
import com.ll.whev.domain.payment.mapper.PaymentMapper;
import com.ll.whev.domain.payment.service.PaymentService;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.global.exceptions.CodeMsg;
import com.ll.whev.global.msg.Msg;
import com.ll.whev.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper mapper;


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

    @GetMapping("/history")
    public RsData<Slice<ChargingHistoryDto>> getChargingHistory(@RequestParam int page) {
        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, sort);
        Slice<Payment> chargingHistories = paymentService.findAllChargingHistories(pageable);
        Slice<ChargingHistoryDto> chargingHistoryDtos = mapper.chargingHistoryToChargingHistoryResponses(chargingHistories);
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(), chargingHistoryDtos);
    }

    @PostMapping("/toss/cancel/point")
    public RsData<Map> tossPaymentCancelPoint(@RequestParam String paymentKey, @RequestParam String cancelReason){
        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(),paymentService.cancelPaymentPoint(paymentKey,cancelReason));
    }
}
