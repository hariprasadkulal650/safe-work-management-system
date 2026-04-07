package com.cts.employee_service.dto;



import lombok.Data;

@Data
public class HazardDTO {
    private Long id;
    private String hazardType;
    private String description;
    private Long employeeId;
    private String status; // e.g., PENDING, RESOLVED
}
