package io.github.mrelizeus.qwcie.workflow.chloride;

import io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies;
import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.FractionPhase;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitBranchPolicy;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitPolicy;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowNodeSpec;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowTransitionSpec;

import java.util.List;

import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AGCL_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AG_DIAMMINE_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AG_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ALO2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AL_OH3_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.AL_3_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CD2_FE_CN6_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CD_OH2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CD_TETRAAMMINE_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CD_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CU2_FE_CN6_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CU_OH2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CU_TETRAAMMINE_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.CU_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.FE_OH3_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.FE_PRUSSIAN_BLUE_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.FE_SCN_BLOOD_RED_COMPLEX;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.FE_3_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.HG0_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.HG2CL2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.HG2_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.MNO2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.MNO4_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.MN_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.MN_4_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.NI_DMG2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.NI_OH2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.NI_TETRAAMMINE_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.NI_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PBCL2_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PBCL4_2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PB_CRO4_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.PB_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SBOCL_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SB_LILAC_COMPLEX;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SB_3_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SNCL6_2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SN_OH4_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SNO3_2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.SN_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ZN2_FE_CN6_SOLID;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ZNO2_2_MINUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ZN_TETRAAMMINE_2_PLUS;
import static io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies.ZN_2_PLUS;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.ACID_DISSOLVED_PRECIPITATE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.AG_AMMINE_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.AG_CONFIRMED_WHITE_AGCL;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.AG_NOT_CONFIRMED_AFTER_HNO3;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.ALKALINE_PRECIPITATE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.ALKALINE_SOLUTION_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CD_CANDIDATE_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CD_WHITE_FERROCYANIDE_CONFIRMED;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CHLORIDE_DISSOLVED_PATH_SB_PB_COMPLEX;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CHLORIDE_PRECIPITATE_NODE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CHLORIDE_RESIDUE_PATH_AG_PB_HG;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CU_CD_FERROCYANIDE_STAGE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CU_CD_SEPARATION_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.CU_CONFIRMED_SOLID_FERROCYANIDE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.AL3_CONFIRMATION_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.AL_CONFIRMED_RED_AL_OH3;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.FE_CONFIRMATION_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.FE_MN_OXIDIZED_SOLUTION_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.FE_MN_RESIDUE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.FE_PRUSSIAN_BLUE_CONFIRMED;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.FE_THIOCYANATE_BLOOD_RED_CONFIRMED;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.HG_CONFIRMED_BLACK_RESIDUE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.MIXTURE_I_TO_III;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.MN_CONFIRMATION_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.MN_PERMANGANATE_CONFIRMED;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.NI_CONFIRMATION_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.NI_CU_CD_ACIDIFIED_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.NI_CU_CD_AMMINE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.NI_DMG_SCARLET_CONFIRMED;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.PB_CONFIRMED_YELLOW_PBCRO4;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_ALKALINE_OXIDATIVE_SPLIT_POINT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_AMPHOTERIC_SPLIT_POINT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_AMMONIACAL_SPLIT_POINT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_CHLORIDE_FILTRATE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_CHLORIDE_PRECIPITATE_RESIDUE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_CHLORIDE_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_CONC_HCL_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_CU_CD_HCL_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_HNO3_SILVER_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_K2CRO4_PB_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_NH4OH_HG_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.POST_SN_AL_HCL_SPLIT;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.SB_CANDIDATE_AFTER_PB_NEGATIVE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.SB_CONFIRMED_LILAC_COMPLEX;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.SN2_REDUCED_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.SN_AL_HYDROXIDE_PRECIPITATE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.SN_CHLORO_COMPLEX_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.SN_CONFIRMED_HG2CL2_WHITE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.ZN_ACIDIFIED_PATH;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.ZN_AMMINE_PHASE;
import static io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition.ZN_CONFIRMED_WHITE_ZN2_FE_CN6;

final class ChlorideMainlineWorkflowSpec {

    static final List<WorkflowNodeSpec> NODES = List.of(
        node(
            MIXTURE_I_TO_III,
            "Initial cation mixture before chloride separation",
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
            CHLORIDE_PRECIPITATE_NODE,
            "Chloride precipitate fraction",
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
            POST_AMMONIACAL_SPLIT_POINT,
            "Post NH4OH split for hydroxide branch"
        ),
        node(
            FE_MN_RESIDUE_PHASE,
            "Fe/Mn residue phase after NH4OH",
            FE_OH3_SOLID,
            MNO2_SOLID
        ),
        node(
            NI_CU_CD_AMMINE_PHASE,
            "Ni/Cu/Cd ammine phase",
            NI_TETRAAMMINE_2_PLUS,
            CU_TETRAAMMINE_2_PLUS,
            CD_TETRAAMMINE_2_PLUS
        ),
        node(
            FE_MN_OXIDIZED_SOLUTION_PHASE,
            "Fe/Mn oxidized solution phase",
            FE_3_PLUS,
            MN_4_PLUS
        ),
        node(
            FE_CONFIRMATION_PATH,
            "Iron confirmation path",
            FE_3_PLUS
        ),
        node(
            FE_PRUSSIAN_BLUE_CONFIRMED,
            "Iron confirmed as Prussian blue",
            FE_PRUSSIAN_BLUE_SOLID
        ),
        node(
            FE_THIOCYANATE_BLOOD_RED_CONFIRMED,
            "Iron confirmed by blood-red thiocyanate complex",
            FE_SCN_BLOOD_RED_COMPLEX
        ),
        node(
            MN_CONFIRMATION_PATH,
            "Manganese confirmation path",
            MN_4_PLUS
        ),
        node(
            MN_PERMANGANATE_CONFIRMED,
            "Manganese confirmed as violet permanganate",
            MNO4_MINUS
        ),
        node(
            NI_CU_CD_ACIDIFIED_PHASE,
            "Ni/Cu/Cd acidified phase",
            NI_2_PLUS,
            CU_2_PLUS,
            CD_2_PLUS
        ),
        node(
            NI_CONFIRMATION_PATH,
            "Nickel confirmation path",
            NI_2_PLUS
        ),
        node(
            NI_DMG_SCARLET_CONFIRMED,
            "Nickel confirmed as scarlet DMG complex",
            NI_DMG2_SOLID
        ),
        node(
            CU_CD_SEPARATION_PATH,
            "Copper/cadmium separation path",
            CU_2_PLUS,
            CD_2_PLUS
        ),
        node(
            CU_CD_FERROCYANIDE_STAGE,
            "Copper/cadmium stage after K4Fe(CN)6",
            CU2_FE_CN6_SOLID,
            CD_2_PLUS
        ),
        node(
            POST_CU_CD_HCL_SPLIT,
            "Post HCl excess split for copper/cadmium",
            CU2_FE_CN6_SOLID,
            CD_2_PLUS
        ),
        node(
            CU_CONFIRMED_SOLID_FERROCYANIDE,
            "Copper confirmed as persistent ferrocyanide solid",
            CU2_FE_CN6_SOLID
        ),
        node(
            CD_CANDIDATE_PATH,
            "Cadmium candidate path",
            CD_2_PLUS
        ),
        node(
            CD_WHITE_FERROCYANIDE_CONFIRMED,
            "Cadmium confirmed as white ferrocyanide precipitate",
            CD2_FE_CN6_SOLID
        ),
        node(
            ALKALINE_SOLUTION_PHASE,
            "Alkaline solution phase",
            SNO3_2_MINUS,
            ALO2_MINUS,
            ZNO2_2_MINUS
        ),
        node(
            POST_AMPHOTERIC_SPLIT_POINT,
            "Post amphoteric split point"
        ),
        node(
            SN_AL_HYDROXIDE_PRECIPITATE_PHASE,
            "Sn/Al hydroxide precipitate phase",
            SN_OH4_SOLID,
            AL_OH3_SOLID
        ),
        node(
            ZN_AMMINE_PHASE,
            "Zinc ammine phase",
            ZN_TETRAAMMINE_2_PLUS
        ),
        node(
            POST_SN_AL_HCL_SPLIT,
            "Post Sn/Al HCl split",
            SNCL6_2_MINUS,
            AL_3_PLUS
        ),
        node(
            SN_CHLORO_COMPLEX_PATH,
            "Tin chloro-complex confirmation path",
            SNCL6_2_MINUS
        ),
        node(
            SN2_REDUCED_PATH,
            "Tin reduced path (Sn2+)",
            SN_2_PLUS
        ),
        node(
            SN_CONFIRMED_HG2CL2_WHITE,
            "Tin confirmed as white Hg2Cl2 precipitate",
            HG2CL2_SOLID
        ),
        node(
            AL3_CONFIRMATION_PATH,
            "Aluminum confirmation path",
            AL_3_PLUS
        ),
        node(
            AL_CONFIRMED_RED_AL_OH3,
            "Aluminum confirmed as red Al(OH)3 (aluminon test)",
            AL_OH3_SOLID
        ),
        node(
            ZN_ACIDIFIED_PATH,
            "Zinc acidified path",
            ZN_2_PLUS
        ),
        node(
            ZN_CONFIRMED_WHITE_ZN2_FE_CN6,
            "Zinc confirmed as white Zn2[Fe(CN)6] precipitate",
            ZN2_FE_CN6_SOLID
        ),
        node(
            CHLORIDE_RESIDUE_PATH_AG_PB_HG,
            "Residue branch: Ag/Pb/Hg",
            AGCL_SOLID,
            PBCL2_SOLID,
            HG2CL2_SOLID
        ),
        node(
            CHLORIDE_DISSOLVED_PATH_SB_PB_COMPLEX,
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
            CHLORIDE_PRECIPITATE_NODE,
            "Chloride precipitate formed after dilute HCl/NaCl addition.",
            ObservedSignal.CHLORIDE_PRECIPITATE_FORMED
        ),
        transition(
            CHLORIDE_PRECIPITATE_NODE,
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
            ObservedSignal.CHLORIDE_RESIDUE_READY,
            ObservedSignal.CHLORIDE_DISSOLVED_COMPLEXES_READY
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
            ALKALINE_SOLUTION_PHASE,
            ReactionAction.ADD_HCL_NH4OH_NH4CL,
            POST_AMPHOTERIC_SPLIT_POINT,
            "HCl + NH4OH + NH4Cl applied; amphoteric fractions prepared for Sn/Al and Zn confirmation.",
            ObservedSignal.AMPHOTERIC_SPLIT_READY
        ),
        transition(
            POST_AMPHOTERIC_SPLIT_POINT,
            ReactionAction.SELECT_SN_AL_HYDROXIDE_BRANCH,
            SN_AL_HYDROXIDE_PRECIPITATE_PHASE,
            "Sn/Al hydroxide fraction isolated from amphoteric split.",
            ObservedSignal.SN_AL_HYDROXIDE_FRACTION_ISOLATED
        ),
        transition(
            POST_AMPHOTERIC_SPLIT_POINT,
            ReactionAction.SELECT_ZN_AMMINE_BRANCH,
            ZN_AMMINE_PHASE,
            "Zinc ammine fraction isolated from amphoteric split.",
            ObservedSignal.ZN_AMMINE_FRACTION_ISOLATED
        ),
        transition(
            SN_AL_HYDROXIDE_PRECIPITATE_PHASE,
            ReactionAction.ADD_CONCENTRATED_HCL,
            POST_SN_AL_HCL_SPLIT,
            "Concentrated HCl dissolved Sn/Al hydroxides and prepared split for individual confirmation.",
            ObservedSignal.SN_AL_HCL_SPLIT_READY
        ),
        transition(
            POST_SN_AL_HCL_SPLIT,
            ReactionAction.SELECT_SN_CONFIRMATION_PATH,
            SN_CHLORO_COMPLEX_PATH,
            "Tin confirmation path selected from Sn/Al split."
        ),
        transition(
            POST_SN_AL_HCL_SPLIT,
            ReactionAction.SELECT_AL_CONFIRMATION_PATH,
            AL3_CONFIRMATION_PATH,
            "Aluminum confirmation path selected from Sn/Al split."
        ),
        transition(
            SN_CHLORO_COMPLEX_PATH,
            ReactionAction.ADD_FE0_WITH_HEAT,
            SN2_REDUCED_PATH,
            "Fe0 with heat reduced Sn chloro-complex to Sn2+.",
            ObservedSignal.SN2_REDUCED_BY_FE0
        ),
        transition(
            SN2_REDUCED_PATH,
            ReactionAction.ADD_HGCL2_EXCESS,
            SN_CONFIRMED_HG2CL2_WHITE,
            "White Hg2Cl2 precipitate observed; tin confirmed.",
            ObservedSignal.SN_CONFIRMED_WHITE_HG2CL2
        ),
        transition(
            AL3_CONFIRMATION_PATH,
            ReactionAction.ADD_NH4CH3COO_ALUMINON_NH4OH,
            AL_CONFIRMED_RED_AL_OH3,
            "Red Al(OH)3 aluminon signal observed; aluminum confirmed.",
            ObservedSignal.AL_CONFIRMED_RED_AL_OH3
        ),
        transition(
            ZN_AMMINE_PHASE,
            ReactionAction.ADD_CH3COOH,
            ZN_ACIDIFIED_PATH,
            "CH3COOH acidified zinc ammine fraction to Zn2+."
        ),
        transition(
            ZN_ACIDIFIED_PATH,
            ReactionAction.ADD_K4FE_CN6,
            ZN_CONFIRMED_WHITE_ZN2_FE_CN6,
            "White Zn2[Fe(CN)6] precipitate observed; zinc confirmed.",
            ObservedSignal.ZN_CONFIRMED_WHITE_ZN2_FE_CN6
        ),
        transition(
            ALKALINE_PRECIPITATE_PHASE,
            ReactionAction.ADD_HCL_WITH_HEAT,
            ACID_DISSOLVED_PRECIPITATE_PHASE,
            "HCl with heat dissolved the precipitated fraction to Fe3+, Mn4+, Cu2+, Cd2+, and Ni2+.",
            ObservedSignal.ALKALINE_PRECIPITATE_DISSOLVED_BY_HCL_HEAT
        ),
        transition(
            ACID_DISSOLVED_PRECIPITATE_PHASE,
            ReactionAction.ADD_EXCESS_NH4OH,
            POST_AMMONIACAL_SPLIT_POINT,
            "Excess NH4OH generated the Fe/Mn residue and Ni/Cu/Cd ammine fractions.",
            ObservedSignal.AMMONIACAL_SPLIT_READY
        ),
        transition(
            POST_AMMONIACAL_SPLIT_POINT,
            ReactionAction.SELECT_FE_MN_RESIDUE_BRANCH,
            FE_MN_RESIDUE_PHASE,
            "Fe/Mn residue branch selected after NH4OH treatment.",
            ObservedSignal.FE_MN_RESIDUE_ISOLATED
        ),
        transition(
            POST_AMMONIACAL_SPLIT_POINT,
            ReactionAction.SELECT_NI_CU_CD_AMMINE_BRANCH,
            NI_CU_CD_AMMINE_PHASE,
            "Ni/Cu/Cd ammine branch selected after NH4OH treatment.",
            ObservedSignal.NI_CU_CD_AMMINE_FRACTION_ISOLATED
        ),
        transition(
            FE_MN_RESIDUE_PHASE,
            ReactionAction.ADD_HNO3_WITH_HEAT,
            FE_MN_OXIDIZED_SOLUTION_PHASE,
            "HNO3 with heat dissolved the Fe/Mn residue to an oxidized Fe3+/Mn4+ solution.",
            ObservedSignal.FE_MN_NITRIC_DISSOLUTION_APPLIED
        ),
        transition(
            FE_MN_OXIDIZED_SOLUTION_PHASE,
            ReactionAction.SELECT_FE_CONFIRMATION_BRANCH,
            FE_CONFIRMATION_PATH,
            "Iron confirmation branch selected."
        ),
        transition(
            FE_CONFIRMATION_PATH,
            ReactionAction.ADD_KSCN,
            FE_THIOCYANATE_BLOOD_RED_CONFIRMED,
            "Blood-red thiocyanate complex observed; iron confirmed.",
            ObservedSignal.FE_THIOCYANATE_BLOOD_RED_CONFIRMED
        ),
        transition(
            FE_CONFIRMATION_PATH,
            ReactionAction.ADD_K4FE_CN6,
            FE_PRUSSIAN_BLUE_CONFIRMED,
            "Prussian blue precipitate observed; iron confirmed.",
            ObservedSignal.FE_PRUSSIAN_BLUE_CONFIRMED
        ),
        transition(
            FE_MN_OXIDIZED_SOLUTION_PHASE,
            ReactionAction.SELECT_MN_CONFIRMATION_BRANCH,
            MN_CONFIRMATION_PATH,
            "Manganese confirmation branch selected."
        ),
        transition(
            MN_CONFIRMATION_PATH,
            ReactionAction.ADD_NABIO3_WITH_HEAT,
            MN_PERMANGANATE_CONFIRMED,
            "Violet permanganate signal observed; manganese confirmed.",
            ObservedSignal.MN_PERMANGANATE_VIOLET_CONFIRMED
        ),
        transition(
            NI_CU_CD_AMMINE_PHASE,
            ReactionAction.ADD_CH3COOH,
            NI_CU_CD_ACIDIFIED_PHASE,
            "Acidification with CH3COOH converted ammine complexes to Ni2+, Cu2+, and Cd2+.",
            ObservedSignal.NI_CU_CD_ACIDIFIED
        ),
        transition(
            NI_CU_CD_ACIDIFIED_PHASE,
            ReactionAction.SELECT_NI_CONFIRMATION_BRANCH,
            NI_CONFIRMATION_PATH,
            "Nickel confirmation branch selected."
        ),
        transition(
            NI_CONFIRMATION_PATH,
            ReactionAction.ADD_DMG_WITH_NH4OH,
            NI_DMG_SCARLET_CONFIRMED,
            "Scarlet Ni-DMG precipitate observed; nickel confirmed.",
            ObservedSignal.NI_DMG_SCARLET_CONFIRMED
        ),
        transition(
            NI_CU_CD_ACIDIFIED_PHASE,
            ReactionAction.SELECT_CU_CD_SEPARATION_BRANCH,
            CU_CD_SEPARATION_PATH,
            "Copper/cadmium separation branch selected."
        ),
        transition(
            CU_CD_SEPARATION_PATH,
            ReactionAction.ADD_K4FE_CN6,
            CU_CD_FERROCYANIDE_STAGE,
            "Red-brown Cu2[Fe(CN)6] precipitate appeared after K4Fe(CN)6.",
            ObservedSignal.CU_FERROCYANIDE_RED_BROWN_OBSERVED
        ),
        transition(
            CU_CD_FERROCYANIDE_STAGE,
            ReactionAction.ADD_HCL_EXCESS,
            POST_CU_CD_HCL_SPLIT,
            "HCl excess applied to evaluate whether the ferrocyanide precipitate persists.",
            ObservedSignal.CU_CD_HCL_SPLIT_READY
        ),
        transition(
            POST_CU_CD_HCL_SPLIT,
            ReactionAction.SELECT_PRECIPITATE_PERSISTS_BRANCH,
            CU_CONFIRMED_SOLID_FERROCYANIDE,
            "Precipitate persisted after HCl excess; copper confirmed in solid phase.",
            ObservedSignal.CU_FERROCYANIDE_SOLID_CONFIRMED_AFTER_HCL_EXCESS
        ),
        transition(
            POST_CU_CD_HCL_SPLIT,
            ReactionAction.SELECT_PRECIPITATE_DISSOLVES_BRANCH,
            CD_CANDIDATE_PATH,
            "Precipitate dissolved after HCl excess; continue with cadmium confirmation."
        ),
        transition(
            CD_CANDIDATE_PATH,
            ReactionAction.ADD_H2SO4,
            CD_WHITE_FERROCYANIDE_CONFIRMED,
            "White Cd2[Fe(CN)6] precipitate observed; cadmium confirmed.",
            ObservedSignal.CD_WHITE_FERROCYANIDE_CONFIRMED
        ),
        transition(
            POST_CONC_HCL_SPLIT,
            ReactionAction.SELECT_RESIDUE_BRANCH,
            CHLORIDE_RESIDUE_PATH_AG_PB_HG,
            "Residue branch selected (Ag/Pb/Hg path)."
        ),
        transition(
            POST_CONC_HCL_SPLIT,
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            CHLORIDE_DISSOLVED_PATH_SB_PB_COMPLEX,
            "Dissolved branch selected (Sb/Pb complex path)."
        ),
        transition(
            CHLORIDE_RESIDUE_PATH_AG_PB_HG,
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
            CHLORIDE_DISSOLVED_PATH_SB_PB_COMPLEX,
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

    static final List<PhysicalSplitPolicy> PHYSICAL_SPLIT_POLICIES = List.of(
        splitPolicy(
            POST_CHLORIDE_SPLIT,
            branch(
                ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
                "Post-chloride filtrate branch",
                FractionPhase.SOLUTION
            ),
            branch(
                ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
                "Post-chloride precipitate branch",
                FractionPhase.PRECIPITATE
            )
        ),
        splitPolicy(
            POST_CONC_HCL_SPLIT,
            branch(
                ReactionAction.SELECT_RESIDUE_BRANCH,
                "Concentrated HCl residue branch",
                FractionPhase.PRECIPITATE
            ),
            branch(
                ReactionAction.SELECT_DISSOLVED_BRANCH,
                "Concentrated HCl dissolved branch",
                FractionPhase.SOLUTION
            )
        ),
        splitPolicy(
            POST_ALKALINE_OXIDATIVE_SPLIT_POINT,
            branch(
                ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
                "Alkaline precipitate branch",
                FractionPhase.PRECIPITATE
            ),
            branch(
                ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH,
                "Alkaline solution branch",
                FractionPhase.SOLUTION
            )
        ),
        splitPolicy(
            POST_AMPHOTERIC_SPLIT_POINT,
            branch(
                ReactionAction.SELECT_SN_AL_HYDROXIDE_BRANCH,
                "Sn/Al hydroxide branch",
                FractionPhase.PRECIPITATE
            ),
            branch(
                ReactionAction.SELECT_ZN_AMMINE_BRANCH,
                "Zn ammine branch",
                FractionPhase.SOLUTION
            )
        ),
        splitPolicy(
            POST_SN_AL_HCL_SPLIT,
            branch(
                ReactionAction.SELECT_SN_CONFIRMATION_PATH,
                "Sn confirmation aliquot",
                FractionPhase.SOLUTION
            ),
            branch(
                ReactionAction.SELECT_AL_CONFIRMATION_PATH,
                "Al confirmation aliquot",
                FractionPhase.SOLUTION
            )
        ),
        splitPolicy(
            POST_AMMONIACAL_SPLIT_POINT,
            branch(
                ReactionAction.SELECT_FE_MN_RESIDUE_BRANCH,
                "Fe/Mn residue branch",
                FractionPhase.PRECIPITATE
            ),
            branch(
                ReactionAction.SELECT_NI_CU_CD_AMMINE_BRANCH,
                "Ni/Cu/Cd ammine branch",
                FractionPhase.SOLUTION
            )
        ),
        splitPolicy(
            FE_MN_OXIDIZED_SOLUTION_PHASE,
            branch(
                ReactionAction.SELECT_FE_CONFIRMATION_BRANCH,
                "Fe confirmation aliquot",
                FractionPhase.SOLUTION
            ),
            branch(
                ReactionAction.SELECT_MN_CONFIRMATION_BRANCH,
                "Mn confirmation aliquot",
                FractionPhase.SOLUTION
            )
        ),
        splitPolicy(
            NI_CU_CD_ACIDIFIED_PHASE,
            branch(
                ReactionAction.SELECT_NI_CONFIRMATION_BRANCH,
                "Ni confirmation aliquot",
                FractionPhase.SOLUTION
            ),
            branch(
                ReactionAction.SELECT_CU_CD_SEPARATION_BRANCH,
                "Cu/Cd separation aliquot",
                FractionPhase.SOLUTION
            )
        )
    );

    private ChlorideMainlineWorkflowSpec() {
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

    private static PhysicalSplitPolicy splitPolicy(String nodeId, PhysicalSplitBranchPolicy... branches) {
        return new PhysicalSplitPolicy(nodeId, List.of(branches));
    }

    private static PhysicalSplitBranchPolicy branch(ReactionAction action, String label, FractionPhase phase) {
        return new PhysicalSplitBranchPolicy(action, label, phase);
    }
}
