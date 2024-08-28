package com.marketing.weather_forecast.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Main {
    private float temp;
    private float temp_min;
    private float temp_max;
}
