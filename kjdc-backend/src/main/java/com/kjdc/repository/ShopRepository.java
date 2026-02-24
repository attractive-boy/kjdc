package com.kjdc.repository;

import com.kjdc.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 店铺Repository
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByApprovedTrueAndOpenTrueOrderByMonthlySalesDesc();
}
