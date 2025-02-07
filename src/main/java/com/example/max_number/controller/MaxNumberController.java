package com.example.max_number.controller;

import com.example.max_number.service.MaxNumberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class MaxNumberController {
    private final MaxNumberService maxNumberService;

    @Operation(summary = "Поиск N-го максимального числа в .xlsx файле")
    @GetMapping("/max")
    public Long getMaxNumberFromXLSXFileByIndex(@RequestParam @Pattern(regexp = "(?i).+\\.xlsx$") String filePath, @RequestParam @Min(1) int index) {
        try {
            return maxNumberService.getMaxNumberFromXLSXFileByIndex(filePath, index);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
