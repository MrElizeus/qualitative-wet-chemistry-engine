package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.engine.core.Node;

public interface WorkflowDefinition {

    String id();

    Node startNode();
}
