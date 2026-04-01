package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies;

import java.util.Objects;
import java.util.Set;

public record FractionSnapshot(
    String fractionId,
    String currentNodeId,
    String currentNodeLabel,
    Set<ChemicalSpecies> expectedSpecies,
    FractionPhase phase,
    FractionStatus status,
    FractionOrigin origin
) {

    public FractionSnapshot {
        fractionId = requireText(fractionId, "fraction id");
        currentNodeId = requireText(currentNodeId, "current node id");
        currentNodeLabel = requireText(currentNodeLabel, "current node label");
        expectedSpecies = Set.copyOf(Objects.requireNonNull(expectedSpecies, "expected species cannot be null"));
        phase = Objects.requireNonNull(phase, "fraction phase cannot be null");
        status = Objects.requireNonNull(status, "fraction status cannot be null");
        origin = Objects.requireNonNull(origin, "fraction origin cannot be null");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return normalized;
    }
}
