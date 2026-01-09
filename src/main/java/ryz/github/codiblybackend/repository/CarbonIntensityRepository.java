package ryz.github.codiblybackend.repository;

import org.springframework.stereotype.Repository;
import ryz.github.codiblybackend.client.CarbonIntensityClient;
import ryz.github.codiblybackend.model.GenerationData;

import java.time.Instant;
import java.util.List;

@Repository
public class CarbonIntensityRepository implements EnergyRepository {

    private final CarbonIntensityClient client;

    public CarbonIntensityRepository(CarbonIntensityClient client) {
        this.client = client;
    }

    @Override
    public List<GenerationData> getGeneration(Instant from, Instant to) {
        return client.getGeneration(from, to).generationData();
    }
}
