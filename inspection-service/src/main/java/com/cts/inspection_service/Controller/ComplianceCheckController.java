package com.cts.inspection_service.Controller;


import com.cts.inspection_service.Dto.ComplianceRequestDTO;
import com.cts.inspection_service.Dto.ComplianceResponseDTO;
import com.cts.inspection_service.Service.IComplianceCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance-checks")
public class ComplianceCheckController {

    @Autowired
    private IComplianceCheckService service;

    @PostMapping
    public ResponseEntity<ComplianceResponseDTO> create(@RequestBody ComplianceRequestDTO dto) {
        return new ResponseEntity<>(service.createCheck(dto), HttpStatus.CREATED);
    }

    @GetMapping("/inspection/{inspectionId}")
    public ResponseEntity<List<ComplianceResponseDTO>> getByInspection(@PathVariable Long inspectionId) {
        return ResponseEntity.ok(service.getChecksByInspectionId(inspectionId));
    }

    @GetMapping
    public ResponseEntity<List<ComplianceResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllChecks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplianceResponseDTO> update(@PathVariable Long id, @RequestBody ComplianceRequestDTO dto) {
        return ResponseEntity.ok(service.updateCheck(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteCheck(id);
        return ResponseEntity.ok("Compliance check deleted successfully");
    }
}
