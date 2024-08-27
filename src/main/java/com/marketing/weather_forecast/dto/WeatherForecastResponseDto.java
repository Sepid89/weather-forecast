package com.marketing.weather_forecast.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class WeatherForecastResponseDto {

    private List<DailyForecast> list;

    @Getter
    @Setter
    public static class DailyForecast{
        private Temp temp;
        private float windSpeed;
        private int humidity;
        private float pressure;
        private float rain;
    }

    @Getter
    @Setter
    public static class Temp {
        private float min;
        private float max;

    }
}
