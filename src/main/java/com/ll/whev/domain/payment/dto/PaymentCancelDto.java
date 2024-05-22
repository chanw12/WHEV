package com.ll.whev.domain.payment.dto;

import com.ll.whev.domain.payment.PayType;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCancelDto {
    private PayType payType;
    private Long amount;
    private String orderName;

    private String customerName;
    private String createDate;
    private String paymentKey;

}
