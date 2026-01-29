package com.jweather.infrastructure.client;

import com.jweather.model.dto.CurrentWeatherDTO;
import com.jweather.model.dto.LocationDTO;
import com.jweather.model.dto.WeatherResponse;
import com.jweather.service.WeatherProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class WeatherApiAdapter implements WeatherProvider {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    private String getUrl(String city) {
        return String.format("http://api.weatherapi.com/v1/current.json?key=%s&q=%s&lang=pt", apiKey, city);
    }

    @Override
    public WeatherResponse fetchCurrentWeather(String city) {
        // Faz a chamada e recebe como um Map genérico para extrair os dados
        Map<String, Object> response = restTemplate.getForObject(getUrl(city), Map.class);

        // Extrair e mapear Location
        Map<String, Object> locMap = (Map<String, Object>) response.get("location");
        LocationDTO location = new LocationDTO(
                (String) locMap.get("country"),
                (String) locMap.get("name"),
                (Double) locMap.get("lat"),
                (Double) locMap.get("lon")
        );

        // Extrair e mapear CurrentWeather
        Map<String, Object> curMap = (Map<String, Object>) response.get("current");
        Map<String, Object> condMap = (Map<String, Object>) curMap.get("condition");

        CurrentWeatherDTO current = new CurrentWeatherDTO(
                Double.valueOf(curMap.get("uv").toString()),

                // Converte para Double primeiro e depois pega o valor inteiro
                Double.valueOf(curMap.get("temp_c").toString()).intValue(),
                Double.valueOf(curMap.get("feelslike_c").toString()).intValue(),

                (String) condMap.get("text"),
                Double.valueOf(curMap.get("vis_km").toString()),
                Double.valueOf(curMap.get("wind_kph").toString()),
                (Integer) curMap.get("humidity"),
                Double.valueOf(curMap.get("pressure_mb").toString())
        );

        // Retorna o objeto centralizado (listas vazias por enquanto)
        return new WeatherResponse(current, null, null, location);
    }
}
