package ryz.github.codiblybackend.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ryz.github.codiblybackend.model.CarbonIntensityResponse;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class CarbonIntensityClient {
    private final RestClient restClient;
    private static final String API_URL = "https://api.carbonintensity.org.uk/generation";

    public CarbonIntensityClient() {
        this.restClient = RestClient.builder()
                .baseUrl(API_URL)
                .build();
    }

    public CarbonIntensityResponse getGeneration(Instant from, Instant to) {
        String fromIso = DateTimeFormatter.ISO_INSTANT.format(from.truncatedTo(ChronoUnit.MINUTES));
        String toIso = DateTimeFormatter.ISO_INSTANT.format(to.truncatedTo(ChronoUnit.MINUTES));

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/from/{from}/to/{to}")
                        .build(fromIso, toIso))
                .retrieve()
                .body(CarbonIntensityResponse.class);
    }
}