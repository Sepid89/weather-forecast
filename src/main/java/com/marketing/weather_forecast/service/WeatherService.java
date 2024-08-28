package com.marketing.weather_forecast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketing.weather_forecast.entity.WeatherForecastResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    private final String API_URL;
    private final String API_KEY;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherService(Environment environment, RestTemplateBuilder restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate.build();
        this.API_KEY = environment.getProperty("openweathermap.api.key");
        this.API_URL = environment.getProperty("openweathermap.api.url");
        this.objectMapper = objectMapper;
    }

    public WeatherForecastResponse getWeatherForecastForBerlin(){
        try{
            String url = API_URL + "&appid=" + API_KEY;
            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);

            return objectMapper.readValue(response.getBody(), WeatherForecastResponse.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.UNAUTHORIZED){
                throw new RuntimeException("Invalid API KEY provided!");
            }throw e;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
