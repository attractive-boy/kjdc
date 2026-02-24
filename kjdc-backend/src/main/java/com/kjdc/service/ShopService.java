package com.kjdc.service;

import com.kjdc.dto.ShopCreateRequest;
import com.kjdc.dto.ShopDTO;
import com.kjdc.dto.ShopUpdateRequest;

import java.util.List;

/**
 * 店铺业务服务接口
 */
public interface ShopService {

    List<ShopDTO> getOpenApprovedShops();

    List<ShopDTO> getAllShops();

    ShopDTO getShopById(Long id);

    ShopDTO createShop(ShopCreateRequest request);

    ShopDTO updateShop(Long id, ShopUpdateRequest request);

    ShopDTO setShopOpen(Long id, Boolean open);

    ShopDTO setShopApproved(Long id, Boolean approved);

    void deleteShop(Long id);
}
