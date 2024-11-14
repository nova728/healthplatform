package com.health.healthplatform.controller;

import com.health.healthplatform.DTO.HealthDataDTO;
import com.health.healthplatform.DTO.HeartRateDTO;
import com.health.healthplatform.DTO.StepsDTO;
import com.health.healthplatform.service.HealthDataService;
import com.health.healthplatform.service.HeartRateService;
import com.health.healthplatform.service.StepsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health")
@Slf4j
public class HealthDataController {
    private final HealthDataService healthDataService;

    @Resource
    HeartRateService heartRateService;

    @Resource
    StepsService stepsService;

    public HealthDataController(HealthDataService healthDataService) {
        this.healthDataService = healthDataService;
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<HealthDataDTO>> getHealthHistory(@PathVariable("id") Integer userId) {
        log.info("Received request for user id: {}", userId);

        if (userId <= 0) {
            log.warn("Invalid user id: {}", userId);
            return ResponseEntity.badRequest().build();
        }

        List<HealthDataDTO> healthData = healthDataService.getHealthHistory(userId);
        log.info("Found {} records for user {}", healthData.size(), userId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(healthData);
    }

    //心率详情
    @GetMapping("/{id}/heart-rate")
    public ResponseEntity<List<HeartRateDTO>> getHeartRateHistory(
            @PathVariable("id") Integer userId,
            @RequestParam(value = "period", defaultValue = "day") String period) {

        log.info("Fetching heart rate history for user: {}, period: {}", userId, period);

        if (userId <= 0) {
            log.warn("Invalid user id: {}", userId);
            return ResponseEntity.badRequest().build();
        }

        List<HeartRateDTO> heartRateData = heartRateService.getUserHeartRateHistory(userId, period);
        log.info("Found {} heart rate records for user {}", heartRateData.size(), userId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(heartRateData);
    }

    @PostMapping("/{id}/heart-rate")
    public ResponseEntity<HeartRateDTO> recordHeartRate(
            @PathVariable("id") Integer userId,
            @RequestBody HeartRateDTO heartRateDTO) {

        log.info("Recording new heart rate for user: {}", userId);

        if (userId <= 0) {
            log.warn("Invalid user id: {}", userId);
            return ResponseEntity.badRequest().build();
        }

        try {
            HeartRateDTO recorded = heartRateService.recordHeartRate(userId, heartRateDTO.getHeartRate());
            return ResponseEntity.ok(recorded);
        } catch (IllegalArgumentException e) {
            log.error("Invalid heart rate data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    //步数
    @GetMapping("/{id}/steps")
    public ResponseEntity<List<StepsDTO>> getStepsHistory(
            @PathVariable("id") Integer userId,
            @RequestParam(value = "period", defaultValue = "day") String period) {

        log.info("Fetching steps history for user: {}, period: {}", userId, period);

        if (userId <= 0) {
            log.warn("Invalid user id: {}", userId);
            return ResponseEntity.badRequest().build();
        }

        List<StepsDTO> stepsData = stepsService.getUserStepsHistory(userId, period);
        log.info("Found {} steps records for user {}", stepsData.size(), userId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(stepsData);
    }

    @PostMapping("/{id}/steps")
    public ResponseEntity<StepsDTO> recordSteps(
            @PathVariable("id") Integer userId,
            @RequestBody StepsDTO stepsDTO) {

        log.info("Recording new steps for user: {}", userId);

        if (userId <= 0) {
            log.warn("Invalid user id: {}", userId);
            return ResponseEntity.badRequest().build();
        }

        try {
            StepsDTO recorded = stepsService.recordSteps(userId, stepsDTO.getSteps());
            return ResponseEntity.ok(recorded);
        } catch (IllegalArgumentException e) {
            log.error("Invalid steps data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}