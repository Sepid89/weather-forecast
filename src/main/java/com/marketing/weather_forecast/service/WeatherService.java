package com.marketing.weather_forecast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketing.weather_forecast.dto.DayDto;
import com.marketing.weather_forecast.dto.HourDto;
import com.marketing.weather_forecast.dto.WeatherForecastResponseDto;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@EnableScheduling
@AllArgsConstructor
public class WeatherService {

    private final ObjectMapper objectMapper;
    private final EmailSchedulerService emailSchedulerService;
    private final Environment environment;

    /**
     * Sends an email with the weather data every day at 8:00 AM.
     *
     * <p>This method is scheduled to run automatically at 8:00 AM every day.
     * It gets the weather data , formats it, and sends it to a specific email address.
     *
     * @throws InternalException if there is an error while sending the email.
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void sendEmail() {

        List<String> weatherReports = getFormattedWeatherData();

        try {
            emailSchedulerService.sendEmailWeatherReport(weatherReports);
        } catch (Exception e) {
            throw new InternalException("Failed to schedule weather emails.");
        }
    }

    /**
     * Fetches the weather data from the Visual Crossing API.
     *
     * @return JSON response as a String.
     */
    private String fetchWeatherData() {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(14);

        HttpClient httpClient = HttpClient.newHttpClient();
        String apiKey = environment.getProperty("visualCrossing.api.key");
        String baseUrl = environment.getProperty("weather.api.base-url");
        String unitGroup = environment.getProperty("weather.api.unit-group");
        String contentType = environment.getProperty("weather.api.content-type");
        String city = environment.getProperty("weather.api.default-city");

        String uri = String.format(
                "%s/%s/%s/%s?unitGroup=%s&key=%s&contentType=%s",
                baseUrl, city, startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                unitGroup, apiKey, contentType);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
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
            return objectMapper.readValue(jsonResponse, WeatherForecastResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize weather data", e);
        }
    }

    /**
     * Retrieves and formats the weather data for Berlin.
     *
     * @return A list of formatted weather data strings.
     */
    private List<String> getFormattedWeatherData() {
        WeatherForecastResponseDto responseDto = deserializeWeatherData(fetchWeatherData());

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

        String weatherDescription = forecast.getDescription();

        if (weatherDescription.isEmpty()) {
            weatherDescription = "Weather prediction not available.";
        }

        return """
                %s (%s):
                Min Temp: %.2f°C
                Max Temp: %.2f°C
                Description: %s
                Wind Velocity: %.2f m/s
                Chance of Precipitation: %.2f%%
                """.formatted(forecast.getDatetime(), getDayName(forecast.getDatetime()), forecast.getTempMin(),
                forecast.getTempMax(), weatherDescription, forecast.getWindSpeed(), getAveragePrecipitationProb(forecast));
    }

    /**
     * Calculates the average chance of precipitation for the day.
     *
     * @param forecast The DailyForecast object containing hourly precipitation data.
     * @return The average precipitation probability.
     */
    private static float getAveragePrecipitationProb(DayDto forecast) {
        List<HourDto> hourDtoList = forecast.getHours();
        if (hourDtoList == null || hourDtoList.isEmpty())
            return 0;

        float totalPrecipitationProb = (float) hourDtoList
                .stream()
                .mapToDouble(HourDto::getPrecipitationProb)
                .sum();

        return totalPrecipitationProb / hourDtoList.size();
    }

    /**
     * Retrieves the name of the day for a given date.
     *
     * @param date The date string in "yyyy-MM-dd" format.
     * @return The name of the day in English.
     */
    private String getDayName(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}