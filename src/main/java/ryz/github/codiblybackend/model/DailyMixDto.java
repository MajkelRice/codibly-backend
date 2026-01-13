package ryz.github.codiblybackend.model;

import java.time.LocalDate;
import java.util.Map;

public record DailyMixDto(
        LocalDate date,
        double cleanEnergyPercentage,
        Map<FuelType, Double> fuelMix
) {}