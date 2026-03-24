package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkflowRegistryTest {

    @Test
    void defaultRegistryContainsGroupIWorkflow() {
        WorkflowRegistry registry = new WorkflowRegistry();

        assertTrue(registry.availableWorkflowIds().contains(GroupIWorkflowDefinition.WORKFLOW_ID));
        assertEquals(GroupIWorkflowDefinition.WORKFLOW_ID, registry.get(GroupIWorkflowDefinition.WORKFLOW_ID).id());
    }

    @Test
    void rejectsDuplicatedWorkflowIds() {
        GroupIWorkflowDefinition first = new GroupIWorkflowDefinition();
        GroupIWorkflowDefinition second = new GroupIWorkflowDefinition();

        assertThrows(
            IllegalArgumentException.class,
            () -> new WorkflowRegistry(List.of(first, second))
        );
    }
}
