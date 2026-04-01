package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.engine.core.Node;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitPolicy;

import java.util.List;

public interface WorkflowDefinition {

    String id();

    Node startNode();

    default List<PhysicalSplitPolicy> physicalSplitPolicies() {
        return List.of();
    }
}
