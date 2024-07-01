package com.ll.whev.domain.purchase.repository;

import com.ll.whev.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
    Optional<Purchase> findByImage_IdAndMember_Id(Long imageId, Long memberId);
}
