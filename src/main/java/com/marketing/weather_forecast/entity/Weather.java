package com.marketing.weather_forecast.entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}
