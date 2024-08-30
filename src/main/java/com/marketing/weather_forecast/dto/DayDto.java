package com.marketing.weather_forecast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayDto {

    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty("tempmax")
    private float tempMax;

    @JsonProperty("tempmin")
    private float tempMin;

    private String description;

    @JsonProperty("windspeed")
    private float windSpeed;

    private List<HourDto> hours;
}
