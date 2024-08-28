package com.marketing.weather_forecast.entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}
