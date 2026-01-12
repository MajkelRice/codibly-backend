package ryz.github.codiblybackend.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ryz.github.codiblybackend.model.CarbonIntensityResponse;

import java.time.Instant;
import java.time.ZoneId;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class CarbonIntensityClient {
    private final RestClient restClient;
    private static final String API_URL = "https://api.carbonintensity.org.uk";

    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm'Z'")
            .withZone(ZoneId.of("UTC"));

    public CarbonIntensityClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(API_URL)
                .build();
    }

    public CarbonIntensityResponse getGeneration(Instant from, Instant to) {
        String fromIso = API_DATE_FORMATTER.format(from.truncatedTo(ChronoUnit.HOURS));
        String toIso = API_DATE_FORMATTER.format(to.truncatedTo(ChronoUnit.HOURS));

        String urlString = String.format("/generation/%s/%s", fromIso, toIso);

        System.out.println("Force calling URI: " + API_URL + urlString);

        return restClient.get()
                .uri(URI.create(API_URL + urlString))
                .retrieve()
                .body(CarbonIntensityResponse.class);
    }
}