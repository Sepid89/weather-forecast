package com.marketing.weather_forecast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastResponseDto {

  private double latitude;
  private double longitude;
  private String resolvedAddress;
  private String address;
  private String timezone;
  private double tzoffset;
  private String description;
  private List<DayDto> days;
}
