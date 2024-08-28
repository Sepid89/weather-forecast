package com.marketing.weather_forecast.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class EmailSchedulerService {

    private final WeatherService weatherService;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailSchedulerService(WeatherService weatherService, JavaMailSender javaMailSender){
        this.weatherService = weatherService;
        this.mailSender = javaMailSender;
    }

    @Scheduled(fixedRate = 600000) // 10 min
    public void sendWeatherEmailEvery10Minutes(){
        String email = "sepidejamshididana@yahoo.com";
        String weatherReport = weatherService.getFormattedWeatherForBerlin();


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Scheduled Weather Report");
        mailMessage.setText(weatherReport);
        mailSender.send(mailMessage);

        System.out.println("Email sent to " + email + " with weather report.");
    }
}
