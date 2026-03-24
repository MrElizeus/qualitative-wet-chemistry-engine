package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;

import java.util.List;
import java.util.Objects;

public class AnalysisEngine {

    private Node currentNode;

    public AnalysisEngine(Node startNode) {
        this.currentNode = Objects.requireNonNull(startNode, "start node cannot be null");
    }

    public TransitionOutcome applyAction(ReactionAction action) {
        Objects.requireNonNull(action, "reaction action cannot be null");

        return currentNode
            .transitionFor(action)
            .map(transition -> {
                currentNode = transition.getNextNode();
                String message = transition.getMessage().isBlank()
                    ? "Transitioned to node " + currentNode.getId() + "."
                    : transition.getMessage();

                return new TransitionOutcome(true, currentNode.getId(), transition.getSignals(), message);
            })
            .orElseGet(() -> new TransitionOutcome(
                false,
                currentNode.getId(),
                List.of(),
                "No transition defined for action " + action + " from node " + currentNode.getId() + "."
            ));
    }

    public Node getCurrentNode() {
        return currentNode;
    }
}
