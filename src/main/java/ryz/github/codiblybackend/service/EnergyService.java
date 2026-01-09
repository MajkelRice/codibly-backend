package ryz.github.codiblybackend.service;

import org.springframework.stereotype.Service;
import ryz.github.codiblybackend.client.CarbonIntensityClient;
import ryz.github.codiblybackend.model.GenerationData;
import ryz.github.codiblybackend.model.GenerationMix;
import ryz.github.codiblybackend.repository.CarbonIntensityRepository;

@Service
public class EnergyService {
    private final CarbonIntensityRepository carbonIntensityRepository;

    public EnergyService(CarbonIntensityRepository carbonIntensityRepository) {
        this.carbonIntensityRepository = carbonIntensityRepository;
    }

    public double calculateCleanEnergyPercentage(GenerationData generationData) {
        return generationData.generationMix().stream()
                .filter(mix -> mix.fuel() != null && mix.fuel().isClean())
                .mapToDouble(GenerationMix::perc)
                .sum();
    }

    //TODO: implement
    public Object getDailyMix() {
        return new Object() {};
    }

    //TODO: implement
    public Object findBestChargingWindow() {
        return new Object() {};
    }
}
