package io.github.mrelizeus.qwcie.workflow;

import io.github.mrelizeus.qwcie.domain.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.ReactionAction;
import io.github.mrelizeus.qwcie.engine.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.TransitionOutcome;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupIWorkflowDefinitionTest {

    @Test
    void precipitatesGroupIChloridesAfterDiluteHclOrNacl() {
        AnalysisEngine engine = createGroupIEngine();

        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.GROUP1_CHLORIDE_PRECIPITATE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.GROUP1_CHLORIDES_PRECIPITATED));
    }

    @Test
    void createsSplitAfterConcentratedHcl() {
        AnalysisEngine engine = createGroupIEngine();

        engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_CONCENTRATED_HCL);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.POST_CONC_HCL_SPLIT, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.GROUP1_RESIDUE_READY));
        assertTrue(outcome.signals().contains(ObservedSignal.GROUP1_DISSOLVED_COMPLEXES_READY));
    }

    @Test
    void exposesPostChlorideFiltrateCationsAfterFiltering() {
        AnalysisEngine engine = createGroupIEngine();

        engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.FILTER_POST_CHLORIDE_FILTRATE);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.POST_CHLORIDE_FILTRATE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.POST_CHLORIDE_FILTRATE_CATIONS_REMAIN_IN_SOLUTION));
    }

    @Test
    void appliesNaohH2o2HeatTreatmentAndOxidizesTinInFiltrate() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.POST_ALKALINE_OXIDATIVE_SPLIT_POINT, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_OXIDATIVE_TREATMENT_APPLIED));
        assertTrue(outcome.signals().contains(ObservedSignal.SN2_OXIDIZED_TO_SN4));
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_SOLID_LIQUID_SPLIT_READY));
    }

    @Test
    void separatesAlkalinePrecipitateBranchForDownstreamCationWork() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.ALKALINE_PRECIPITATE_PHASE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_PRECIPITATE_ISOLATED));
    }

    @Test
    void dissolvesAlkalinePrecipitateWithHclAndHeat() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.ACID_DISSOLVED_PRECIPITATE_PHASE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_PRECIPITATE_DISSOLVED_BY_HCL_HEAT));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains("Fe3+"));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains("Mn4+"));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains("Cu2+"));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains("Cd2+"));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains("Ni2+"));
    }

    @Test
    void separatesAlkalineSolutionBranchAsSolutionFraction() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.ALKALINE_SOLUTION_PHASE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_SOLUTION_ISOLATED));
    }

    @Test
    void confirmsSilverOnResidueBranch() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_RESIDUE_BRANCH,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.ADD_HNO3
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.AG_CONFIRMED_WHITE_AGCL, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.AG_WHITE_AGCL_CONFIRMED));
    }

    @Test
    void confirmsLeadOnDissolvedBranch() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.PB_CONFIRMED_YELLOW_PBCRO4, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.PB_YELLOW_PBCRO4_CONFIRMED));
    }

    @Test
    void confirmsAntimonyOnDissolvedBranch() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER,
            ReactionAction.ADD_HCL_KNO2_RHODAMINE_B
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.SB_CONFIRMED_LILAC_COMPLEX, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.SB_LILAC_COMPLEX_CONFIRMED));
    }

    @Test
    void requiresLeadStepBeforeAntimonyStepOnDissolvedBranch() {
        AnalysisEngine engine = createGroupIEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_DISSOLVED_BRANCH
        );

        applySequence(engine, sequence);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_HCL_KNO2_RHODAMINE_B);

        assertFalse(outcome.transitioned());
        assertEquals(GroupIWorkflowDefinition.GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX, outcome.currentNodeId());
    }

    private static AnalysisEngine createGroupIEngine() {
        GroupIWorkflowDefinition workflow = new GroupIWorkflowDefinition();
        return new AnalysisEngine(workflow.startNode());
    }

    private static TransitionOutcome applySequence(AnalysisEngine engine, List<ReactionAction> actions) {
        TransitionOutcome latest = null;
        for (ReactionAction action : actions) {
            latest = engine.applyAction(action);
        }
        return latest;
    }
}
