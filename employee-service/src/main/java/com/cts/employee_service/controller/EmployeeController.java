package com.cts.employee_service.controller;

import com.cts.employee_service.dto.EmployeeResponseDTO;
import com.cts.employee_service.dto.HazardDTO;
import com.cts.employee_service.dto.LoginRequest;
import com.cts.employee_service.dto.TrainingDTO;
import com.cts.employee_service.entities.*;
import com.cts.employee_service.service.IEmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<Employee> register(@Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.registerEmployee(employee), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Employee> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(employeeService.loginEmployee(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<EmployeeResponseDTO>> getAll() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // Hazard Routes (using DTOs)
    @PostMapping("/hazard/report")
    public ResponseEntity<HazardDTO> reportHazard(@Valid @RequestBody HazardDTO hazardDTO) {
        return new ResponseEntity<>(employeeService.reportHazard(hazardDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/hazards")
    public ResponseEntity<List<HazardDTO>> getHazards(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.getHazardsByEmployee(id));
    }

    // Training Routes
    @GetMapping("/{id}/trainings")
    public ResponseEntity<List<TrainingDTO>> getMyTrainings(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.getTrainingsByEmployee(id));
    }

    @GetMapping("/{id}/document")
    public ResponseEntity<EmployeeDocument> getDocument(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.getEmployeeDocument(id));
    }
}