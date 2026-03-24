package io.github.mrelizeus.qwcie.workflow.groupi;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.Node;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowDefinition;

import java.util.List;

public class GroupIWorkflowDefinition implements WorkflowDefinition {

    public static final String WORKFLOW_ID = "chloride-first-mainline";

    public static final String MIXTURE_I_TO_III = "MIXTURE_I_TO_III";
    public static final String GROUP1_CHLORIDE_PRECIPITATE = "GROUP1_CHLORIDE_PRECIPITATE";
    public static final String POST_CHLORIDE_SPLIT = "POST_CHLORIDE_SPLIT";
    public static final String POST_CHLORIDE_FILTRATE = "POST_CHLORIDE_FILTRATE";
    public static final String POST_CHLORIDE_PRECIPITATE_RESIDUE = "POST_CHLORIDE_PRECIPITATE_RESIDUE";
    public static final String POST_ALKALINE_OXIDATIVE_SPLIT_POINT = "POST_ALKALINE_OXIDATIVE_SPLIT_POINT";
    public static final String ALKALINE_PRECIPITATE_PHASE = "ALKALINE_PRECIPITATE_PHASE";
    public static final String ACID_DISSOLVED_PRECIPITATE_PHASE = "ACID_DISSOLVED_PRECIPITATE_PHASE";
    public static final String ALKALINE_SOLUTION_PHASE = "ALKALINE_SOLUTION_PHASE";
    public static final String POST_CONC_HCL_SPLIT = "POST_CONC_HCL_SPLIT";
    public static final String GROUP1_RESIDUE_PATH_AG_PB_HG = "GROUP1_RESIDUE_PATH_AG_PB_HG";
    public static final String GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX = "GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX";
    public static final String POST_NH4OH_HG_SPLIT = "POST_NH4OH_HG_SPLIT";
    public static final String POST_K2CRO4_PB_SPLIT = "POST_K2CRO4_PB_SPLIT";
    public static final String SB_CANDIDATE_AFTER_PB_NEGATIVE = "SB_CANDIDATE_AFTER_PB_NEGATIVE";
    public static final String HG_CONFIRMED_BLACK_RESIDUE = "HG_CONFIRMED_BLACK_RESIDUE";
    public static final String AG_AMMINE_PATH = "AG_AMMINE_PATH";
    public static final String POST_HNO3_SILVER_SPLIT = "POST_HNO3_SILVER_SPLIT";
    public static final String AG_CONFIRMED_WHITE_AGCL = "AG_CONFIRMED_WHITE_AGCL";
    public static final String AG_NOT_CONFIRMED_AFTER_HNO3 = "AG_NOT_CONFIRMED_AFTER_HNO3";
    public static final String PB_CONFIRMED_YELLOW_PBCRO4 = "PB_CONFIRMED_YELLOW_PBCRO4";
    public static final String SB_CONFIRMED_LILAC_COMPLEX = "SB_CONFIRMED_LILAC_COMPLEX";

    private final Node startNode;

    public GroupIWorkflowDefinition() {
        Node mixture = new Node(
            MIXTURE_I_TO_III,
            "Mixture of Group I to Group III cations",
            List.of("Ag+", "Pb2+", "Hg2_2+", "Sb3+", "Sn2+", "Cu2+", "Cd2+", "Fe3+", "Mn2+", "Ni2+", "Al3+", "Zn2+")
        );

        Node chloridePrecipitate = new Node(
            GROUP1_CHLORIDE_PRECIPITATE,
            "Group I chloride precipitate",
            List.of("AgCl(s)", "PbCl2(s)", "Hg2Cl2(s)", "SbOCl(s)")
        );

        Node postConcentratedSplit = new Node(
            POST_CONC_HCL_SPLIT,
            "Post concentrated HCl split",
            List.of("AgCl(s)", "PbCl2(s)", "Hg2Cl2(s)", "Sb3+", "PbCl4^2-")
        );

        Node filtratePostChlorides = new Node(
            POST_CHLORIDE_FILTRATE,
            "Filtrate after chloride precipitation step",
            List.of("Fe3+", "Mn2+", "Cu2+", "Cd2+", "Ni2+", "Sn2+", "Al3+", "Zn2+")
        );

        Node postChlorideSplit = new Node(
            POST_CHLORIDE_SPLIT,
            "Post chloride filtration split",
            List.of("Filtered chloride residue and filtrate ready for branch selection")
        );

        Node chloridePrecipitateResidue = new Node(
            POST_CHLORIDE_PRECIPITATE_RESIDUE,
            "Post chloride precipitate residue",
            List.of("AgCl(s)", "PbCl2(s)", "Hg2Cl2(s)", "SbOCl(s)")
        );

        Node filtrateAfterAlkalineOxidativeTreatment = new Node(
            POST_ALKALINE_OXIDATIVE_SPLIT_POINT,
            "Post NaOH excess + H2O2 + heat split point",
            List.of("Mixed solid and liquid phases after alkaline oxidative treatment")
        );

        Node alkalinePrecipitatePhase = new Node(
            ALKALINE_PRECIPITATE_PHASE,
            "Alkaline precipitate phase",
            List.of("Fe(OH)3(s)", "MnO2(s)", "Cu(OH)2(s)", "Cd(OH)2(s)", "Ni(OH)2(s)")
        );

        Node alkalinePrecipitateDissolved = new Node(
            ACID_DISSOLVED_PRECIPITATE_PHASE,
            "Alkaline precipitate dissolved with HCl and heat",
            List.of("Fe3+", "Mn4+", "Cu2+", "Cd2+", "Ni2+")
        );

        Node alkalineSolutionPhase = new Node(
            ALKALINE_SOLUTION_PHASE,
            "Alkaline solution phase",
            List.of("SnO3^2-", "AlO2-", "ZnO2^2-")
        );

        Node residuePath = new Node(
            GROUP1_RESIDUE_PATH_AG_PB_HG,
            "Residue branch: Ag/Pb/Hg",
            List.of("AgCl(s)", "PbCl2(s)", "Hg2Cl2(s)")
        );

        Node dissolvedPath = new Node(
            GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX,
            "Dissolved branch: Sb/Pb complexes",
            List.of("Sb3+", "PbCl4^2-")
        );

        Node postNh4ohHgSplit = new Node(
            POST_NH4OH_HG_SPLIT,
            "Post NH4OH mercury check split",
            List.of("Hg black residue candidate", "[Ag(NH3)2]+ candidate")
        );

        Node postK2cro4PbSplit = new Node(
            POST_K2CRO4_PB_SPLIT,
            "Post K2CrO4 lead check split",
            List.of("PbCrO4(s) candidate", "Sb3+ candidate")
        );

        Node sbCandidateAfterPbNegative = new Node(
            SB_CANDIDATE_AFTER_PB_NEGATIVE,
            "Antimony candidate path after negative PbCrO4",
            List.of("Sb3+")
        );

        Node hgConfirmed = new Node(
            HG_CONFIRMED_BLACK_RESIDUE,
            "Mercury confirmed as black metallic residue",
            List.of("Hg0(s) black")
        );

        Node agAmminePath = new Node(
            AG_AMMINE_PATH,
            "Silver ammine path",
            List.of("[Ag(NH3)2]+")
        );

        Node postHno3SilverSplit = new Node(
            POST_HNO3_SILVER_SPLIT,
            "Post HNO3 silver check split",
            List.of("Acidified silver path ready for AgCl precipitation check")
        );

        Node agConfirmed = new Node(
            AG_CONFIRMED_WHITE_AGCL,
            "Silver confirmed as white AgCl",
            List.of("AgCl(s) white")
        );

        Node agNotConfirmed = new Node(
            AG_NOT_CONFIRMED_AFTER_HNO3,
            "Silver not confirmed after HNO3",
            List.of("No AgCl(s) precipitate observed")
        );

        Node pbConfirmed = new Node(
            PB_CONFIRMED_YELLOW_PBCRO4,
            "Lead confirmed as yellow PbCrO4",
            List.of("PbCrO4(s) yellow")
        );

        Node sbConfirmed = new Node(
            SB_CONFIRMED_LILAC_COMPLEX,
            "Antimony confirmed as lilac complex",
            List.of("Sb complex lilac")
        );

        mixture.addTransition(
            ReactionAction.ADD_DILUTE_HCL_OR_NACL,
            chloridePrecipitate,
            List.of(ObservedSignal.GROUP1_CHLORIDES_PRECIPITATED),
            "Group I chlorides precipitated after dilute HCl/NaCl addition."
        );

        chloridePrecipitate.addTransition(
            ReactionAction.FILTER_POST_CHLORIDE_FILTRATE,
            postChlorideSplit,
            List.of(ObservedSignal.POST_CHLORIDE_SPLIT_READY),
            "Filtering prepared both post-chloride branches for selection."
        );

        postChlorideSplit.addTransition(
            ReactionAction.SELECT_POST_CHLORIDE_FILTRATE_BRANCH,
            filtratePostChlorides,
            List.of(ObservedSignal.POST_CHLORIDE_FILTRATE_CATIONS_REMAIN_IN_SOLUTION),
            "Filtrate branch selected after chloride filtration; Fe3+, Mn2+, Cu2+, Cd2+, Ni2+, Sn2+, Al3+, and Zn2+ remain in solution."
        );

        postChlorideSplit.addTransition(
            ReactionAction.SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH,
            chloridePrecipitateResidue,
            List.of(),
            "Precipitate residue branch selected for concentrated HCl treatment."
        );

        chloridePrecipitateResidue.addTransition(
            ReactionAction.ADD_CONCENTRATED_HCL,
            postConcentratedSplit,
            List.of(ObservedSignal.GROUP1_RESIDUE_READY, ObservedSignal.GROUP1_DISSOLVED_COMPLEXES_READY),
            "Concentrated HCl treatment on chloride residue prepared residue and dissolved branches."
        );

        filtratePostChlorides.addTransition(
            ReactionAction.ADD_EXCESS_NAOH_H2O2_WITH_HEAT,
            filtrateAfterAlkalineOxidativeTreatment,
            List.of(
                ObservedSignal.ALKALINE_OXIDATIVE_TREATMENT_APPLIED,
                ObservedSignal.SN2_OXIDIZED_TO_SN4,
                ObservedSignal.ALKALINE_SOLID_LIQUID_SPLIT_READY
            ),
            "Excess NaOH + H2O2 with heat applied to filtrate; alkaline oxidative treatment performed and tin advanced to Sn4+."
        );

        filtrateAfterAlkalineOxidativeTreatment.addTransition(
            ReactionAction.SELECT_ALKALINE_PRECIPITATE_BRANCH,
            alkalinePrecipitatePhase,
            List.of(ObservedSignal.ALKALINE_PRECIPITATE_ISOLATED),
            "Precipitated fraction isolated for downstream cation separation."
        );

        filtrateAfterAlkalineOxidativeTreatment.addTransition(
            ReactionAction.SELECT_ALKALINE_SOLUTION_BRANCH,
            alkalineSolutionPhase,
            List.of(ObservedSignal.ALKALINE_SOLUTION_ISOLATED),
            "Solution fraction isolated (stannite/aluminate/zincate path)."
        );

        alkalinePrecipitatePhase.addTransition(
            ReactionAction.ADD_HCL_WITH_HEAT,
            alkalinePrecipitateDissolved,
            List.of(ObservedSignal.ALKALINE_PRECIPITATE_DISSOLVED_BY_HCL_HEAT),
            "HCl with heat dissolved the precipitated fraction to Fe3+, Mn4+, Cu2+, Cd2+, and Ni2+."
        );

        postConcentratedSplit.addTransition(
            ReactionAction.SELECT_RESIDUE_BRANCH,
            residuePath,
            List.of(),
            "Residue branch selected (Ag/Pb/Hg path)."
        );

        postConcentratedSplit.addTransition(
            ReactionAction.SELECT_DISSOLVED_BRANCH,
            dissolvedPath,
            List.of(),
            "Dissolved branch selected (Sb/Pb complex path)."
        );

        residuePath.addTransition(
            ReactionAction.ADD_EXCESS_NH4OH,
            postNh4ohHgSplit,
            List.of(),
            "Excess NH4OH applied; evaluate whether black mercury residue appears."
        );

        postNh4ohHgSplit.addTransition(
            ReactionAction.SELECT_HG_BLACK_RESIDUE_PRESENT_BRANCH,
            hgConfirmed,
            List.of(ObservedSignal.HG_BLACK_RESIDUE),
            "Black metallic mercury residue observed; mercury confirmed."
        );

        postNh4ohHgSplit.addTransition(
            ReactionAction.SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH,
            agAmminePath,
            List.of(ObservedSignal.AG_DIAMMINE_COMPLEX_FORMED),
            "No black mercury residue observed; continue through silver ammine path."
        );

        agAmminePath.addTransition(
            ReactionAction.ADD_HNO3,
            postHno3SilverSplit,
            List.of(),
            "HNO3 added to acidify the silver ammine path; evaluate AgCl precipitation."
        );

        postHno3SilverSplit.addTransition(
            ReactionAction.SELECT_AGCL_PRECIPITATE_PRESENT_BRANCH,
            agConfirmed,
            List.of(ObservedSignal.AG_WHITE_AGCL_CONFIRMED),
            "White AgCl precipitate observed; silver confirmed."
        );

        postHno3SilverSplit.addTransition(
            ReactionAction.SELECT_AGCL_PRECIPITATE_ABSENT_BRANCH,
            agNotConfirmed,
            List.of(),
            "No AgCl precipitate observed after HNO3; silver not confirmed in this branch."
        );

        dissolvedPath.addTransition(
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER,
            postK2cro4PbSplit,
            List.of(),
            "K2CrO4 with acetate buffer applied; evaluate whether yellow PbCrO4 precipitate appears."
        );

        postK2cro4PbSplit.addTransition(
            ReactionAction.SELECT_PBCRO4_PRECIPITATE_PRESENT_BRANCH,
            pbConfirmed,
            List.of(ObservedSignal.PB_YELLOW_PBCRO4_CONFIRMED),
            "Yellow PbCrO4 precipitate observed; lead confirmed."
        );

        postK2cro4PbSplit.addTransition(
            ReactionAction.SELECT_PBCRO4_PRECIPITATE_ABSENT_BRANCH,
            sbCandidateAfterPbNegative,
            List.of(),
            "No yellow PbCrO4 precipitate observed; continue to antimony confirmation."
        );

        sbCandidateAfterPbNegative.addTransition(
            ReactionAction.ADD_HCL_KNO2_RHODAMINE_B,
            sbConfirmed,
            List.of(ObservedSignal.SB_LILAC_COMPLEX_CONFIRMED),
            "HCl + KNO2 + Rhodamine B confirmed antimony as a lilac complex."
        );

        this.startNode = mixture;
    }

    @Override
    public String id() {
        return WORKFLOW_ID;
    }

    @Override
    public Node startNode() {
        return startNode;
    }
}
