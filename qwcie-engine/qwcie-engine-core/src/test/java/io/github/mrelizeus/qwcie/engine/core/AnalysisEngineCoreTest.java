package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalysisEngineCoreTest {

    @Test
    void transitionsOnValidActionToExpectedNodeId() {
        Node start = new Node("START", "Start");
        Node next = new Node("NEXT", "Next");
        start.addTransition(ReactionAction.ADD_DILUTE_HCL_OR_NACL, next);

        AnalysisEngine engine = new AnalysisEngine(start);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);

        assertTrue(outcome.transitioned());
        assertEquals("NEXT", outcome.currentNodeId());
        assertEquals("NEXT", engine.getCurrentNode().getId());
    }

    @Test
    void doesNotTransitionWhenActionIsUndefined() {
        Node start = new Node("START", "Start");
        AnalysisEngine engine = new AnalysisEngine(start);

        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_HNO3);

        assertFalse(outcome.transitioned());
        assertEquals("START", outcome.currentNodeId());
        assertEquals("START", engine.getCurrentNode().getId());
    }

    @Test
    void duplicateActionTransitionFailsFast() {
        Node start = new Node("START", "Start");
        Node first = new Node("FIRST", "First");
        Node second = new Node("SECOND", "Second");

        start.addTransition(ReactionAction.ADD_HNO3, first);

        assertThrows(
            IllegalArgumentException.class,
            () -> start.addTransition(ReactionAction.ADD_HNO3, second)
        );
    }

    @Test
    void constructorRejectsNullStartNode() {
        assertThrows(NullPointerException.class, () -> new AnalysisEngine(null));
    }

    @Test
    void applyActionRejectsNullAction() {
        AnalysisEngine engine = new AnalysisEngine(new Node("START", "Start"));

        assertThrows(NullPointerException.class, () -> engine.applyAction(null));
    }
}
