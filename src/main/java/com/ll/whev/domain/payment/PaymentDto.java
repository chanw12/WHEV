package com.ll.whev.domain.payment;

import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    @NonNull
    private PayType payType;
    @NonNull
    private Long amount;
    @NonNull
    private String orderName;

    private String yourSuccessUrl;
    private String yourFailUrl;


    public Payment toEntity() {
        return Payment.builder()
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .paySuccessYN(false)
                .createDate(LocalDateTime.now())
                .build();
    }

}
