package com.cts.employee_service.service;

import com.cts.employee_service.client.HazardClient;
import com.cts.employee_service.client.TrainingClient;
import com.cts.employee_service.dto.EmployeeResponseDTO;
import com.cts.employee_service.dto.HazardDTO;
import com.cts.employee_service.dto.TrainingDTO;
import com.cts.employee_service.entities.Employee;
import com.cts.employee_service.entities.EmployeeDocument;
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
    private HazardClient hazardClient; // Naya Feign Client

    @Autowired
    private TrainingClient trainingClient; // Naya Feign Client

    private EmployeeResponseDTO mapToDTO(Employee emp) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setEmployeeId(emp.getEmployeeId());
        dto.setEmployeeName(emp.getEmployeeName());
        dto.setEmail(emp.getEmail());
        dto.setEmployeeDepartmentName(emp.getEmployeeDepartmentName());
        dto.setEmployeeStatus(emp.getEmployeeStatus().toString());
        return dto;
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        return mapToDTO(emp);
    }

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

    // --- External Service Calls via Feign ---

    @Override
    public HazardDTO reportHazard(HazardDTO hazardDTO) {
        if(!employeeRepository.existsById(hazardDTO.getEmployeeId())) {
            throw new RuntimeException("Employee ID does not exist.");
        }
        return hazardClient.createHazard(hazardDTO); // Hazard Service ko call gaya
    }

    @Override
    public List<HazardDTO> getHazardsByEmployee(long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found.");
        }
        return hazardClient.getHazardsByEmployee(employeeId);
    }

    @Override
    public List<TrainingDTO> getTrainingsByEmployee(long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found.");
        }
        return trainingClient.getTrainingsByEmployee(employeeId);
    }

    @Override
    public EmployeeDocument getEmployeeDocument(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found."));
        return employee.getDocument();
    }
}