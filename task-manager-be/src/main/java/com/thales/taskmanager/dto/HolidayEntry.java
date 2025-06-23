package com.thales.taskmanager.dto;

import lombok.Data;

@Data
public class HolidayEntry {

    // Properties of Nager.Date API response
    private String date;
    private String localName;
    private String name;
    private boolean fixed;
    private boolean global;
    private String[] counties;
    private Integer launchYear;

}