package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.engine.core.Node;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class WorkflowGraphFactory {

    private WorkflowGraphFactory() {
    }

    public static Node buildStartNode(
        String startNodeId,
        Collection<WorkflowNodeSpec> nodeSpecs,
        Collection<WorkflowTransitionSpec> transitionSpecs
    ) {
        String normalizedStartNodeId = validateText(startNodeId, "start node id");
        Objects.requireNonNull(nodeSpecs, "node specs cannot be null");
        Objects.requireNonNull(transitionSpecs, "transition specs cannot be null");

        if (nodeSpecs.isEmpty()) {
            throw new IllegalArgumentException("node specs cannot be empty");
        }

        Map<String, Node> nodesById = new LinkedHashMap<>();
        for (WorkflowNodeSpec nodeSpec : nodeSpecs) {
            Objects.requireNonNull(nodeSpec, "workflow node spec cannot be null");
            if (nodesById.containsKey(nodeSpec.id())) {
                throw new IllegalArgumentException("duplicated node id: " + nodeSpec.id());
            }
            nodesById.put(nodeSpec.id(), new Node(nodeSpec.id(), nodeSpec.label(), nodeSpec.expectedSpecies()));
        }

        if (!nodesById.containsKey(normalizedStartNodeId)) {
            throw new IllegalArgumentException("start node id not defined in node specs: " + normalizedStartNodeId);
        }

        for (WorkflowTransitionSpec transitionSpec : transitionSpecs) {
            Objects.requireNonNull(transitionSpec, "workflow transition spec cannot be null");
            Node fromNode = nodesById.get(transitionSpec.fromNodeId());
            if (fromNode == null) {
                throw new IllegalArgumentException(
                    "transition from node not defined in node specs: " + transitionSpec.fromNodeId()
                );
            }

            Node toNode = nodesById.get(transitionSpec.toNodeId());
            if (toNode == null) {
                throw new IllegalArgumentException(
                    "transition to node not defined in node specs: " + transitionSpec.toNodeId()
                );
            }

            fromNode.addTransition(
                transitionSpec.action(),
                toNode,
                transitionSpec.signals(),
                transitionSpec.message()
            );
        }

        return nodesById.get(normalizedStartNodeId);
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
