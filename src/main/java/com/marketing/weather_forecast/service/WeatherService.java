package com.marketing.weather_forecast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketing.weather_forecast.dto.DayDto;
import com.marketing.weather_forecast.dto.HourDto;
import com.marketing.weather_forecast.dto.WeatherForecastResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final HttpClient httpClient;
    private final String API_KEY;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherService(Environment environment, ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.API_KEY = environment.getProperty("visualcrossing.api.key");
        this.objectMapper = objectMapper;
    }

    /**
     * Fetches the weather data from the Visual Crossing API.
     *
     * @return JSON response as a String.
     */
    private String fetchWeatherData() {
        try {
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(15);
            String formattedStartDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            System.out.println("Start date : " + formattedStartDate);
            System.out.println("End date : " + formattedEndDate);

            String uri = String.format(
                    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Berlin/%s/%s?unitGroup=metric&key=%s&contentType=json",
                    formattedStartDate, formattedEndDate, API_KEY);
            System.out.println(uri);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            logger.error("Failed to fetch weather data", e);
            throw new RuntimeException("Failed to fetch weather data.", e);
        }
    }


    /**
     * Deserializes the JSON response into a WeatherForecastResponseDto object.
     *
     * @param jsonResponse JSON response as a String.
     * @return Deserialized WeatherForecastResponseDto object.
     */
    private WeatherForecastResponseDto deserializeWeatherData(String jsonResponse) {
        try {
            logger.info("Deserializing weather data...");
            return objectMapper.readValue(jsonResponse, WeatherForecastResponseDto.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize weather data", e);
            throw new RuntimeException("Failed to deserialize weather data", e);
        }
    }


    public List<String> getFormattedWeatherForBerlin() {
        String jsonResponse = fetchWeatherData();
        WeatherForecastResponseDto responseDto = deserializeWeatherData(jsonResponse);

        if (responseDto == null || responseDto.getDays().isEmpty()) {
            throw new RuntimeException("Failed to retrieve weather data!");
        }

        List<String> formattedWeatherReports = new ArrayList<>();
        for (DayDto forecast : responseDto.getDays()) {
            formattedWeatherReports.add(formatWeatherData(forecast));
        }

        return formattedWeatherReports;
    }

    /**
     * Formats the weather data for a single day into a human-readable string.
     *
     * @param forecast DailyForecast object representing the weather data for a single day.
     * @return Formatted weather data string.
     */
    private String formatWeatherData(DayDto forecast) {
        String formattedDate = forecast.getDatetime();
        float minTempCelsius = forecast.getTempmin();
        float maxTempCelsius = forecast.getTempmax();
        String weatherDescription = forecast.getDescription();
        float windVelocity = forecast.getWindspeed();

        float averagePrecipProb = 0;

        // Check if the hours list is not null and contains data
        if (forecast.getHours() != null && !forecast.getHours().isEmpty()) {
            float totalPrecipProb = 0;
            int count = 0;

            System.out.println(count);

            for (HourDto hour : forecast.getHours()) {
                totalPrecipProb += hour.getPrecipprob();
                count++;
            }

            // Calculate the average precipprob
            averagePrecipProb = count > 0 ? totalPrecipProb / count : 0;
        }

        if(weatherDescription.isEmpty()){
            weatherDescription ="Weather prediction not available.";
        }

        String outPut = String.format("%s:\nMin Temp: %.2f°C\nMax Temp: %.2f°C\nDescription: %s\nWind Velocity: %.2f m/s\nChance of Precipitation: %.2f%%",
                formattedDate, minTempCelsius, maxTempCelsius, weatherDescription, windVelocity, averagePrecipProb);
        System.out.println(outPut);

        return outPut;
    }


    /**
     * Converts temperature from Fahrenheit to Celsius.
     *
     * @param fahrenheit Temperature in Fahrenheit.
     * @return Temperature in Celsius.
     */
    private float convertFahrenheitToCelsius(float fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }
}