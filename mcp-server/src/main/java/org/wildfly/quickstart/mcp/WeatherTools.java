package org.wildfly.quickstart.mcp;


import org.mcp_java.server.tools.Tool;
import org.mcp_java.server.tools.ToolArg;

import java.util.stream.Collectors;

/**
 * Example of "open-world" MCP tools that relies on an external system
 * (provided by <a href="https://api.weather.gov">...</a>) to provide
 * weather alerts and forecasts in the USA.
 */
public class WeatherTools {

    private final static WeatherClient WEATHER = new WeatherClient();

    @Tool(description = "Get weather alerts for a US state.",
            name = "alerts",
            annotations = @Tool.Annotations(readOnlyHint = true, destructiveHint = false))
    public String getAlerts(
            @ToolArg(description = "Two-letter US state code (e.g. CA, NY)") String state) {
        return formatAlerts(WEATHER.getAlerts(state));
    }

    @Tool(description = "Get weather forecast for a location.",
            annotations = @Tool.Annotations(readOnlyHint = true, destructiveHint = false))
    public String forecast(
            @ToolArg(description = "Latitude of the location") double latitude,
            @ToolArg(description = "Longitude of the location") double longitude) {
        var points = WEATHER.getPoints(latitude, longitude);
        var url = points.get("properties").asJsonObject().getString("forecast");

        return formatForecast(WEATHER.getForecast(url));
    }

    private static String formatForecast(WeatherClient.Forecast forecast) {
        return forecast.properties().periods().stream()
                .map(WeatherTools::formatPeriod)
                .collect(Collectors.joining("\n---\n"));
    }

    private static String formatPeriod(WeatherClient.Period period) {
        return """
                    Temperature: %d°%s
                    Wind: %s %s
                    Forecast: %s
                """.formatted(period.temperature(), period.temperatureUnit(),
                period.windSpeed(), period.windDirection(),
                period.detailedForecast());
    }

    private static String formatAlerts(WeatherClient.Alerts alerts) {
        return alerts.features().stream()
                .map(feature -> formatProperties(feature.properties()))
                .collect(Collectors.joining("\n---\n"));
    }

    private static String formatProperties(WeatherClient.Properties p) {
        return """
                    Event: %s
                    Area: %s
                    Severity: %s
                    Description: %s
                    Instructions: %s
               """.formatted(p.event(), p.areaDesc(), p.severity(), p.description(), p.instruction());
    }
}
