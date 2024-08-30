package com.marketing.weather_forecast.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailSchedulerService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final Environment environment;


    /**
     * Sends an email containing weather reports.
     *
     * @param email          The recipient's email address.
     * @param weatherReports The list of formatted weather reports to be included in the email.
     * @throws MessagingException If there is an error in the email sending process.
     */
    public void sendEmailWeatherReport(String email, List<String> weatherReports) throws MessagingException {

        MimeMessage message = createMimeMessage(email, getEmailRecipient(), weatherReports);

        mailSender.send(message);
    }

    /**
     * Retrieves the recipient email address from the environment properties.
     *
     * @return The recipient email address as a String.
     */
    private String getEmailRecipient() {

        return environment.getProperty("spring.mail.to");
    }

    /**
     * Creates a MimeMessage object with the given email details.
     *
     * @param email          The recipient's email address.
     * @param emailTo        The recipient name or address to be included in the email content.
     * @param weatherReports The list of formatted weather reports to be included in the email.
     * @return A MimeMessage object ready to be sent.
     * @throws MessagingException If there is an error while creating the MimeMessage.
     */
    private MimeMessage createMimeMessage(String email, String emailTo, List<String> weatherReports) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        String context = generateEmailContent(emailTo, weatherReports);

        helper.setTo(email);
        helper.setSubject("14-Day Weather Report Of Berlin");
        helper.setText(context, true);
        helper.setFrom(new InternetAddress(getEmailRecipient()));

        return message;
    }


    /**
     * Generates the content of the email using Thymeleaf template engine.
     *
     * @param emailTo        The recipient name or address to be included in the email content.
     * @param weatherReports The list of formatted weather reports to be included in the email.
     * @return A String containing the processed HTML content of the email.
     */
    private String generateEmailContent(String emailTo, List<String> weatherReports) {
        Context context = new Context();
        context.setVariable("username", emailTo);
        context.setVariable("weatherReports", weatherReports);

        return templateEngine.process("email_template", context);
    }
}
