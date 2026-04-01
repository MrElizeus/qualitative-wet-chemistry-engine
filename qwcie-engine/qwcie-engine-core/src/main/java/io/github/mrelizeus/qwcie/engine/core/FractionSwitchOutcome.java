package io.github.mrelizeus.qwcie.engine.core;

import java.util.Objects;

public record FractionSwitchOutcome(
    boolean switched,
    String activeFractionId,
    String currentNodeId,
    String message
) {

    public FractionSwitchOutcome {
        activeFractionId = requireText(activeFractionId, "active fraction id");
        currentNodeId = requireText(currentNodeId, "current node id");
        message = message == null ? "" : message;
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
