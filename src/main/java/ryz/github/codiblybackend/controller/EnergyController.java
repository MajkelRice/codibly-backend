package ryz.github.codiblybackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ryz.github.codiblybackend.service.EnergyService;

@RestController
@RequestMapping("api/energy")
@Validated
//TODO: change origins for prod (mb use Spring profiles)
@CrossOrigin(origins = "*")
public class EnergyController {
    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping("/mix")
    public ResponseEntity<Object> getEnergyMix() {
        return ResponseEntity.ok(energyService.getDailyMix());
    }

    @GetMapping("/optimal-charging")
    public ResponseEntity<Object> getOptimalCharging() {
        return ResponseEntity.ok(energyService.findBestChargingWindow());    }
}
