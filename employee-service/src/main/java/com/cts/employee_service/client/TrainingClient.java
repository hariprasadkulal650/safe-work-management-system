package com.cts.employee_service.client;

import com.cts.employee_service.dto.TrainingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "TRAINING-SERVICE") // Eureka mein registered name
public interface TrainingClient {

    @GetMapping("/trainings/employee/{id}") // Training Service ka endpoint mapping
    List<TrainingDTO> getTrainingsByEmployee(@PathVariable("id") long id);
}