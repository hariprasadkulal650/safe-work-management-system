package com.cts.inspection_service.Controller;


import com.cts.inspection_service.Dto.InspectionRequestDTO;
import com.cts.inspection_service.Dto.InspectionResponseDTO;
import com.cts.inspection_service.Service.IInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    @Autowired
    private IInspectionService service;

    @PostMapping
    public ResponseEntity<InspectionResponseDTO> create(@RequestBody InspectionRequestDTO dto) {
        return ResponseEntity.ok(service.createInspection(dto));
    }

    @GetMapping
    public ResponseEntity<List<InspectionResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllInspections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getInspectionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteInspection(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}