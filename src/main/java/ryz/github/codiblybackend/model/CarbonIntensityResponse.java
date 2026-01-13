package ryz.github.codiblybackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CarbonIntensityResponse(
        @JsonProperty("data")
        List<GenerationData> generationData
) {
}
