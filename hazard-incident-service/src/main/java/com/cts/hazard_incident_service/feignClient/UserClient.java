package com.cts.hazard_incident_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        path = "/users"
)
public interface UserClient {

    @GetMapping("/getUserById/{userId}")
    void getUserById(@PathVariable("userId") Long userId);
}