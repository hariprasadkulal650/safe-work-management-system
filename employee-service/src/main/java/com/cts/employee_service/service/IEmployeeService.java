

package com.cts.employee_service.service;

import com.cts.employee_service.dto.*;
import com.cts.employee_service.entities.*;
import java.util.List;

public interface IEmployeeService {
    Employee registerEmployee(Employee employee);
    Employee loginEmployee(String email, String password);
    List<EmployeeResponseDTO> getAllEmployees();
    EmployeeResponseDTO getEmployeeById(long id);
    EmployeeDocument getEmployeeDocument(long employeeId); // New requirement added
}