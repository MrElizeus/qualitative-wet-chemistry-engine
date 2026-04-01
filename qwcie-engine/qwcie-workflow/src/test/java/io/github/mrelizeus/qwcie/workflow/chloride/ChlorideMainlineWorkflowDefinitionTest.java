package io.github.mrelizeus.qwcie.workflow.chloride;

import io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies;
import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.core.TransitionOutcome;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChlorideMainlineWorkflowDefinitionTest {

    @Test
    void precipitatesChloridesAfterDiluteHclOrNacl() {
        AnalysisEngine engine = createMainlineEngine();

        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.CHLORIDE_PRECIPITATE_NODE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.CHLORIDE_PRECIPITATE_FORMED));
    }

    @Test
    void createsSplitAfterConcentratedHcl() {
        AnalysisEngine engine = createMainlineEngine();

        engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);
        engine.applyAction(ReactionAction.FILTER_POST_CHLORIDE_FILTRATE);
        engine.applyAction(ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_CONCENTRATED_HCL);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_CONC_HCL_SPLIT, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.CHLORIDE_RESIDUE_READY));
        assertTrue(outcome.signals().contains(ObservedSignal.CHLORIDE_DISSOLVED_COMPLEXES_READY));
    }

    @Test
    void exposesPostChlorideFiltrateCationsAfterFiltering() {
        AnalysisEngine engine = createMainlineEngine();

        engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.FILTER_POST_CHLORIDE_FILTRATE);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_CHLORIDE_SPLIT, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.POST_CHLORIDE_SPLIT_READY));
    }

    @Test
    void selectsFiltrateBranchAfterPostChlorideSplit() {
        AnalysisEngine engine = createMainlineEngine();

        engine.applyAction(ReactionAction.ADD_DILUTE_HCL_OR_NACL);
        engine.applyAction(ReactionAction.FILTER_POST_CHLORIDE_FILTRATE);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_CHLORIDE_FILTRATE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.POST_CHLORIDE_FILTRATE_CATIONS_REMAIN_IN_SOLUTION));
    }

    @Test
    void appliesNaohH2o2HeatTreatmentAndOxidizesTinInFiltrate() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_ALKALINE_OXIDATIVE_SPLIT_POINT, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_OXIDATIVE_TREATMENT_APPLIED));
        assertTrue(outcome.signals().contains(ObservedSignal.SN2_OXIDIZED_TO_SN4));
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_SOLID_LIQUID_SPLIT_READY));
    }

    @Test
    void separatesAlkalinePrecipitateBranchForDownstreamCationWork() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.ALKALINE_PRECIPITATE_PHASE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_PRECIPITATE_ISOLATED));
    }

    @Test
    void dissolvesAlkalinePrecipitateWithHclAndHeat() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.ACID_DISSOLVED_PRECIPITATE_PHASE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_PRECIPITATE_DISSOLVED_BY_HCL_HEAT));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains(ChemicalSpecies.FE_3_PLUS));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains(ChemicalSpecies.MN_4_PLUS));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains(ChemicalSpecies.CU_2_PLUS));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains(ChemicalSpecies.CD_2_PLUS));
        assertTrue(engine.getCurrentNode().getExpectedSpecies().contains(ChemicalSpecies.NI_2_PLUS));
    }

    @Test
    void separatesAlkalineSolutionBranchAsSolutionFraction() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.ALKALINE_SOLUTION_PHASE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ALKALINE_SOLUTION_ISOLATED));
    }

    @Test
    void preparesAmphotericSplitAfterAlkalineSolutionBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH,
            ReactionAction.ADD_HCL_NH4OH_NH4CL
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_AMPHOTERIC_SPLIT_POINT, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.AMPHOTERIC_SPLIT_READY));
    }

    @Test
    void confirmsTinOnAmphotericBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH,
            ReactionAction.ADD_HCL_NH4OH_NH4CL,
            ReactionAction.SELECT_SN_AL_HYDROXIDE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_SN_CONFIRMATION_PATH,
            ReactionAction.ADD_FE0_WITH_HEAT,
            ReactionAction.ADD_HGCL2_EXCESS
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.SN_CONFIRMED_HG2CL2_WHITE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.SN_CONFIRMED_WHITE_HG2CL2));
    }

    @Test
    void confirmsAluminumOnAmphotericBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH,
            ReactionAction.ADD_HCL_NH4OH_NH4CL,
            ReactionAction.SELECT_SN_AL_HYDROXIDE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_AL_CONFIRMATION_PATH,
            ReactionAction.ADD_NH4CH3COO_ALUMINON_NH4OH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.AL_CONFIRMED_RED_AL_OH3, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.AL_CONFIRMED_RED_AL_OH3));
    }

    @Test
    void confirmsZincOnAmphotericBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH,
            ReactionAction.ADD_HCL_NH4OH_NH4CL,
            ReactionAction.SELECT_ZN_AMMINE_BRANCH,
            ReactionAction.ADD_CH3COOH,
            ReactionAction.ADD_K4FE_CN6
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.ZN_CONFIRMED_WHITE_ZN2_FE_CN6, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.ZN_CONFIRMED_WHITE_ZN2_FE_CN6));
    }

    @Test
    void splitsHydroxideBranchIntoAmmoniacalFractions() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ReactionAction.ADD_EXCESS_NH4OH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_AMMONIACAL_SPLIT_POINT, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.AMMONIACAL_SPLIT_READY));
    }

    @Test
    void confirmsIronAsBloodRedThiocyanateComplex() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_FE_MN_RESIDUE_BRANCH,
            ReactionAction.ADD_HNO3_WITH_HEAT,
            ReactionAction.SELECT_FE_CONFIRMATION_BRANCH,
            ReactionAction.ADD_KSCN
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.FE_THIOCYANATE_BLOOD_RED_CONFIRMED, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.FE_THIOCYANATE_BLOOD_RED_CONFIRMED));
    }

    @Test
    void confirmsIronAsPrussianBlue() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_FE_MN_RESIDUE_BRANCH,
            ReactionAction.ADD_HNO3_WITH_HEAT,
            ReactionAction.SELECT_FE_CONFIRMATION_BRANCH,
            ReactionAction.ADD_K4FE_CN6
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.FE_PRUSSIAN_BLUE_CONFIRMED, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.FE_PRUSSIAN_BLUE_CONFIRMED));
    }

    @Test
    void confirmsManganeseAsVioletPermanganate() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_FE_MN_RESIDUE_BRANCH,
            ReactionAction.ADD_HNO3_WITH_HEAT,
            ReactionAction.SELECT_MN_CONFIRMATION_BRANCH,
            ReactionAction.ADD_NABIO3_WITH_HEAT
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.MN_PERMANGANATE_CONFIRMED, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.MN_PERMANGANATE_VIOLET_CONFIRMED));
    }

    @Test
    void confirmsNickelAsScarletDmgPrecipitate() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_NI_CU_CD_AMMINE_BRANCH,
            ReactionAction.ADD_CH3COOH,
            ReactionAction.SELECT_NI_CONFIRMATION_BRANCH,
            ReactionAction.ADD_DMG_WITH_NH4OH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.NI_DMG_SCARLET_CONFIRMED, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.NI_DMG_SCARLET_CONFIRMED));
    }

    @Test
    void confirmsCopperWhenFerrocyanidePrecipitatePersistsAfterHclExcess() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_NI_CU_CD_AMMINE_BRANCH,
            ReactionAction.ADD_CH3COOH,
            ReactionAction.SELECT_CU_CD_SEPARATION_BRANCH,
            ReactionAction.ADD_K4FE_CN6,
            ReactionAction.ADD_HCL_EXCESS,
            ReactionAction.SELECT_PRECIPITATE_PERSISTS_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.CU_CONFIRMED_SOLID_FERROCYANIDE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.CU_FERROCYANIDE_SOLID_CONFIRMED_AFTER_HCL_EXCESS));
    }

    @Test
    void confirmsCadmiumWhenPrecipitateDissolvesAndThenTurnsWhiteWithH2so4() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_NI_CU_CD_AMMINE_BRANCH,
            ReactionAction.ADD_CH3COOH,
            ReactionAction.SELECT_CU_CD_SEPARATION_BRANCH,
            ReactionAction.ADD_K4FE_CN6,
            ReactionAction.ADD_HCL_EXCESS,
            ReactionAction.SELECT_PRECIPITATE_DISSOLVES_BRANCH,
            ReactionAction.ADD_H2SO4
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.CD_WHITE_FERROCYANIDE_CONFIRMED, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.CD_WHITE_FERROCYANIDE_CONFIRMED));
    }

    @Test
    void confirmsSilverOnResidueBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_RESIDUE_BRANCH,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH,
            ReactionAction.ADD_HNO3,
            ReactionAction.SELECT_AGCL_PRECIPITATE_PRESENT_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.AG_CONFIRMED_WHITE_AGCL, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.AG_WHITE_AGCL_CONFIRMED));
    }

    @Test
    void confirmsMercuryWhenBlackResidueAppearsAfterNh4oh() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_RESIDUE_BRANCH,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_HG_BLACK_RESIDUE_PRESENT_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.HG_CONFIRMED_BLACK_RESIDUE, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.HG_BLACK_RESIDUE));
    }

    @Test
    void requiresAgclObservationStepAfterHno3ToConfirmSilver() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_RESIDUE_BRANCH,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH,
            ReactionAction.ADD_HNO3
        );

        TransitionOutcome afterHno3 = applySequence(engine, sequence);
        assertTrue(afterHno3.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_HNO3_SILVER_SPLIT, afterHno3.currentNodeId());

        TransitionOutcome outcome = engine.applyAction(ReactionAction.SELECT_AGCL_PRECIPITATE_ABSENT_BRANCH);
        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.AG_NOT_CONFIRMED_AFTER_HNO3, outcome.currentNodeId());
        assertFalse(outcome.signals().contains(ObservedSignal.AG_WHITE_AGCL_CONFIRMED));
    }

    @Test
    void confirmsLeadOnDissolvedBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER,
            ReactionAction.SELECT_PBCRO4_PRECIPITATE_PRESENT_BRANCH
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.PB_CONFIRMED_YELLOW_PBCRO4, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.PB_YELLOW_PBCRO4_CONFIRMED));
    }

    @Test
    void confirmsAntimonyOnDissolvedBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER,
            ReactionAction.SELECT_PBCRO4_PRECIPITATE_ABSENT_BRANCH,
            ReactionAction.ADD_HCL_KNO2_RHODAMINE_B
        );

        TransitionOutcome outcome = applySequence(engine, sequence);

        assertTrue(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.SB_CONFIRMED_LILAC_COMPLEX, outcome.currentNodeId());
        assertTrue(outcome.signals().contains(ObservedSignal.SB_LILAC_COMPLEX_CONFIRMED));
    }

    @Test
    void requiresPbObservationBranchSelectionBeforeAntimonyStepOnDissolvedBranch() {
        AnalysisEngine engine = createMainlineEngine();

        List<ReactionAction> sequence = List.of(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER
        );

        applySequence(engine, sequence);
        TransitionOutcome outcome = engine.applyAction(ReactionAction.ADD_HCL_KNO2_RHODAMINE_B);

        assertFalse(outcome.transitioned());
        assertEquals(ChlorideMainlineWorkflowDefinition.POST_K2CRO4_PB_SPLIT, outcome.currentNodeId());
    }

    private static AnalysisEngine createMainlineEngine() {
        ChlorideMainlineWorkflowDefinition workflow = new ChlorideMainlineWorkflowDefinition();
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
