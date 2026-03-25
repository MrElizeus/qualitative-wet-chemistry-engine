package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public record WorkflowTransitionSpec(
    String fromNodeId,
    ReactionAction action,
    String toNodeId,
    List<ObservedSignal> signals,
    String message
) {

    public WorkflowTransitionSpec(String fromNodeId, ReactionAction action, String toNodeId) {
        this(fromNodeId, action, toNodeId, List.of(), "");
    }

    public WorkflowTransitionSpec(
        String fromNodeId,
        ReactionAction action,
        String toNodeId,
        Collection<ObservedSignal> signals,
        String message
    ) {
        this(
            fromNodeId,
            action,
            toNodeId,
            List.copyOf(Objects.requireNonNull(signals, "signals cannot be null")),
            message
        );
    }

    public WorkflowTransitionSpec {
        fromNodeId = validateText(fromNodeId, "from node id");
        action = Objects.requireNonNull(action, "reaction action cannot be null");
        toNodeId = validateText(toNodeId, "to node id");
        signals = List.copyOf(Objects.requireNonNull(signals, "signals cannot be null"));
        message = message == null ? "" : message;
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
