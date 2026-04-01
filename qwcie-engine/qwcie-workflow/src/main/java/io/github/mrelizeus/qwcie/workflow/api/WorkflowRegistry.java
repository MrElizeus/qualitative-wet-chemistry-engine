package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WorkflowRegistry {

    private final Map<String, WorkflowDefinition> workflows;

    public WorkflowRegistry() {
        this(List.of(new ChlorideMainlineWorkflowDefinition()));
    }

    public WorkflowRegistry(List<WorkflowDefinition> workflowDefinitions) {
        Objects.requireNonNull(workflowDefinitions, "workflow definitions cannot be null");
        if (workflowDefinitions.isEmpty()) {
            throw new IllegalArgumentException("workflow definitions cannot be empty");
        }

        Map<String, WorkflowDefinition> byId = new LinkedHashMap<>();
        for (WorkflowDefinition definition : workflowDefinitions) {
            Objects.requireNonNull(definition, "workflow definition cannot be null");
            if (byId.containsKey(definition.id())) {
                throw new IllegalArgumentException("duplicated workflow id: " + definition.id());
            }
            byId.put(definition.id(), definition);
        }

        this.workflows = Map.copyOf(byId);
    }

    public WorkflowDefinition get(String workflowId) {
        Objects.requireNonNull(workflowId, "workflow id cannot be null");

        WorkflowDefinition definition = workflows.get(workflowId);
        if (definition == null) {
            throw new IllegalArgumentException("workflow not found: " + workflowId);
        }

        return definition;
    }

    public Set<String> availableWorkflowIds() {
        return workflows.keySet();
    }
}
