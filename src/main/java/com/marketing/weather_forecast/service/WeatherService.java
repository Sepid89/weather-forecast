package com.marketing.weather_forecast.service;

import com.marketing.weather_forecast.dto.WeatherForecastResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    private final String URL = "https://api.openweathermap.org/data/2.5/forecast/daily?q=Berlin,de&cnt=14";
    private final String API_ID = "aa0f2c38f44fbf6ca887943bf704444f";

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherForecastResponseDto getWeatherForecastForBerlin(){
        ResponseEntity<WeatherForecastResponseDto> response = restTemplate.getForEntity(this.url(), WeatherForecastResponseDto.class);
        return response.getBody();
    }

    private String url(){
        return String.format(URL.concat("&appid=%s"),API_ID);
    }
}
