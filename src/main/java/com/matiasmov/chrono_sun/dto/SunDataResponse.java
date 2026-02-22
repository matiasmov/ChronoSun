package com.matiasmov.chrono_sun.dto;

public record SunDataResponse(
    Results results,
    String status,
    String timeZoneId 
) {
    public record Results(
        String sunrise,
        String sunset,
        String day_length,
        String solar_noon
    ) {}
}