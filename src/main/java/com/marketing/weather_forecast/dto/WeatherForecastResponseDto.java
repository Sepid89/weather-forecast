package com.marketing.weather_forecast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastResponseDto {
    private List<WeatherDto> weather;
    private MainDto main;
    private WindDto wind;
    private float pop;
    private long dt;
    private String name;
}
