package com.ll.whev.domain.payment.service;

import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.member.repository.MemberRepository;
import com.ll.whev.domain.payment.Payment;
import com.ll.whev.domain.payment.dto.PaymentSuccessDto;
import com.ll.whev.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testTossPaymentSuccess() {
        String paymentKey = "testKey";
        String orderId = "testOrder";
        Long amount = 1000L;

        // Given: 기존 멤버 생성 및 저장
        Member member = new Member();
        member.setCache(0L);
        memberRepository.save(member);

        // Given: 기존 결제 생성 및 저장
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setCustomer(member);
        // ... 기타 초기화 코드

        // When: 결제 성공 처리
        PaymentSuccessDto result = paymentService.tossPaymentSuccess(paymentKey, orderId, amount);

        // Then: 결제 키, 성공 여부 및 캐쉬 업데이트 검증
        Payment updatedPayment = paymentRepository.findByOrderId(orderId).get();
        assertNotNull(updatedPayment);
        assertEquals(paymentKey, updatedPayment.getPaymentKey());
        assertTrue(updatedPayment.isPaySuccessYN());

        Member updatedMember = memberRepository.findById(member.getId()).orElse(null);
        assertNotNull(updatedMember);
        assertEquals(amount, updatedMember.getCache());
    }
}
