package ryz.github.codiblybackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GenerationData(
        String from,
        String to,
        @JsonProperty("generationmix") List<GenerationMix> generationMix
) {
}
