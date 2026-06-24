package org.wildfly.quickstart.mcp;

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class WeatherClient {

    private static final String REST_URI = "https://api.weather.gov";

    private static final Client HTTP_CLIENT = ClientBuilder.newClient()
            .property("dev.resteasy.client.follow.redirects", "true");

    Alerts getAlerts(String state) {
        try (Response response = HTTP_CLIENT.target(REST_URI)
                .path("/alerts/active/area/%s".formatted(state))
                .request(MediaType.APPLICATION_JSON).get();) {
            return response.readEntity(Alerts.class);
        }
    }

    JsonObject getPoints(double latitude, double longitude) {
        DecimalFormat decimal = new DecimalFormat("##.####", DecimalFormatSymbols.getInstance(Locale.US));
        String endpoint = "/points/%s,%s".formatted(
                decimal.format(latitude),
                decimal.format(longitude));
        Response response = HTTP_CLIENT.target(REST_URI)
                .path(endpoint)
                .request(MediaType.APPLICATION_JSON)
                .get();
        return response.readEntity(JsonObject.class);
    }

    Forecast getForecast(String url) {
        return HTTP_CLIENT.target(url)
                .request(MediaType.APPLICATION_JSON)
                .get(Forecast.class);
    }

    public record Properties(
            String id,
            String areaDesc,
            String event,
            String severity,
            String description,
            String instruction) {}

    public record Feature(
            String id,
            String type,
            Object geometry,
            Properties properties) {}

    public record Alerts(
            List<String> context,
            String type,
            List<Feature> features,
            String title,
            String updated) {}

    public record Period(
            String name,
            int temperature,
            String temperatureUnit,
            String windSpeed,
            String windDirection,
            String detailedForecast) {}

    public record ForecastProperties(
            List<Period> periods) {}

    public record Forecast(
            ForecastProperties properties) {}
}
