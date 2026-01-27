package com.jweather.model.dto;

import java.util.List;

public record WeatherResponse (
        CurrentWeatherDTO currentWeather,
        List<DayForecastDTO> dayForecast,
        List<HourForecastDTO> hourForecast,
        LocationDTO location) {
}
