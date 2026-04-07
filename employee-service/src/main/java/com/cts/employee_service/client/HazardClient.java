package com.cts.employee_service.client;

import com.cts.employee_service.dto.HazardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "HAZARD-SERVICE") // Eureka mein jo naam hai wahi yahan aayega
public interface HazardClient {

    @PostMapping("/hazards/report") // Hazard Service ka POST endpoint mapping
    HazardDTO createHazard(@RequestBody HazardDTO hazardDTO);

    @GetMapping("/hazards/employee/{id}") // Hazard Service ka GET endpoint mapping
    List<HazardDTO> getHazardsByEmployee(@PathVariable("id") long id);
}