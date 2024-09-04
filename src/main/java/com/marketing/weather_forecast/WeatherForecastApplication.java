package com.marketing.weather_forecast;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
        info = @Info(
                title = "Weather Forecast API",
                description = "API Documentation for Weather Forecasting Service used by the Marketing Team",
                version = "1.0",
                contact = @Contact(
                        name = "Sepide Jamshididana",
                        email = "s.jamshididana@gmail.com"
                )
        )
)
public class WeatherForecastApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherForecastApplication.class, args);
    }
}
