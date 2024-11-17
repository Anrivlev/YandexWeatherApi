package ru.anrivlev;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class YandexWeatherController {
    private String xYandexWeatherKey;

    private final HttpClient httpClient;

    YandexWeatherController(String xYandexWeatherKey) {
        this.xYandexWeatherKey = xYandexWeatherKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    public String getTemperatureAt(
            double latitude,
            double longitude
    ) throws IOException, InterruptedException {
        String requestUrl = String
                .format(Locale.ENGLISH, "https://api.weather.yandex.ru/v2/forecast?lat=%f&lon=%f",
                        latitude, longitude);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(requestUrl))
                .header(
                        "X-Yandex-Weather-Key",
                        this.xYandexWeatherKey
                )
                .GET()
                .build();
        HttpResponse<String> httpResponse = this.httpClient
                .send(
                        request,
                        HttpResponse.BodyHandlers.ofString());
        return httpResponse.body();
    }

    public double getAverageTemperatureAt(
            double latitude,
            double longitude,
            int limit
    ) throws IOException, InterruptedException {
        String requestUrl = String
                .format(Locale.ENGLISH, "https://api.weather.yandex.ru/v2/forecast?lat=%f&lon=%f&limit=%d",
                        latitude, longitude, limit);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(requestUrl))
                .header(
                        "X-Yandex-Weather-Key",
                        this.xYandexWeatherKey
                )
                .GET()
                .build();
        HttpResponse<String> httpResponse = this.httpClient
                .send(
                        request,
                        HttpResponse.BodyHandlers.ofString());
        String response = httpResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Double> temperatures = new ArrayList<>();
        for (int i =0; i < limit; i++) {
            double temperature = objectMapper
                    .readTree(response)
                    .get("forecasts")
                    .get(i)
                    .get("parts")
                    .get("day")
                    .get("temp_avg")
                    .asDouble();
            temperatures.add(temperature);
        }

        return temperatures.stream().reduce(Double::sum).get() / limit;
    }

    public String getxYandexWeatherKey() {
        return xYandexWeatherKey;
    }

    public void setxYandexWeatherKey(String xYandexWeatherKey) {
        this.xYandexWeatherKey = xYandexWeatherKey;
    }
}
