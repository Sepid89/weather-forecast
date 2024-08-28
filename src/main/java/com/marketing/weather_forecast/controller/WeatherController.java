package com.marketing.weather_forecast.controller;


import com.marketing.weather_forecast.service.EmailSchedulerService;
import com.marketing.weather_forecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/forecast")
public class WeatherController {

    private final WeatherService weatherService;
    private final EmailSchedulerService emailSchedulerService;

    @Autowired
    public WeatherController(WeatherService weatherService, EmailSchedulerService emailSchedulerService) {
        this.weatherService = weatherService;
        this.emailSchedulerService = emailSchedulerService;
    }


    @GetMapping("/berlin")
    public ResponseEntity<String> getWeatherDetails() {
        try {
            String weatherReport = weatherService.getFormattedWeatherForBerlin();
            return ResponseEntity.ok(weatherReport);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve weather data.");
        }
    }

    @PostMapping("/send-mail-weather")
    public ResponseEntity<String> scheduleWeatherEmails(){
        try {
            emailSchedulerService.sendWeatherEmailEvery10Minutes();
            return ResponseEntity.ok("Weather email scheduling initiated for sepide@yahoo.com for the next 14 days.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to schedule weather emails.");
        }
    }


}
