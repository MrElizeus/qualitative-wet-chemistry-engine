package io.github.mrelizeus.qwcie.workflow.api;

import io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.core.Node;
import io.github.mrelizeus.qwcie.engine.core.TransitionOutcome;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkflowGraphFactoryTest {

    @Test
    void buildsStartNodeFromDeclarativeSpecs() {
        List<WorkflowNodeSpec> nodes = List.of(
            new WorkflowNodeSpec("START", "Start", List.of(ChemicalSpecies.AG_PLUS)),
            new WorkflowNodeSpec("END", "End", List.of(ChemicalSpecies.AGCL_SOLID))
        );
        List<WorkflowTransitionSpec> transitions = List.of(
            new WorkflowTransitionSpec(
                "START",
                ReactionAction.ADD_DILUTE_HCL_OR_NACL,
                "END",
                List.of(),
                "Move to end node."
            )
        );

        Node startNode = WorkflowGraphFactory.buildStartNode("START", nodes, transitions);
        AnalysisEngine engine = new AnalysisEngine(startNode);

        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);

        assertTrue(outcome.transitioned());
        assertEquals("END", outcome.currentNodeId());
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains(ChemicalSpecies.AGCL_SOLID));
    }

    @Test
    void rejectsMissingStartNodeInSpecs() {
        List<WorkflowNodeSpec> nodes = List.of(
            new WorkflowNodeSpec("A", "Node A")
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> WorkflowGraphFactory.buildStartNode("MISSING", nodes, List.of())
        );
    }

    @Test
    void rejectsTransitionThatReferencesUnknownNode() {
        List<WorkflowNodeSpec> nodes = List.of(
            new WorkflowNodeSpec("A", "Node A")
        );
        List<WorkflowTransitionSpec> transitions = List.of(
            new WorkflowTransitionSpec("A", ReactionAction.ADD_HNO3, "B")
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> WorkflowGraphFactory.buildStartNode("A", nodes, transitions)
        );
    }

    @Test
    void rejectsDuplicatedNodeIds() {
        List<WorkflowNodeSpec> nodes = List.of(
            new WorkflowNodeSpec("A", "Node A"),
            new WorkflowNodeSpec("A", "Node A duplicate")
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> WorkflowGraphFactory.buildStartNode("A", nodes, List.of())
        );
    }
}
