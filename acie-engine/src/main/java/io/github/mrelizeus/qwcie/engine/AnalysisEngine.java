package io.github.mrelizeus.qwcie.engine;

import io.github.mrelizeus.qwcie.domain.Observation;

public class AnalysisEngine {

    private Node currentNode;

    public AnalysisEngine(Node startNode) {
        this.currentNode = startNode;
    }

    public boolean applyObservation(Observation obs) {
        Node next = currentNode.next(obs);

        if (next == null) {
            return false;
        }

        currentNode = next;
        return true;
    }

    public Node getCurrentNode() {
        return currentNode;
    }
}
