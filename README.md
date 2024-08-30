# Wettervorhersage Applikation

## Übersicht
Diese Spring Boot Applikation bietet eine tägliche Wettervorhersage für die nächsten 14 Tage. Jeden Morgen um 8:00 Uhr wird eine E-Mail mit den Wetterdaten an eine vorgegebene E-Mail-Adresse gesendet. Die Daten werden von einer externen Wetter-API (z.B. Visual Crossing) abgerufen und in einem benutzerfreundlichen E-Mail-Template präsentiert.

## Voraussetzungen

- Java 17 oder höher
- Maven 3.6.0 oder höher
- Zugang zu einer Wetter-API (z.B. Visual Crossing)
- E-Mail-Konto (z.B. Gmail) zur Verwendung von SMTP für den E-Mail-Versand

## Installation

1. **Projekt klonen**:
   ```bash
   git clone https://github.com/Sepid89/weather-forecast.git
   cd wettervorhersage-applikation
