package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 店铺DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {

    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private String phone;

    private Long ownerUserId;

    private String address;

    private String businessHours;

    private Double rating;

    private Integer monthlySales;

    private Boolean approved;

    private Boolean open;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
