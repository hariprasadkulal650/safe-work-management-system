package com.cts.security_service.client;

import com.cts.security_service.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/getByEmail/{userEmail}")
    User findByEmail(@PathVariable("userEmail") String email);
}

