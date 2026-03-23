package io.github.mrelizeus.qwcie.domain;

import java.util.Objects;

public record CationMetadata(
    Ion ion,
    AnalyticalGroup theoreticalGroup,
    String theoreticalSeparationCriterion,
    String notes
) {

    public CationMetadata {
        ion = Objects.requireNonNull(ion, "ion cannot be null");
        theoreticalGroup = Objects.requireNonNull(theoreticalGroup, "theoretical group cannot be null");
        theoreticalSeparationCriterion = requireText(theoreticalSeparationCriterion, "theoretical separation criterion");
        notes = requireText(notes, "notes");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return trimmed;
    }
}
