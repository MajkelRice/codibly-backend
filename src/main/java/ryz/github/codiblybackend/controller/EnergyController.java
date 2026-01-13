package ryz.github.codiblybackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ryz.github.codiblybackend.model.ChargingWindowDto;
import ryz.github.codiblybackend.model.DailyMixDto;
import ryz.github.codiblybackend.service.EnergyService;

import java.util.List;

@RestController
@RequestMapping("api/energy")
@Validated
public class EnergyController {
    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping("/mix")
    public ResponseEntity<List<DailyMixDto>> getEnergyMix() {
        return ResponseEntity.ok(energyService.getDailyMix());
    }

    @GetMapping("/optimal-charging")
    public ResponseEntity<ChargingWindowDto> getOptimalCharging(
            @RequestParam(defaultValue = "1") int hours
    ) {
        return ResponseEntity.ok(energyService.findBestChargingWindow(hours));
    }
}
