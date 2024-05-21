package com.ll.whev.domain.payment.mapper;

import com.ll.whev.domain.payment.Payment;
import com.ll.whev.domain.payment.dto.ChargingHistoryDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    default Slice<ChargingHistoryDto> chargingHistoryToChargingHistoryResponses(Slice<Payment> chargingHistories) {
        if (chargingHistories == null) {
            return null;
        }

        List<ChargingHistoryDto> dtoList = chargingHistories.stream()
                .map(chargingHistory -> ChargingHistoryDto.builder()
                        .paymentHistoryId(chargingHistory.getPaymentId())
                        .amount(chargingHistory.getAmount())
                        .orderName(chargingHistory.getOrderName())
                        .createdAt(chargingHistory.getCreateDate())
                        .isPaySuccessYN(chargingHistory.isPaySuccessYN())
                        .build())
                .collect(Collectors.toList());

        // Create a new Slice with the mapped DTOs
        return new SliceImpl<>(dtoList, chargingHistories.getPageable(), chargingHistories.hasNext());
    }

}