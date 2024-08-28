package com.marketing.weather_forecast.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException  extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}