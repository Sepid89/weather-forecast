package com.marketing.weather_forecast.controller;


import com.marketing.weather_forecast.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/forecast")
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/send-mail-weather")
    public void scheduleWeatherEmails() {
            weatherService.sendEmail();
    }
}
