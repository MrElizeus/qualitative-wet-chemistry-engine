package io.github.mrelizeus.qwcie.workflow;

import io.github.mrelizeus.qwcie.engine.Node;

public interface WorkflowDefinition {

    String id();

    Node startNode();
}
