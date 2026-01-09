package ryz.github.codiblybackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FuelType {

    //Clean fuel sources
    @JsonProperty("biomass") BIOMASS(true),
    @JsonProperty("nuclear") NUCLEAR(true),
    @JsonProperty("hydro") HYDRO(true),
    @JsonProperty("wind") WIND(true),
    @JsonProperty("solar") SOLAR(true),

    //Other fuel sources
    @JsonProperty("gas") GAS(false),
    @JsonProperty("coal") COAL(false),
    @JsonProperty("imports") IMPORTS(false),
    @JsonProperty("other") OTHER(false);

    private final boolean isClean;

    FuelType(boolean isClean) {
        this.isClean = isClean;
    }

    public boolean isClean() {
        return isClean;
    }

}
