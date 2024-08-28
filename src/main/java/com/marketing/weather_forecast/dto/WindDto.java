package com.marketing.weather_forecast.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WindDto {
    private float speed;
    private int deg;
}
