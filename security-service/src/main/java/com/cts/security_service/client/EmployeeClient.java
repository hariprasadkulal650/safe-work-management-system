package com.cts.security_service.client;

import com.cts.employee_service.entities.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service")
public interface EmployeeClient {
    @GetMapping("/employees/getByEmail/{email}")
    Employee findByEmail(@PathVariable("email") String email);
}

