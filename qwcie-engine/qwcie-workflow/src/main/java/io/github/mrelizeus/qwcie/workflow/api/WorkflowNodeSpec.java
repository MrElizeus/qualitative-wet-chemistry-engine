package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public record WorkflowNodeSpec(
    String id,
    String label,
    Set<ChemicalSpecies> expectedSpecies
) {

    public WorkflowNodeSpec(String id, String label) {
        this(id, label, Set.of());
    }

    public WorkflowNodeSpec(String id, String label, Collection<ChemicalSpecies> expectedSpecies) {
        this(id, label, Set.copyOf(Objects.requireNonNull(expectedSpecies, "expected species cannot be null")));
    }

    public WorkflowNodeSpec {
        id = validateText(id, "node id");
        label = validateText(label, "node label");
        expectedSpecies = Set.copyOf(Objects.requireNonNull(expectedSpecies, "expected species cannot be null"));
    }

    private static String validateText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return trimmed;
    }
}
