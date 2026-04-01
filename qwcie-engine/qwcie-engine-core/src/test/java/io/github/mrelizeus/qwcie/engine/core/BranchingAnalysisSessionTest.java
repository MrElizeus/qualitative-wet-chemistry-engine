package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BranchingAnalysisSessionTest {

    @Test
    void materializesFractionsAutomaticallyOnPhysicalSplitNode() {
        NodeGraph graph = buildSplitGraph();
        BranchingAnalysisSession session = new BranchingAnalysisSession(
            graph.start(),
            List.of(
                new PhysicalSplitPolicy(
                    "SPLIT",
                    List.of(
                        new PhysicalSplitBranchPolicy(
                            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
                            "Solution branch",
                            FractionPhase.SOLUTION
                        ),
                        new PhysicalSplitBranchPolicy(
                            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
                            "Precipitate branch",
                            FractionPhase.PRECIPITATE
                        )
                    )
                )
            )
        );

        BranchingTransitionOutcome beforeSelection = session.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);

        assertTrue(beforeSelection.transitioned());
        assertEquals("F1", beforeSelection.activeFractionId());
        assertEquals("SPLIT", beforeSelection.currentNodeId());
        assertTrue(beforeSelection.createdFractions().isEmpty());

        BranchingTransitionOutcome outcome = session.applyAction(ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH);

        assertTrue(outcome.transitioned());
        assertEquals("F2", outcome.activeFractionId());
        assertEquals("SOLUTION_PATH", outcome.currentNodeId());
        assertEquals(2, outcome.createdFractions().size());
        assertEquals(List.of("F1"), outcome.closedFractionIds());
        assertEquals(List.of("F2"), outcome.activatedFractionIds());

        List<FractionSnapshot> fractions = session.listFractions();
        assertEquals(3, fractions.size());
        assertEquals(FractionStatus.CLOSED, fractions.get(0).status());
        assertEquals(FractionStatus.ACTIVE, fractions.get(1).status());
        assertEquals(FractionStatus.PENDING, fractions.get(2).status());
    }

    @Test
    void keepsFractionsIndependentAcrossSwitches() {
        NodeGraph graph = buildSplitGraph();
        BranchingAnalysisSession session = new BranchingAnalysisSession(
            graph.start(),
            List.of(
                new PhysicalSplitPolicy(
                    "SPLIT",
                    List.of(
                        new PhysicalSplitBranchPolicy(
                            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
                            "Solution branch",
                            FractionPhase.SOLUTION
                        ),
                        new PhysicalSplitBranchPolicy(
                            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
                            "Precipitate branch",
                            FractionPhase.PRECIPITATE
                        )
                    )
                )
            )
        );

        session.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);
        session.applyAction(ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH);
        session.applyAction(ReactionAction.ADD_HNO3);

        FractionSwitchOutcome switchOutcome = session.switchActiveFraction("F3");
        assertTrue(switchOutcome.switched());
        assertEquals("PRECIP_PATH", switchOutcome.currentNodeId());

        session.applyAction(ReactionAction.ADD_CONCENTRATED_HCL);

        FractionSnapshot solutionFraction = session.listFractions().stream()
            .filter(snapshot -> snapshot.fractionId().equals("F2"))
            .findFirst()
            .orElseThrow();
        FractionSnapshot precipitateFraction = session.listFractions().stream()
            .filter(snapshot -> snapshot.fractionId().equals("F3"))
            .findFirst()
            .orElseThrow();

        assertEquals("SOLUTION_DONE", solutionFraction.currentNodeId());
        assertEquals("PRECIP_DONE", precipitateFraction.currentNodeId());
    }

    @Test
    void reportsInvalidTargetWhenSwitchingToUnknownFraction() {
        NodeGraph graph = buildSplitGraph();
        BranchingAnalysisSession session = new BranchingAnalysisSession(graph.start());

        FractionSwitchOutcome switchOutcome = session.switchActiveFraction("F99");

        assertFalse(switchOutcome.switched());
        assertEquals("F1", switchOutcome.activeFractionId());
        assertEquals("START", switchOutcome.currentNodeId());
    }

    @Test
    void doesNotAutoSplitInNodesWithoutPhysicalSplitPolicy() {
        NodeGraph graph = buildSplitGraph();
        BranchingAnalysisSession session = new BranchingAnalysisSession(graph.start(), List.of());

        BranchingTransitionOutcome outcome = session.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);

        assertTrue(outcome.transitioned());
        assertTrue(outcome.createdFractions().isEmpty());
        assertTrue(outcome.closedFractionIds().isEmpty());
        assertEquals("SPLIT", outcome.currentNodeId());
        assertEquals(1, session.listFractions().size());
    }

    private static NodeGraph buildSplitGraph() {
        Node start = new Node("START", "Start");
        Node split = new Node("SPLIT", "Physical split");
        Node solutionPath = new Node("SOLUTION_PATH", "Solution path");
        Node solutionDone = new Node("SOLUTION_DONE", "Solution done");
        Node precipitatePath = new Node("PRECIP_PATH", "Precipitate path");
        Node precipitateDone = new Node("PRECIP_DONE", "Precipitate done");

        start.addTransition(ReactionAction.ADD_DILUTE_HCL_OR_NACL, split);
        split.addTransition(ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH, solutionPath);
        split.addTransition(ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH, precipitatePath);
        solutionPath.addTransition(ReactionAction.ADD_HNO3, solutionDone);
        precipitatePath.addTransition(ReactionAction.ADD_CONCENTRATED_HCL, precipitateDone);

        return new NodeGraph(start);
    }

    private record NodeGraph(Node start) {
    }
}
