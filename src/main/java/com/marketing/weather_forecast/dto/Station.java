package com.marketing.weather_forecast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
    private double distance;
    private double latitude;
    private double longitude;
    private int useCount;
    private String id;
    private String name;
    private int quality;
    private double contribution;
}