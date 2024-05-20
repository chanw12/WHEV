package com.ll.whev.domain.payment.service;

import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.domain.payment.Payment;
import com.ll.whev.domain.payment.PaymentResDto;
import com.ll.whev.domain.payment.dto.PaymentSuccessDto;
import com.ll.whev.domain.payment.repository.PaymentRepository;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.global.exceptions.CustomLogicException;
import com.ll.whev.global.exceptions.ExceptionCode;
import com.ll.whev.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {
    private final MemberService memberService;
    private final Rq rq;
    private final PaymentRepository paymentRepository;


    public Payment requestTossPayment(Payment payment) {
        Member member = rq.getMember();
        if(payment.getAmount() < 1000){
            throw new IllegalArgumentException("결제금액은 1000원 이상이어야 합니다.");
        }
        payment.setCustomer(member);
        return paymentRepository.save(payment);
    }

    @Transactional
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = verifyPayment(orderId, amount);
        PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        payment.setPaymentKey(paymentKey);//추후 결제 취소 / 결제 조회
        payment.setPaySuccessYN(true);
        payment.getCustomer().setCache(payment.getCustomer().getCache() + amount);
        memberService.updateMemberCache(payment.getCustomer());
        return result;
    }


    public Payment verifyPayment(String orderId, Long amount) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        });
        if (!payment.getAmount().equals(amount)) {
            throw new CustomLogicException(ExceptionCode.PAYMENT_AMOUNT_EXP);
        }
        return payment;
    }

    @Transactional
    public PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();//키/값 쌍을 문자열이 아닌 오브젝트로 보낼 수 있음
        params.put("orderId", orderId);
        params.put("amount", amount);

        PaymentSuccessDto result = null;
        try { //post요청 (url , HTTP객체 ,응답 Dto)
            result = restTemplate.postForObject(AppConfig.URL + paymentKey,
                    new HttpEntity<>(params, headers),
                    PaymentSuccessDto.class);
        } catch (Exception e) {
            throw new CustomLogicException(ExceptionCode.ALREADY_APPROVED);
        }

        return result;

    }
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder().encode((AppConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Transactional
    public void tossPaymentFail(String code, String message, String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        });
        payment.setPaySuccessYN(false);
        payment.setFailReason(message);
    }


}
