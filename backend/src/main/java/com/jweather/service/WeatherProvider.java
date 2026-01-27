package com.jweather.service;

import com.jweather.model.dto.WeatherResponse;

public interface WeatherProvider {
    WeatherResponse fetchCurrentWeather(String city);
}
