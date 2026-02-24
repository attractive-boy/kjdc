package com.kjdc.controller;

import com.kjdc.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查API
 */
@RestController
public class HealthController {
    
    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        Map<String, String> data = new HashMap<>();
        data.put("status", "UP");
        data.put("version", "1.0.0");
        data.put("application", "kjdc-backend");
        return ApiResponse.success(data);
    }
}
