package com.marketing.weather_forecast.controller;


import com.marketing.weather_forecast.service.EmailSchedulerService;
import com.marketing.weather_forecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/forecast")
public class WeatherController {

    @Value("${spring.mail.to}")
    private String toEmail;

    private final WeatherService weatherService;
    private final EmailSchedulerService emailSchedulerService;

    @Autowired
    public WeatherController(WeatherService weatherService, EmailSchedulerService emailSchedulerService) {
        this.weatherService = weatherService;
        this.emailSchedulerService = emailSchedulerService;
    }


    @GetMapping("/berlin")
    public String getWeatherDetails() {

        List<String> weatherReports = weatherService.getFormattedWeatherData();

        StringBuilder weatherReportString = new StringBuilder();
        for(String report : weatherReports){
            weatherReportString.append(report).append("\n\n");
        }
        return weatherReportString.toString();
    }

    @PostMapping("/send-mail-weather")
    public ResponseEntity<String> scheduleWeatherEmails(){
        try {
            emailSchedulerService.sendWeatherEmailEvery10Minutes();
            String message = String.format("Weather email scheduling initiated for %s the next 14 days.",toEmail);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to schedule weather emails.");
        }
    }


}
