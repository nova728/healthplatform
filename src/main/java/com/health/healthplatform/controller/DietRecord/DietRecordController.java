package com.health.healthplatform.controller.DietRecord;

import com.health.healthplatform.DTO.DietRecord.DailyDietDTO;
import com.health.healthplatform.DTO.DietRecord.MealRecordDTO;
import com.health.healthplatform.service.DietRecord.DietRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diet")
@Slf4j
public class DietRecordController {
    private final DietRecordService dietRecordService;

    public DietRecordController(DietRecordService dietRecordService) {
        this.dietRecordService = dietRecordService;
    }

    @PostMapping("/{userId}/meals")
    public ResponseEntity<DailyDietDTO> addMealRecord(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody MealRecordDTO mealRecord) {
        return ResponseEntity.ok(dietRecordService.addMealRecord(userId, date, mealRecord));
    }

    @GetMapping("/{userId}/daily")
    public ResponseEntity<DailyDietDTO> getDailyDiet(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(dietRecordService.getDailyDiet(userId, date));
    }

    @DeleteMapping("/{userId}/meals/{mealId}")
    public ResponseEntity<DailyDietDTO> deleteMealRecord(
            @PathVariable Integer userId,
            @PathVariable Long mealId) {
        return ResponseEntity.ok(dietRecordService.deleteMealRecord(userId, mealId));
    }
}