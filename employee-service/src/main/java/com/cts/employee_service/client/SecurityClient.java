package com.cts.employee_service.client;

import com.cts.employee_service.dto.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Point this to your security-service
@FeignClient(name = "security-service", url = "http://localhost:8080")
public interface SecurityClient {

    @PostMapping("/auth/login")
    String login(@RequestBody LoginRequest loginRequest);
}