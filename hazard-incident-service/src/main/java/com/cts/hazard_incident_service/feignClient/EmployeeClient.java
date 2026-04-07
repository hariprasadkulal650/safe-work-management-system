package com.cts.hazard_incident_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "employee-service",
        path = "/employees"
)
public interface EmployeeClient {

    @GetMapping("/getEmployeebyId/{employeeId}")
    void getEmployeeById(@PathVariable("employeeId") Long employeeId);
}