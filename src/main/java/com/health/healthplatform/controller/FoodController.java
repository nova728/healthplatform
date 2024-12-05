package com.health.healthplatform.controller;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.health.healthplatform.service.FoodSearchService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/foods")
@Slf4j
public class FoodController {
    private final FoodSearchService foodSearchService;

    public FoodController(FoodSearchService foodSearchService) {
        this.foodSearchService = foodSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFood(
        @RequestParam String query,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "搜索关键词不能为空"));
            }

            if (page < 1 || size < 1) {
                return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "页码和大小必须大于0"));
            }

            JsonNode result = foodSearchService.searchFood(query, page, size);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("搜索处理失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "搜索处理失败: " + e.getMessage()));
        }
    }
}
