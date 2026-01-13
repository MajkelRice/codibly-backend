package ryz.github.codiblybackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ryz.github.codiblybackend.model.*;
import ryz.github.codiblybackend.repository.CarbonIntensityRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnergyServiceTest {

    @Mock
    private CarbonIntensityRepository repository;

    @InjectMocks
    private EnergyService energyService;

    @Test
    void findBestChargingWindow_ShouldReturnWindowWithHighestCleanEnergy() {
        // GIVEN
        Instant now = Instant.now();
        List<GenerationData> mockData = Arrays.asList(
                createData(now.toString(), 10.0),
                createData(now.plusSeconds(1800).toString(), 80.0),
                createData(now.plusSeconds(3600).toString(), 80.0),
                createData(now.plusSeconds(5400).toString(), 10.0)
        );

        when(repository.getGeneration(any(), any())).thenReturn(mockData);

        // WHEN
        ChargingWindowDto result = energyService.findBestChargingWindow(1);

        // THEN
        assertNotNull(result);
        assertEquals(80.0, result.cleanEnergyPercentage());
        assertEquals(mockData.get(1).from(), result.startTime());
    }

    @Test
    void findBestChargingWindow_ShouldThrowException_WhenHoursInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            energyService.findBestChargingWindow(7);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            energyService.findBestChargingWindow(0);
        });
    }

    @Test
    void findBestChargingWindow_ShouldThrowException_WhenNotEnoughData() {
        // GIVEN
        when(repository.getGeneration(any(), any())).thenReturn(Collections.emptyList());

        // WHEN & THEN
        assertThrows(IllegalStateException.class, () -> {
            energyService.findBestChargingWindow(1);
        });
    }

    private GenerationData createData(String fromTime, double cleanPerc) {
        List<GenerationMix> mix = Arrays.asList(
                new GenerationMix(FuelType.WIND, cleanPerc),
                new GenerationMix(FuelType.GAS, 100.0 - cleanPerc)
        );
        return new GenerationData(fromTime, fromTime, mix);
    }
}