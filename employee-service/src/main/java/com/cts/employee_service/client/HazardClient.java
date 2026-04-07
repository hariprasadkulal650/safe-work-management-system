package com.cts.employee_service.client;

import com.cts.employee_service.dto.HazardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "hazard-incident-service")
public interface HazardClient {

    @PostMapping("/hazard/postHazard/{employeeId}")
    HazardDTO createHazard(@PathVariable("employeeId") Long employeeId, @RequestBody HazardDTO hazardDTO);


    @GetMapping("/hazard/employee/{employeeId}")
    List<HazardDTO> getHazardsByEmployee(@PathVariable("employeeId") Long employeeId);
}