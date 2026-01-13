package ryz.github.codiblybackend.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ryz.github.codiblybackend.model.CarbonIntensityResponse;
import ryz.github.codiblybackend.util.DateUtils;

import java.time.Instant;
import java.net.URI;
import java.time.temporal.ChronoUnit;

@Component
public class CarbonIntensityClient {
    private final RestClient restClient;
    private static final String API_URL = "https://api.carbonintensity.org.uk";


    public CarbonIntensityClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(API_URL)
                .build();
    }

    public CarbonIntensityResponse getGeneration(Instant from, Instant to) {
        String fromIso = DateUtils.API_DATE_FORMATTER.format(from.truncatedTo(ChronoUnit.HOURS));
        String toIso = DateUtils.API_DATE_FORMATTER.format(to.truncatedTo(ChronoUnit.HOURS));

        String urlString = String.format("/generation/%s/%s", fromIso, toIso);

        System.out.println("Force calling URI: " + API_URL + urlString);

        return restClient.get()
                .uri(URI.create(API_URL + urlString))
                .retrieve()
                .body(CarbonIntensityResponse.class);
    }
}