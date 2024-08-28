package com.marketing.weather_forecast.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketing.weather_forecast.dto.WeatherForecastResponseDto;
import com.marketing.weather_forecast.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final WebClient webClient;
    private final String API_URL;
    private final String API_KEY;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherService(Environment environment, WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(environment.getProperty("openweathermap.api.url")).build();
        this.API_KEY = environment.getProperty("openweathermap.api.key");
        this.API_URL = environment.getProperty("openweathermap.api.url");
        this.objectMapper = new ObjectMapper();
    }

    public WeatherForecastResponseDto getWeatherForecastForBerlin(){
        try{
            logger.info("Requesting weather data for Berlin...");
            String jsonResponse  = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", "Berlin")
                            .queryParam("cnt", "14")
                            .queryParam("appid", API_KEY)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Received JSON: " + jsonResponse);

            WeatherForecastResponseDto responseDto = objectMapper.readValue(jsonResponse, WeatherForecastResponseDto.class);
            System.out.println("Deserialized DTO: " + responseDto);
            return responseDto;
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadRequestException("Invalid request parameters");
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("Requested resource not found");
            }
            throw new ClientErrorException(e.getMessage());
        }catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new InternalServerErrorException("Server encountered an error");
            } else if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new ServiceUnavailableException("Service is temporarily unavailable");
            }
            throw new ServerErrorException(e.getMessage());
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFormattedWeatherForBerlin(){

        WeatherForecastResponseDto responseDto = getWeatherForecastForBerlin();

        if (responseDto == null){
            throw new RuntimeException("Failed to retrieve weather data!");
        }

        float minTempCelsius = responseDto.getMain().getTemp_min() - 273.15f;
        float maxTempCelsius = responseDto.getMain().getTemp_max() - 273.15f;

        String weatherDescription = responseDto.getWeather().get(0).getDescription();

        float windVelocity = responseDto.getWind().getSpeed();

        long dateTime = responseDto.getDt();
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateTime), ZoneId.of("Europe/Berlin"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = date.format(formatter);

        float chanceOfPrecipitation = responseDto.getPop()*100;

        return String.format("Weather in Berlin on %s:\nMin Temp: %.2f°C\nMax Temp: %.2f°C\nDescription: %s\nWind Velocity: %.2f m/s\nChance of Precipitation: %.2f%%",
                formattedDate,
                minTempCelsius,
                maxTempCelsius,
                weatherDescription,
                windVelocity,
                chanceOfPrecipitation);
    }
}
