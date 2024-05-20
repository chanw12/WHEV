package com.ll.whev.domain.payment.repository;

import com.ll.whev.domain.payment.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    Optional<Payment> findByOrderId(String orderId);
}
