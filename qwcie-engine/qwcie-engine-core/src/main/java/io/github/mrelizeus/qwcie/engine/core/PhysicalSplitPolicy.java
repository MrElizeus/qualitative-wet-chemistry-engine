package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record PhysicalSplitPolicy(
    String nodeId,
    List<PhysicalSplitBranchPolicy> branches
) {

    public PhysicalSplitPolicy(String nodeId, Collection<PhysicalSplitBranchPolicy> branches) {
        this(nodeId, List.copyOf(Objects.requireNonNull(branches, "split branches cannot be null")));
    }

    public PhysicalSplitPolicy {
        nodeId = requireText(nodeId, "split node id");
        branches = List.copyOf(Objects.requireNonNull(branches, "split branches cannot be null"));
        if (branches.isEmpty()) {
            throw new IllegalArgumentException("split branches cannot be empty");
        }

        Set<ReactionAction> actions = new LinkedHashSet<>();
        for (PhysicalSplitBranchPolicy branch : branches) {
            Objects.requireNonNull(branch, "split branch cannot be null");
            if (!actions.add(branch.action())) {
                throw new IllegalArgumentException(
                    "duplicate split branch action " + branch.action() + " in node " + nodeId + "."
                );
            }
        }
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
