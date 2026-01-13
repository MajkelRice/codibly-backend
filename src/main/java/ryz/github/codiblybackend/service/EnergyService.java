package ryz.github.codiblybackend.service;

import org.springframework.stereotype.Service;
import ryz.github.codiblybackend.model.*;
import ryz.github.codiblybackend.repository.CarbonIntensityRepository;
import ryz.github.codiblybackend.util.DateUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnergyService {
    private final CarbonIntensityRepository carbonIntensityRepository;

    public EnergyService(CarbonIntensityRepository carbonIntensityRepository) {
        this.carbonIntensityRepository = carbonIntensityRepository;
    }

    public List<DailyMixDto> getDailyMix() {
        Instant now = Instant.now();
        Instant threeDaysLater = now.plus(3, ChronoUnit.DAYS);

        List<GenerationData> data = carbonIntensityRepository.getGeneration(now, threeDaysLater);

        Map<String, List<GenerationData>> groupedByDay = data.stream()
                .collect(Collectors.groupingBy(d -> {
                    Instant instant = DateUtils.API_DATE_FORMATTER.parse(d.from(), Instant::from);

                    return instant.atZone(ZoneId.of("Europe/London"))
                            .toLocalDate()
                            .toString();
                }));

        return groupedByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .limit(3)
                .map(entry -> calculateDailyAverage(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public ChargingWindowDto findBestChargingWindow(int hours) {
        if (hours < 1 || hours > 6) {
            throw new IllegalArgumentException("Charging window must be between 1 and 6 hours");
        }

        Instant now = Instant.now();
        Instant twoDaysLater = now.plus(2, ChronoUnit.DAYS);
        List<GenerationData> data = carbonIntensityRepository.getGeneration(now, twoDaysLater);

        int slotsNeeded = hours * 2;

        if (data.size() < slotsNeeded) {

            throw new IllegalStateException("Not enough data points from API to calculate window");
        }

        double maxAvgCleanEnergy = -1.0;
        GenerationData bestStartInterval = null;
        GenerationData bestEndInterval = null;

        for (int i = 0; i <= data.size() - slotsNeeded; i++) {
            List<GenerationData> window = data.subList(i, i + slotsNeeded);

            double currentAvg = window.stream()
                    .mapToDouble(this::calculateCleanEnergyPercentage)
                    .average()
                    .orElse(0.0);

            if (currentAvg > maxAvgCleanEnergy) {
                maxAvgCleanEnergy = currentAvg;
                bestStartInterval = window.get(0);
                bestEndInterval = window.get(window.size() - 1);
            }
        }

        if (bestStartInterval == null) return null;

        return new ChargingWindowDto(
                bestStartInterval.from(),
                bestEndInterval.to(),
                Math.round(maxAvgCleanEnergy * 10.0) / 10.0
        );
    }

    private DailyMixDto calculateDailyAverage(String dateStr, List<GenerationData> dayData) {
        Map<FuelType, Double> averageMix = new EnumMap<>(FuelType.class);

        for (FuelType type : FuelType.values()) {
            double avgPerc = dayData.stream()
                    .mapToDouble(interval -> interval.generationMix().stream()
                            .filter(mix -> mix.fuel() == type)
                            .findFirst()
                            .map(GenerationMix::perc)
                            .orElse(0.0))
                    .average()
                    .orElse(0.0);
            averageMix.put(type, Math.round(avgPerc * 10.0) / 10.0);
        }

        double cleanEnergyAvg = dayData.stream()
                .mapToDouble(this::calculateCleanEnergyPercentage)
                .average()
                .orElse(0.0);

        return new DailyMixDto(
                java.time.LocalDate.parse(dateStr),
                Math.round(cleanEnergyAvg * 10.0) / 10.0,
                averageMix
        );
    }

    public double calculateCleanEnergyPercentage(GenerationData generationData) {
        return generationData.generationMix().stream()
                .filter(mix -> mix.fuel() != null && mix.fuel().isClean())
                .mapToDouble(GenerationMix::perc)
                .sum();
    }
}