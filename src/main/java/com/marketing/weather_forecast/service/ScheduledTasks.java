package com.marketing.weather_forecast.service;

import com.marketing.weather_forecast.dto.WeatherForecastResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasks {
    private final WeatherService weatherService;
    private final EmailService emailService;

    @Autowired
    public ScheduledTasks(WeatherService weatherService, EmailService emailService) {
        this.weatherService = weatherService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 7 * * ?") // Every day at 7 AM
    public void sendDailyWeatherForecast() {
        WeatherForecastResponseDto forecast = weatherService.getWeatherForecastForBerlin();
        String emailContent = emailService.prepareEmailContent(forecast);
        emailService.sendWeatherForecastEmail("marketing@deefinity.com", "14-Day Weather Forecast", emailContent);
    }
}
