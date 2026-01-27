package com.jweather.controller;

import com.jweather.model.dto.WeatherResponse;
import com.jweather.service.WeatherProvider;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*") // Permite que o React acesse o Spring
public class WeatherController {
    private final WeatherProvider weatherProvider;

    public WeatherController(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    @GetMapping("/{city}")
    public WeatherResponse getWeather(@PathVariable String city) {
        return weatherProvider.fetchCurrentWeather(city);
    }
}
