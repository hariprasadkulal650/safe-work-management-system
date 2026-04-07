package com.cts.hazard_incident_service.controller;

import com.cts.hazard_incident_service.dto.HazardRequestDto;
import com.cts.hazard_incident_service.enums.HazardStatus;
import com.cts.hazard_incident_service.projection.HazardReportProjection;
import com.cts.hazard_incident_service.service.IHazardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/hazard")
@Slf4j
public class HazardController {

    @Autowired
    private IHazardService hazardService;

    @PostMapping("/postHazard/{employeeId}")
    public ResponseEntity<HazardRequestDto> addHazard(@PathVariable Long employeeId,@Valid @RequestBody HazardRequestDto request) {
        log.info("Received request to report a new hazard. Reported by Employee ID: {}", employeeId);
        HazardRequestDto savedHazard = hazardService.addHazard(employeeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHazard);
    }

    @GetMapping("/getAllHazard")
    public ResponseEntity<List<HazardReportProjection>> getAllHazards() {
        List<HazardReportProjection> hazards = hazardService.getAllHazards();
        return ResponseEntity.ok(hazards);
    }

    @GetMapping("/getById/{hazardId}")
    public ResponseEntity<HazardReportProjection> findByHazardId(@PathVariable Long hazardId) {
        HazardReportProjection hazard = hazardService.getHazardById(hazardId);
        return ResponseEntity.ok(hazard);
    }

    @DeleteMapping("/delete/{hazardId}/{employeeId}")
    public ResponseEntity<String> deleteHazard(@PathVariable Long hazardId,@PathVariable Long employeeId) {
        String response = hazardService.deleteHazard(hazardId, employeeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{hazardId}/{employeeId}")
    public ResponseEntity<HazardRequestDto> updateHazard(@PathVariable Long hazardId,@PathVariable Long employeeId,@Valid @RequestBody HazardRequestDto hazardRequestDto) {
        HazardRequestDto updated = hazardService.updateHazard(hazardId, employeeId, hazardRequestDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<HazardReportProjection>> getHazardsByEmployee(@PathVariable Long employeeId) {
        List<HazardReportProjection> hazards = hazardService.getHazardsByEmployee(employeeId);
        return ResponseEntity.ok(hazards);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<HazardReportProjection>> getHazardsByStatus(@PathVariable HazardStatus status) {
        List<HazardReportProjection> hazards = hazardService.getHazardsByStatus(status);
        return ResponseEntity.ok(hazards);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getHazardSummary() {
        Map<String, Long> summary = hazardService.getHazardSummary();
        return ResponseEntity.ok(summary);
    }



}