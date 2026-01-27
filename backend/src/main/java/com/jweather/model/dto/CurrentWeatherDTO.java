package com.jweather.model.dto;

public record CurrentWeatherDTO(
        Double uv,
        Integer currentTempC,
        Integer thermalSensationC,
        String condition,
        Double visibilityKm,
        Double windKph,
        Integer humidity,
        Double pressureMb) {
}
