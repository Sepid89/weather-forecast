package com.marketing.weather_forecast.controller;


import com.marketing.weather_forecast.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Weather Forecast Email Sender",
        description = "Handles the scheduling and sending of weather forecast emails"
)
@RestController
@RequestMapping(path = "/forecast")
@AllArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(
            summary = "Send Weather Forecast Email",
            description = "Triggers the sending of a weather forecast email for the next 14 days"
    )
    @PostMapping("/send-mail-weather")
    public void scheduleWeatherEmails() {
        weatherService.sendEmail();
    }
}
