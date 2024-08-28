package com.marketing.weather_forecast.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWeatherForecastEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

//    public String prepareEmailContent(WeatherForecastResponseDto weatherForecast) {
//        StringBuilder content = new StringBuilder();
//        content.append("Weather Forecast for Berlin (next 14 days):\n\n");
//        for (WeatherForecastResponseDto.DailyForecast daily : weatherForecast.getList()) {
//            content.append(String.format("Day: Min Temp: %.2f°C, Max Temp: %.2f°C, Wind: %.2f m/s\n",
//                    daily.getTemp().getMin(), daily.getTemp().getMax(), daily.getWindSpeed()));
//        }
//        return content.toString();
//    }
}
