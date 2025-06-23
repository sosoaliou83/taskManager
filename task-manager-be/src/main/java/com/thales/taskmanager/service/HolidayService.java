package com.thales.taskmanager.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thales.taskmanager.dto.HolidayDTO;
import com.thales.taskmanager.dto.HolidayEntry;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HolidayService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${holidays.api.url}")
    private String holidaysApiUrl;

    /**
     * Fetches public holidays for the given year and country.
     */
    public List<HolidayDTO> getPublicHolidays(int year, String countryCode) {
        String url = String.format("%s/%d/%s", holidaysApiUrl, year, countryCode);
        log.info("Fetching public holidays from URL: {}", url);

        try {
            ResponseEntity<HolidayEntry[]> response = restTemplate.getForEntity(url, HolidayEntry[].class);
            HolidayEntry[] entries = response.getBody();

            if (entries == null) {
                log.warn("No public holidays found in API response.");
                return List.of();
            }

            List<HolidayDTO> holidays = Arrays.stream(entries)
                    .map(e -> new HolidayDTO(e.getDate(), e.getLocalName()))
                    .collect(Collectors.toList());

            log.info("Fetched {} public holidays for year {} and country {}", holidays.size(), year, countryCode);
            return holidays;

        } catch (Exception e) {
            log.error("Error fetching public holidays from {}: {}", url, e.getMessage(), e);
            return List.of();
        }
    }
}
