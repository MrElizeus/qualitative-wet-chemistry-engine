package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitBranchPolicy;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitPolicy;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class WorkflowSplitPolicyValidator {

    private WorkflowSplitPolicyValidator() {
    }

    public static void validate(
        Collection<WorkflowNodeSpec> nodeSpecs,
        Collection<WorkflowTransitionSpec> transitionSpecs,
        Collection<PhysicalSplitPolicy> splitPolicies
    ) {
        Objects.requireNonNull(nodeSpecs, "workflow node specs cannot be null");
        Objects.requireNonNull(transitionSpecs, "workflow transition specs cannot be null");
        Objects.requireNonNull(splitPolicies, "physical split policies cannot be null");

        Set<String> nodeIds = new HashSet<>();
        for (WorkflowNodeSpec nodeSpec : nodeSpecs) {
            Objects.requireNonNull(nodeSpec, "workflow node spec cannot be null");
            nodeIds.add(nodeSpec.id());
        }

        Map<String, Set<ReactionAction>> actionsByNode = new HashMap<>();
        for (WorkflowTransitionSpec transitionSpec : transitionSpecs) {
            Objects.requireNonNull(transitionSpec, "workflow transition spec cannot be null");
            actionsByNode
                .computeIfAbsent(transitionSpec.fromNodeId(), ignored -> new HashSet<>())
                .add(transitionSpec.action());
        }

        Set<String> splitNodeIds = new HashSet<>();
        for (PhysicalSplitPolicy splitPolicy : splitPolicies) {
            Objects.requireNonNull(splitPolicy, "physical split policy cannot be null");

            if (!splitNodeIds.add(splitPolicy.nodeId())) {
                throw new IllegalArgumentException("duplicate split policy for node " + splitPolicy.nodeId() + ".");
            }

            if (!nodeIds.contains(splitPolicy.nodeId())) {
                throw new IllegalArgumentException("split policy references unknown node " + splitPolicy.nodeId() + ".");
            }

            Set<ReactionAction> actionsForNode = actionsByNode.getOrDefault(splitPolicy.nodeId(), Set.of());
            Set<ReactionAction> policyActions = new HashSet<>();
            for (PhysicalSplitBranchPolicy branch : splitPolicy.branches()) {
                Objects.requireNonNull(branch, "physical split branch policy cannot be null");

                if (!policyActions.add(branch.action())) {
                    throw new IllegalArgumentException(
                        "duplicate split branch action "
                            + branch.action()
                            + " in node "
                            + splitPolicy.nodeId()
                            + "."
                    );
                }

                if (!actionsForNode.contains(branch.action())) {
                    throw new IllegalArgumentException(
                        "split branch action "
                            + branch.action()
                            + " is not defined as transition in node "
                            + splitPolicy.nodeId()
                            + "."
                    );
                }
            }
        }
    }
}
