package io.github.mrelizeus.qwcie.engine;

import io.github.mrelizeus.qwcie.domain.ObservedSignal;

import java.util.List;
import java.util.Objects;

public final class NodeTransition {

    private final Node nextNode;
    private final List<ObservedSignal> signals;
    private final String message;

    public NodeTransition(Node nextNode, List<ObservedSignal> signals, String message) {
        this.nextNode = Objects.requireNonNull(nextNode, "next node cannot be null");
        this.signals = List.copyOf(Objects.requireNonNull(signals, "signals cannot be null"));
        this.message = message == null ? "" : message;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public List<ObservedSignal> getSignals() {
        return signals;
    }

    public String getMessage() {
        return message;
    }
}
