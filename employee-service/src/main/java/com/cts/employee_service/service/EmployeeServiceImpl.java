


package com.cts.employee_service.service;

import com.cts.employee_service.client.SecurityClient;
import com.cts.employee_service.dto.EmployeeResponseDTO;
import com.cts.employee_service.dto.LoginRequest;
import com.cts.employee_service.entities.*;
import com.cts.employee_service.enums.EmployeeStatus;
import com.cts.employee_service.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SecurityClient securityClient;

    @Override
    public Employee registerEmployee(Employee employee) {
        if (employee.getDocument() != null) {
            employee.getDocument().setUploadedDate(LocalDate.now());
            employee.getDocument().setVerificationStatus("PENDING");
        }
        employee.setEmployeeStatus(EmployeeStatus.ACTIVE);
        return employeeRepository.save(employee);
    }

    @Override
    public String loginEmployee(String email, String password) {
        // Build login request DTO
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Delegate authentication to security-service
        return securityClient.login(loginRequest);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(emp -> new EmployeeResponseDTO(
                        emp.getEmployeeId(),
                        emp.getEmployeeName(),
                        emp.getEmail(),
                        emp.getEmployeeDepartmentName(),
                        emp.getEmployeeStatus().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        return new EmployeeResponseDTO(
                emp.getEmployeeId(),
                emp.getEmployeeName(),
                emp.getEmail(),
                emp.getEmployeeDepartmentName(),
                emp.getEmployeeStatus().toString());
    }

    @Override
    public EmployeeDocument getEmployeeDocument(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found."));
        return employee.getDocument();
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee with email " + email + " not found."));
    }





}