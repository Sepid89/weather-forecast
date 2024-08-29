package com.marketing.weather_forecast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayDto {
    private String datetime;
    private float tempmax;
    private float tempmin;
    private String description;
    private float windspeed;
    private float precipprob;
}
