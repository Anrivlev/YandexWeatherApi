package ru.anrivlev;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String xYandexWeatherKey = "d9828626-6d5a-4ad3-8abf-a1143419431e";
        YandexWeatherController weatherController = new YandexWeatherController(xYandexWeatherKey);

        double latitude = 55.636717;
        double longitude = 37.665999;

        String temperatureJson = weatherController.getTemperatureAt(latitude, longitude);
        ObjectMapper objectMapper = new ObjectMapper();
        double temperature = objectMapper
                .readTree(temperatureJson)
                .get("fact")
                .get("temp")
                .asDouble();

        System.out.printf("Temperature JSON: %s\n", temperatureJson);
        System.out.printf("Temperature: %f\n", temperature);

        int limit = 5;

        double averageTemperature = weatherController.getAverageTemperatureAt(
                latitude,
                longitude,
                limit
                );
        System.out.printf("Average temperature: %f\n", averageTemperature);
    }
}