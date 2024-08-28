package com.marketing.weather_forecast.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {
    private String main;
    private String description;
    private String icon;
}
