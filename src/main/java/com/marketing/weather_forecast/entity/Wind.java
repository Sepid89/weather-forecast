package com.marketing.weather_forecast.entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Wind {
    private float speed;
    private int deg;
}
