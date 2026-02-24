package com.kjdc.service.impl;

import com.kjdc.dto.ShopCreateRequest;
import com.kjdc.dto.ShopDTO;
import com.kjdc.dto.ShopUpdateRequest;
import com.kjdc.entity.Shop;
import com.kjdc.repository.ShopRepository;
import com.kjdc.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺业务服务实现
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    @Override
    public List<ShopDTO> getOpenApprovedShops() {
        return shopRepository.findByApprovedTrueAndOpenTrueOrderByMonthlySalesDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopDTO> getAllShops() {
        return shopRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShopDTO getShopById(Long id) {
        return shopRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public ShopDTO createShop(ShopCreateRequest request) {
        Shop shop = Shop.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .phone(request.getPhone())
                .ownerUserId(request.getOwnerUserId())
                .address(request.getAddress())
                .businessHours(request.getBusinessHours())
                .approved(false)
                .open(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Shop saved = shopRepository.save(shop);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public ShopDTO updateShop(Long id, ShopUpdateRequest request) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("店铺不存在"));

        if (request.getName() != null) {
            shop.setName(request.getName());
        }
        if (request.getDescription() != null) {
            shop.setDescription(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            shop.setImageUrl(request.getImageUrl());
        }
        if (request.getPhone() != null) {
            shop.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            shop.setAddress(request.getAddress());
        }
        if (request.getBusinessHours() != null) {
            shop.setBusinessHours(request.getBusinessHours());
        }
        if (request.getRating() != null) {
            shop.setRating(request.getRating());
        }
        if (request.getMonthlySales() != null) {
            shop.setMonthlySales(request.getMonthlySales());
        }
        if (request.getOpen() != null) {
            shop.setOpen(request.getOpen());
        }
        shop.setUpdatedAt(LocalDateTime.now());

        Shop saved = shopRepository.save(shop);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public ShopDTO setShopOpen(Long id, Boolean open) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("店铺不存在"));
        shop.setOpen(open != null ? open : shop.getOpen());
        shop.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(shopRepository.save(shop));
    }

    @Override
    @Transactional
    public ShopDTO setShopApproved(Long id, Boolean approved) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("店铺不存在"));
        shop.setApproved(approved != null ? approved : shop.getApproved());
        shop.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(shopRepository.save(shop));
    }

    @Override
    @Transactional
    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }

    private ShopDTO convertToDTO(Shop shop) {
        return ShopDTO.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .imageUrl(shop.getImageUrl())
                .phone(shop.getPhone())
                .ownerUserId(shop.getOwnerUserId())
                .address(shop.getAddress())
                .businessHours(shop.getBusinessHours())
                .rating(shop.getRating())
                .monthlySales(shop.getMonthlySales())
                .approved(shop.getApproved())
                .open(shop.getOpen())
                .createdAt(shop.getCreatedAt())
                .updatedAt(shop.getUpdatedAt())
                .build();
    }
}
