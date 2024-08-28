package com.marketing.weather_forecast.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
@EnableScheduling
public class EmailSchedulerService {

    private final WeatherService weatherService;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.to}")
    private String TO;

    @Autowired
    public EmailSchedulerService(WeatherService weatherService, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine){
        this.weatherService = weatherService;
        this.mailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Scheduled(fixedRate = 600000) // 10 min
    //@Scheduled(cron = "0 0 15 28-31 8 0")
    public void sendWeatherEmailEvery10Minutes() throws MessagingException {
        String email = "sepidejamshididana@yahoo.com";
        String weatherReport = weatherService.getFormattedWeatherForBerlin();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariable("username", TO);
        context.setVariable("weatherReport", weatherReport);

        // Assuming "email_html.html" is in the templates folder
        String process = templateEngine.process("email_html", context);

        helper.setTo(email);
        helper.setSubject("Scheduled Weather Report Of Berlin");
        helper.setText(process, true);
        helper.setFrom(new InternetAddress(TO));

        mailSender.send(message);

        System.out.println("Email sent to " + email + " with weather report.");
    }
}
