package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;

import java.util.Objects;

public record PhysicalSplitBranchPolicy(
    ReactionAction action,
    String label,
    FractionPhase phase,
    boolean relevant
) {

    public PhysicalSplitBranchPolicy(ReactionAction action, String label, FractionPhase phase) {
        this(action, label, phase, true);
    }

    public PhysicalSplitBranchPolicy {
        action = Objects.requireNonNull(action, "reaction action cannot be null");
        label = requireText(label, "branch label");
        phase = Objects.requireNonNull(phase, "fraction phase cannot be null");
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
