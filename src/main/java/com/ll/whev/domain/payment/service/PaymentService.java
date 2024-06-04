package com.ll.whev.domain.payment.service;

import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.service.MemberService;
import com.ll.whev.domain.payment.Payment;
import com.ll.whev.domain.payment.PaymentResDto;
import com.ll.whev.domain.payment.dto.PaymentSuccessDto;
import com.ll.whev.domain.payment.repository.PaymentRepository;
import com.ll.whev.domain.sse.service.SseService;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.global.exceptions.CustomLogicException;
import com.ll.whev.global.exceptions.ExceptionCode;
import com.ll.whev.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {
    private final MemberService memberService;
    private final Rq rq;
    private final PaymentRepository paymentRepository;
    private final SseService sseService;

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        });
    }


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
        if(payment.isPaySuccessYN()){
            return getExistingPaymentSuccessDto(payment);
        }
        PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        payment.setPaymentKey(paymentKey);//추후 결제 취소 / 결제 조회
        payment.setPaySuccessYN(true);
        payment.getCustomer().setCache(payment.getCustomer().getCache() + amount);
        memberService.updateMemberCache(payment.getCustomer());
        sseService.updatePoints(payment.getCustomer());
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
            if (isAlreadyApprovedException(e)) {
                Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
                    throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
                });
                return getExistingPaymentSuccessDto(payment);
            } else {
                throw new CustomLogicException(ExceptionCode.ALREADY_APPROVED);
            }
        }

        return result;

    }
    private boolean isAlreadyApprovedException(Exception e) {
        // 이미 승인된 요청에 대한 예외 여부를 확인하는 로직 구현
        // 예외 메시지 또는 특정 조건에 따라 확인
        return e.getMessage().contains("already approved") || e.getMessage().contains("ALREADY_APPROVED");
    }

    private PaymentSuccessDto getExistingPaymentSuccessDto(Payment payment) {
        PaymentSuccessDto dto = new PaymentSuccessDto();
        dto.setPaymentKey(payment.getPaymentKey());
        dto.setOrderId(payment.getOrderId());
        dto.setTotalAmount(payment.getAmount());
        dto.setApprovedAt(payment.getCreateDate().toString());

        // 추가 필드 설정
        return dto;
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

    public Slice<Payment> findAllChargingHistories(Pageable pageable) {
        if(rq.getMember() == null){
            throw new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        return paymentRepository.findByCustomer_Id(rq.getMember().getId(), pageable);

    }
    @Transactional
    public Map cancelPaymentPoint(String paymentKey, String cancelReason) {
        Member member = rq.getMember();
        Payment payment = paymentRepository.findByPaymentKeyAndCustomerId(paymentKey,member.getId()).orElseThrow(() -> {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        });

        if (payment.getCustomer().getCache() >= payment.getAmount()) {
            payment.setCancelYN(true);
            payment.setCancelReason(cancelReason);
            payment.getCustomer().setCache(payment.getCustomer().getCache() - payment.getAmount());
            sseService.updatePoints(payment.getCustomer());
            return tossPaymentCancel(paymentKey, cancelReason);
        }

        throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_ENOUGH_POINT);

    }

    public Map tossPaymentCancel(String paymentKey, String cancelReason) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("cancelReason", cancelReason);

        return restTemplate.postForObject(AppConfig.URL + paymentKey + "/cancel",
                new HttpEntity<>(params, headers),
                Map.class);
    }

}
