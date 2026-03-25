package io.github.mrelizeus.qwcie.workflow.groupi;

import io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies;
import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowNodeSpec;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowTransitionSpec;

import java.util.List;

import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AGCL_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AG_DIAMMINE_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AG_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ALO2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AL_3_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CD_OH2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CD_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CU_OH2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CU_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.FE_OH3_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.FE_3_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.HG0_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.HG2CL2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.HG2_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.MNO2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.MN_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.MN_4_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.NI_OH2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.NI_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PBCL2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PBCL4_2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PB_CRO4_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PB_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SBOCL_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SB_LILAC_COMPLEX;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SB_3_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SNO3_2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SN_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ZNO2_2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ZN_2_PLUS;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.ACID_DISSOLVED_PRECIPITATE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.AG_AMMINE_PATH;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.AG_CONFIRMED_WHITE_AGCL;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.AG_NOT_CONFIRMED_AFTER_HNO3;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.ALKALINE_PRECIPITATE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.ALKALINE_SOLUTION_PHASE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.GROUP1_CHLORIDE_PRECIPITATE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.GROUP1_RESIDUE_PATH_AG_PB_HG;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.HG_CONFIRMED_BLACK_RESIDUE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.MIXTURE_I_TO_III;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.PB_CONFIRMED_YELLOW_PBCRO4;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_ALKALINE_OXIDATIVE_SPLIT_POINT;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_CHLORIDE_FILTRATE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_CHLORIDE_PRECIPITATE_RESIDUE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_CHLORIDE_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_CONC_HCL_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_HNO3_SILVER_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_K2CRO4_PB_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.POST_NH4OH_HG_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.SB_CANDIDATE_AFTER_PB_NEGATIVE;
import static io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition.SB_CONFIRMED_LILAC_COMPLEX;

final class GroupIWorkflowSpec {

    static final List<WorkflowNodeSpec> NODES = List.of(
        node(
            MIXTURE_I_TO_III,
            "Mixture of Group I to Group III cations",
            AG_PLUS,
            PB_2_PLUS,
            HG2_2_PLUS,
            SB_3_PLUS,
            SN_2_PLUS,
            CU_2_PLUS,
            CD_2_PLUS,
            FE_3_PLUS,
            MN_2_PLUS,
            NI_2_PLUS,
            AL_3_PLUS,
            ZN_2_PLUS
        ),
        node(
            GROUP1_CHLORIDE_PRECIPITATE,
            "Group I chloride precipitate",
            AGCL_SOLID,
            PBCL2_SOLID,
            HG2CL2_SOLID,
            SBOCL_SOLID
        ),
        node(
            POST_CONC_HCL_SPLIT,
            "Post concentrated HCl split",
            AGCL_SOLID,
            PBCL2_SOLID,
            HG2CL2_SOLID,
            SB_3_PLUS,
            PBCL4_2_MINUS
        ),
        node(
            POST_CHLORIDE_FILTRATE,
            "Filtrate after chloride precipitation step",
            FE_3_PLUS,
            MN_2_PLUS,
            CU_2_PLUS,
            CD_2_PLUS,
            NI_2_PLUS,
            SN_2_PLUS,
            AL_3_PLUS,
            ZN_2_PLUS
        ),
        node(POST_CHLORIDE_SPLIT, "Post chloride filtration split"),
        node(
            POST_CHLORIDE_PRECIPITATE_RESIDUE,
            "Post chloride precipitate residue",
            AGCL_SOLID,
            PBCL2_SOLID,
            HG2CL2_SOLID,
            SBOCL_SOLID
        ),
        node(
            POST_ALKALINE_OXIDATIVE_SPLIT_POINT,
            "Post NaOH excess + H2O2 + heat split point"
        ),
        node(
            ALKALINE_PRECIPITATE_PHASE,
            "Alkaline precipitate phase",
            FE_OH3_SOLID,
            MNO2_SOLID,
            CU_OH2_SOLID,
            CD_OH2_SOLID,
            NI_OH2_SOLID
        ),
        node(
            ACID_DISSOLVED_PRECIPITATE_PHASE,
            "Alkaline precipitate dissolved with HCl and heat",
            FE_3_PLUS,
            MN_4_PLUS,
            CU_2_PLUS,
            CD_2_PLUS,
            NI_2_PLUS
        ),
        node(
            ALKALINE_SOLUTION_PHASE,
            "Alkaline solution phase",
            SNO3_2_MINUS,
            ALO2_MINUS,
            ZNO2_2_MINUS
        ),
        node(
            GROUP1_RESIDUE_PATH_AG_PB_HG,
            "Residue branch: Ag/Pb/Hg",
            AGCL_SOLID,
            PBCL2_SOLID,
            HG2CL2_SOLID
        ),
        node(
            GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX,
            "Dissolved branch: Sb/Pb complexes",
            SB_3_PLUS,
            PBCL4_2_MINUS
        ),
        node(
            POST_NH4OH_HG_SPLIT,
            "Post NH4OH mercury check split",
            HG0_SOLID,
            AG_DIAMMINE_PLUS
        ),
        node(
            POST_K2CRO4_PB_SPLIT,
            "Post K2CrO4 lead check split",
            PB_CRO4_SOLID,
            SB_3_PLUS
        ),
        node(
            SB_CANDIDATE_AFTER_PB_NEGATIVE,
            "Antimony candidate path after negative PbCrO4",
            SB_3_PLUS
        ),
        node(
            HG_CONFIRMED_BLACK_RESIDUE,
            "Mercury confirmed as black metallic residue",
            HG0_SOLID
        ),
        node(
            AG_AMMINE_PATH,
            "Silver ammine path",
            AG_DIAMMINE_PLUS
        ),
        node(
            POST_HNO3_SILVER_SPLIT,
            "Post HNO3 silver check split"
        ),
        node(
            AG_CONFIRMED_WHITE_AGCL,
            "Silver confirmed as white AgCl",
            AGCL_SOLID
        ),
        node(
            AG_NOT_CONFIRMED_AFTER_HNO3,
            "Silver not confirmed after HNO3"
        ),
        node(
            PB_CONFIRMED_YELLOW_PBCRO4,
            "Lead confirmed as yellow PbCrO4",
            PB_CRO4_SOLID
        ),
        node(
            SB_CONFIRMED_LILAC_COMPLEX,
            "Antimony confirmed as lilac complex",
            SB_LILAC_COMPLEX
        )
    );

    static final List<WorkflowTransitionSpec> TRANSITIONS = List.of(
        transition(
            MIXTURE_I_TO_III,
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            GROUP1_CHLORIDE_PRECIPITATE,
            "Group I chlorides precipitated after dilute HCl/NaCl addition.",
            ObservedSignal.GROUP1_CHLORIDES_PRECIPITATED
        ),
        transition(
            GROUP1_CHLORIDE_PRECIPITATE,
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            POST_CHLORIDE_SPLIT,
            "Filtering prepared both post-chloride branches for selection.",
            ObservedSignal.POST_CHLORIDE_SPLIT_READY
        ),
        transition(
            POST_CHLORIDE_SPLIT,
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            POST_CHLORIDE_FILTRATE,
            "Filtrate branch selected after chloride filtration; Fe3+, Mn2+, Cu2+, Cd2+, Ni2+, Sn2+, Al3+, and Zn2+ remain in solution.",
            ObservedSignal.POST_CHLORIDE_FILTRATE_CATIONS_REMAIN_IN_SOLUTION
        ),
        transition(
            POST_CHLORIDE_SPLIT,
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            POST_CHLORIDE_PRECIPITATE_RESIDUE,
            "Precipitate residue branch selected for concentrated HCl treatment."
        ),
        transition(
            POST_CHLORIDE_PRECIPITATE_RESIDUE,
            ReactionAction.ADD_CONCENTRATED_HCL,
            POST_CONC_HCL_SPLIT,
            "Concentrated HCl treatment on chloride residue prepared residue and dissolved branches.",
            ObservedSignal.GROUP1_RESIDUE_READY,
            ObservedSignal.GROUP1_DISSOLVED_COMPLEXES_READY
        ),
        transition(
            POST_CHLORIDE_FILTRATE,
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            POST_ALKALINE_OXIDATIVE_SPLIT_POINT,
            "Excess NaOH + H2O2 with heat applied to filtrate; alkaline oxidative treatment performed and tin advanced to Sn4+.",
            ObservedSignal.ALKALINE_OXIDATIVE_TREATMENT_APPLIED,
            ObservedSignal.SN2_OXIDIZED_TO_SN4,
            ObservedSignal.ALKALINE_SOLID_LIQUID_SPLIT_READY
        ),
        transition(
            POST_ALKALINE_OXIDATIVE_SPLIT_POINT,
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            ALKALINE_PRECIPITATE_PHASE,
            "Precipitated fraction isolated for downstream cation separation.",
            ObservedSignal.ALKALINE_PRECIPITATE_ISOLATED
        ),
        transition(
            POST_ALKALINE_OXIDATIVE_SPLIT_POINT,
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH,
            ALKALINE_SOLUTION_PHASE,
            "Solution fraction isolated (stannite/aluminate/zincate path).",
            ObservedSignal.ALKALINE_SOLUTION_ISOLATED
        ),
        transition(
            ALKALINE_PRECIPITATE_PHASE,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ACID_DISSOLVED_PRECIPITATE_PHASE,
            "HCl with heat dissolved the precipitated fraction to Fe3+, Mn4+, Cu2+, Cd2+, and Ni2+.",
            ObservedSignal.ALKALINE_PRECIPITATE_DISSOLVED_BY_HCL_HEAT
        ),
        transition(
            POST_CONC_HCL_SPLIT,
            ReactionAction.SELECT_RESIDUE_BRANCH,
            GROUP1_RESIDUE_PATH_AG_PB_HG,
            "Residue branch selected (Ag/Pb/Hg path)."
        ),
        transition(
            POST_CONC_HCL_SPLIT,
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX,
            "Dissolved branch selected (Sb/Pb complex path)."
        ),
        transition(
            GROUP1_RESIDUE_PATH_AG_PB_HG,
            ReactionAction.ADD_EXCESS_NH4OH,
            POST_NH4OH_HG_SPLIT,
            "Excess NH4OH applied; evaluate whether black mercury residue appears."
        ),
        transition(
            POST_NH4OH_HG_SPLIT,
            ReactionAction.SELECT_HG_BLACK_RESIDUE_PRESENT_BRANCH,
            HG_CONFIRMED_BLACK_RESIDUE,
            "Black metallic mercury residue observed; mercury confirmed.",
            ObservedSignal.HG_BLACK_RESIDUE
        ),
        transition(
            POST_NH4OH_HG_SPLIT,
            ReactionAction.SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH,
            AG_AMMINE_PATH,
            "No black mercury residue observed; continue through silver ammine path.",
            ObservedSignal.AG_DIAMMINE_COMPLEX_FORMED
        ),
        transition(
            AG_AMMINE_PATH,
            ReactionAction.ADD_HNO3,
            POST_HNO3_SILVER_SPLIT,
            "HNO3 added to acidify the silver ammine path; evaluate AgCl precipitation."
        ),
        transition(
            POST_HNO3_SILVER_SPLIT,
            ReactionAction.SELECT_AGCL_PRECIPITATE_PRESENT_BRANCH,
            AG_CONFIRMED_WHITE_AGCL,
            "White AgCl precipitate observed; silver confirmed.",
            ObservedSignal.AG_WHITE_AGCL_CONFIRMED
        ),
        transition(
            POST_HNO3_SILVER_SPLIT,
            ReactionAction.SELECT_AGCL_PRECIPITATE_ABSENT_BRANCH,
            AG_NOT_CONFIRMED_AFTER_HNO3,
            "No AgCl precipitate observed after HNO3; silver not confirmed in this branch."
        ),
        transition(
            GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX,
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER,
            POST_K2CRO4_PB_SPLIT,
            "K2CrO4 with acetate buffer applied; evaluate whether yellow PbCrO4 precipitate appears."
        ),
        transition(
            POST_K2CRO4_PB_SPLIT,
            ReactionAction.SELECT_PBCRO4_PRECIPITATE_PRESENT_BRANCH,
            PB_CONFIRMED_YELLOW_PBCRO4,
            "Yellow PbCrO4 precipitate observed; lead confirmed.",
            ObservedSignal.PB_YELLOW_PBCRO4_CONFIRMED
        ),
        transition(
            POST_K2CRO4_PB_SPLIT,
            ReactionAction.SELECT_PBCRO4_PRECIPITATE_ABSENT_BRANCH,
            SB_CANDIDATE_AFTER_PB_NEGATIVE,
            "No yellow PbCrO4 precipitate observed; continue to antimony confirmation."
        ),
        transition(
            SB_CANDIDATE_AFTER_PB_NEGATIVE,
            ReactionAction.ADD_HCL_KNO2_RHODAMINE_B,
            SB_CONFIRMED_LILAC_COMPLEX,
            "HCl + KNO2 + Rhodamine B confirmed antimony as a lilac complex.",
            ObservedSignal.SB_LILAC_COMPLEX_CONFIRMED
        )
    );

    private GroupIWorkflowSpec() {
    }

    private static WorkflowNodeSpec node(String id, String label, ChemicalSpecies... expectedSpecies) {
        return new WorkflowNodeSpec(id, label, List.of(expectedSpecies));
    }

    private static WorkflowTransitionSpec transition(
        String fromNodeId,
        ReactionAction action,
        String toNodeId,
        String message,
        ObservedSignal... signals
    ) {
        return new WorkflowTransitionSpec(fromNodeId, action, toNodeId, List.of(signals), message);
    }
}
