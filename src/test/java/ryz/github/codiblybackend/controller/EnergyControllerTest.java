package ryz.github.codiblybackend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ryz.github.codiblybackend.model.ChargingWindowDto;
import ryz.github.codiblybackend.model.DailyMixDto;
import ryz.github.codiblybackend.model.FuelType;
import ryz.github.codiblybackend.service.EnergyService;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnergyController.class)
class EnergyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnergyService energyService;

    @Test
    void getEnergyMix_ShouldReturnListAndStatus200() throws Exception {
        // GIVEN
        DailyMixDto dummyMix = new DailyMixDto(
                LocalDate.now(),
                50.5,
                new EnumMap<>(FuelType.class)
        );
        when(energyService.getDailyMix()).thenReturn(List.of(dummyMix));

        // WHEN & THEN
        mockMvc.perform(get("/api/energy/mix")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cleanEnergyPercentage").value(50.5));
    }

    @Test
    void getOptimalCharging_ShouldReturnWindowAndStatus200() throws Exception {
        // GIVEN
        ChargingWindowDto dummyWindow = new ChargingWindowDto(
                "2026-01-13T12:00:00Z",
                "2026-01-13T14:00:00Z",
                88.0
        );

        when(energyService.findBestChargingWindow(anyInt())).thenReturn(dummyWindow);

        // WHEN & THEN
        mockMvc.perform(get("/api/energy/optimal-charging")
                        .param("hours", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cleanEnergyPercentage").value(88.0))
                .andExpect(jsonPath("$.startTime").value("2026-01-13T12:00:00Z"));
    }

    @Test
    void getOptimalCharging_ShouldUseDefaultParam() throws Exception {
        ChargingWindowDto dummyWindow = new ChargingWindowDto("start", "end", 10.0);
        when(energyService.findBestChargingWindow(1)).thenReturn(dummyWindow);

        mockMvc.perform(get("/api/energy/optimal-charging"))
                .andExpect(status().isOk());
    }
}