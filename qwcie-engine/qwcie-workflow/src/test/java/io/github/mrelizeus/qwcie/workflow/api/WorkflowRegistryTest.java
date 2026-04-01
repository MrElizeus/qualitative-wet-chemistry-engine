package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkflowRegistryTest {

    @Test
    void defaultRegistryContainsChlorideMainlineWorkflow() {
        WorkflowRegistry registry = new WorkflowRegistry();

        assertTrue(registry.availableWorkflowIds().contains(ChlorideMainlineWorkflowDefinition.WORKFLOW_ID));
        assertEquals(ChlorideMainlineWorkflowDefinition.WORKFLOW_ID, registry.get(ChlorideMainlineWorkflowDefinition.WORKFLOW_ID).id());
    }

    @Test
    void rejectsDuplicatedWorkflowIds() {
        ChlorideMainlineWorkflowDefinition first = new ChlorideMainlineWorkflowDefinition();
        ChlorideMainlineWorkflowDefinition second = new ChlorideMainlineWorkflowDefinition();

        assertThrows(
            IllegalArgumentException.class,
            () -> new WorkflowRegistry(List.of(first, second))
        );
    }
}
