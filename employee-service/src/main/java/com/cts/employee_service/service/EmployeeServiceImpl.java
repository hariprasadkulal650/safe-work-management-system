


package com.cts.employee_service.service;

import com.cts.employee_service.dto.EmployeeResponseDTO;
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
    public Employee loginEmployee(String email, String password) {
        return employeeRepository.findByEmail(email)
                .filter(emp -> emp.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
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
}