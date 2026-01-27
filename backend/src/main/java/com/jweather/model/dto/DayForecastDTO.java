package com.jweather.model.dto;

public record DayForecastDTO(
        Integer maximumTempC,
        Integer minimumTempC,
        String weekDay,
        String sunset,
        String sunrise) {
}
