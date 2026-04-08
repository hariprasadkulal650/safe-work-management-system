package com.cts.employee_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {
    private long employeeId;
    private String employeeName;
    private String email;
    private String employeeDepartmentName;
    private String employeeStatus;
    // Notice: NO Password, NO DOB, NO Address here.
}