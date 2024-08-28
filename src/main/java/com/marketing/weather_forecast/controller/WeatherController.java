package com.marketing.weather_forecast.controller;

import com.marketing.weather_forecast.entity.WeatherForecastResponse;
import com.marketing.weather_forecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/forecast", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

//    @GetMapping("/sendForecastEmail")
//    public ResponseEntity<?> sendForecastEmail() {
//        // Fetch the weather forecast for Berlin
//        WeatherForecastResponseDto weatherForecast = weatherService.getWeatherForecastForBerlin();
//
//        WeatherResponseDto weather = weatherService.getWeatherForBerlin();
//        return ResponseEntity.ok(weather);
//        // Prepare the email content
////        String emailContent = emailService.prepareEmailContent(weatherForecast);
//
//        // Send the email
////        emailService.sendWeatherForecastEmail("sepidejamshididana@yahoo.com", "Weather Forecast for Berlin", emailContent);
//
//        // return new ResponseEntity<String>(HttpStatusCode.valueOf(200));
//    }

    @GetMapping("/currentWeather")
    public ResponseEntity<WeatherForecastResponse> getCurrentWeather() {
        WeatherForecastResponse weather = weatherService.getWeatherForecastForBerlin();
        return ResponseEntity.ok(weather);
    }
}
