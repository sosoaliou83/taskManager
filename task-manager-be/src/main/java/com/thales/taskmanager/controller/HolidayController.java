package com.thales.taskmanager.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thales.taskmanager.dto.ApiResponse;
import com.thales.taskmanager.dto.HolidayDTO;
import com.thales.taskmanager.service.HolidayService;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    @Autowired
    HolidayService holidayService;

    /**
     * GET /api/holidays
     * 
     * @param year        optional year (defaults to current year)
     * @param countryCode optional country code (defaults to 'FR')
     * @return list of public holidays with date and localName
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<HolidayDTO>>> getHolidays(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "SG") String countryCode) {
        int y = (year != null) ? year : LocalDate.now().getYear();
        List<HolidayDTO> holidays = holidayService.getPublicHolidays(y, countryCode);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Public holidays fetched", holidays));
    }
}
