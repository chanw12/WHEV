package com.ll.whev.domain.payment;

import com.ll.whev.domain.member.entity.Member;
import com.ll.whev.domain.payment.dto.PaymentCancelDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Auditable;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(indexes = {
        @Index(name = "idx_payment_member", columnList = "customer"),
        @Index(name = "idx_payment_paymentKey", columnList = "paymentKey"),
})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;

    @CreatedDate
    private LocalDateTime createDate;

    @Column(nullable = false, name = "pay_type")
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @Column(nullable = false, name = "pay_amount")
    private Long amount;
    @Column(nullable = false, name = "pay_name")
    private String orderName;
    @Column(nullable = false, name = "order_id")
    private String orderId;

    private boolean paySuccessYN;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer")
    private Member customer;
    @Column
    private String paymentKey;
    @Column
    private String failReason;

    @Column
    private boolean cancelYN;
    @Column
    private String cancelReason;

    public PaymentResDto toPaymentResDto() {
        return PaymentResDto.builder()
                .payType(payType.getDescription())
                .amount(amount)
                .orderName(orderName)
                .orderId(orderId)
                .customerName(customer.getName())
                .createDate(this.createDate != null ? this.createDate.toString() : LocalDateTime.now().toString())
                .cancelYN(cancelYN)
                .failReason(failReason)
                .build();
    }

    public PaymentCancelDto toPaymentCancelDto() {
        return PaymentCancelDto.builder()
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .customerName(customer.getNickname())
                .createDate(this.createDate != null ? this.createDate.toString() : LocalDateTime.now().toString())
                .paymentKey(paymentKey)
                .build();
    }


}