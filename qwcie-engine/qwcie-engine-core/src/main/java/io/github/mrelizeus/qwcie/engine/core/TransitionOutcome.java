package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;

import java.util.List;
import java.util.Objects;

public record TransitionOutcome(
    boolean transitioned,
    String currentNodeId,
    List<ObservedSignal> signals,
    String message
) {

    public TransitionOutcome {
        currentNodeId = Objects.requireNonNull(currentNodeId, "current node id cannot be null");
        signals = List.copyOf(Objects.requireNonNull(signals, "signals cannot be null"));
        message = message == null ? "" : message;
    }
}
