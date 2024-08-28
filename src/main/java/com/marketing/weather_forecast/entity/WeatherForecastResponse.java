package com.marketing.weather_forecast.entity;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherForecastResponse {
    private List<DailyForecast> list;
}
