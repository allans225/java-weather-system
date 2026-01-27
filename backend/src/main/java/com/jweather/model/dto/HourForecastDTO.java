package com.jweather.model.dto;

public record HourForecastDTO(
        String dateTimeStr,
        Integer currentTempC,
        String condition) {
}
