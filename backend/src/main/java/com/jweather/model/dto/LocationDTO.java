package com.jweather.model.dto;

public record LocationDTO(
        String country,
        String city,
        Double latitude,
        Double longitude) {
}
