package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;

import java.util.List;
import java.util.Objects;

public record BranchingTransitionOutcome(
    boolean transitioned,
    String activeFractionId,
    String currentNodeId,
    List<ObservedSignal> signals,
    String message,
    List<FractionSnapshot> createdFractions,
    List<String> closedFractionIds,
    List<String> activatedFractionIds
) {

    public BranchingTransitionOutcome {
        activeFractionId = requireText(activeFractionId, "active fraction id");
        currentNodeId = requireText(currentNodeId, "current node id");
        signals = List.copyOf(Objects.requireNonNull(signals, "signals cannot be null"));
        message = message == null ? "" : message;
        createdFractions = List.copyOf(Objects.requireNonNull(createdFractions, "created fractions cannot be null"));
        closedFractionIds = List.copyOf(Objects.requireNonNull(closedFractionIds, "closed fraction ids cannot be null"));
        activatedFractionIds = List.copyOf(
            Objects.requireNonNull(activatedFractionIds, "activated fraction ids cannot be null")
        );
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
