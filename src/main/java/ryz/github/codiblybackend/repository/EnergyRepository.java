package ryz.github.codiblybackend.repository;

import ryz.github.codiblybackend.model.GenerationData;

import java.time.Instant;
import java.util.List;

public interface EnergyRepository {
    List<GenerationData> getGeneration(Instant from, Instant to);
}
