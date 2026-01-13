package ryz.github.codiblybackend.model;

public record ChargingWindowDto(
        String startTime,
        String endTime,
        double cleanEnergyPercentage
) {}