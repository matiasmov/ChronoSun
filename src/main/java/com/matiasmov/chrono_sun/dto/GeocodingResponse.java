package com.matiasmov.chrono_sun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record GeocodingResponse(
    String lat,
    String lon,
    String display_name
) {}