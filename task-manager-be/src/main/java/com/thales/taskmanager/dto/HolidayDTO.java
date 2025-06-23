package com.thales.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representing a public holiday date and its local name.
 */
@Data
@AllArgsConstructor
public class HolidayDTO {
    private String date; // ISO date string (YYYY-MM-DD)
    private String localName; // Local name of the holiday
}