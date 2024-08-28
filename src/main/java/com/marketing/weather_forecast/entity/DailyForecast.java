package com.marketing.weather_forecast.entity;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DailyForecast {

        private Main main;
        private List<Weather> weather;
        private Clouds clouds;
        private Wind wind;
        private Sys sys;
        private String dt_txt;
    }
