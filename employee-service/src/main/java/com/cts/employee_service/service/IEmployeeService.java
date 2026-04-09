

package com.cts.employee_service.service;

import com.cts.employee_service.dto.*;
import com.cts.employee_service.entities.*;
import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    Employee registerEmployee(Employee employee);
    String loginEmployee(String email, String password);
    List<EmployeeResponseDTO> getAllEmployees();
    EmployeeResponseDTO getEmployeeById(long id);
    EmployeeDocument getEmployeeDocument(long employeeId);
    Employee getEmployeeByEmail(String email);

}