package io.github.mrelizeus.qwcie.workflow;

import io.github.mrelizeus.qwcie.domain.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.ReactionAction;
import io.github.mrelizeus.qwcie.engine.Node;

import java.util.List;

public class GroupIWorkflowDefinition implements WorkflowDefinition {

    public static final String WORKFLOW_ID = "group1-left-branch";

    public static final String MIXTURE_I_TO_III = "MIXTURE_I_TO_III";
    public static final String GROUP1_CHLORIDE_PRECIPITATE = "GROUP1_CHLORIDE_PRECIPITATE";
    public static final String POST_CONC_HCL_SPLIT = "POST_CONC_HCL_SPLIT";
    public static final String GROUP1_RESIDUE_PATH_AG_PB_HG = "GROUP1_RESIDUE_PATH_AG_PB_HG";
    public static final String GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX = "GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX";
    public static final String HG_BLACK_RESIDUE_AND_AG_AMMINE = "HG_BLACK_RESIDUE_AND_AG_AMMINE";
    public static final String AG_CONFIRMED_WHITE_AGCL = "AG_CONFIRMED_WHITE_AGCL";
    public static final String PB_CONFIRMED_YELLOW_PBCRO4 = "PB_CONFIRMED_YELLOW_PBCRO4";
    public static final String SB_CONFIRMED_LILAC_COMPLEX = "SB_CONFIRMED_LILAC_COMPLEX";

    private final Node startNode;

    public GroupIWorkflowDefinition() {
        Node mixture = new Node(
            MIXTURE_I_TO_III,
            "Mixture of Group I to Group III cations",
            List.of("Ag+", "Pb2+", "Hg2_2+", "Sb3+", "Cu2+", "Cd2+", "Fe3+", "Mn2+", "Ni2+", "Al3+", "Zn2+")
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

        Node hgBlackAndAgAmmine = new Node(
            HG_BLACK_RESIDUE_AND_AG_AMMINE,
            "Hg black residue and silver ammine complex",
            List.of("Hg black residue", "[Ag(NH3)2]+")
        );

        Node agConfirmed = new Node(
            AG_CONFIRMED_WHITE_AGCL,
            "Silver confirmed as white AgCl",
            List.of("AgCl(s) white")
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
            ReactionAction.ADD_CONCENTRATED_HCL,
            postConcentratedSplit,
            List.of(ObservedSignal.GROUP1_RESIDUE_READY, ObservedSignal.GROUP1_DISSOLVED_COMPLEXES_READY),
            "Concentrated HCl prepared residue and dissolved branches."
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
            hgBlackAndAgAmmine,
            List.of(ObservedSignal.HG_BLACK_RESIDUE, ObservedSignal.AG_DIAMMINE_COMPLEX_FORMED),
            "Excess NH4OH produced black mercury residue and silver ammine complex."
        );

        hgBlackAndAgAmmine.addTransition(
            ReactionAction.ADD_HNO3,
            agConfirmed,
            List.of(ObservedSignal.AG_WHITE_AGCL_CONFIRMED),
            "HNO3 reconverted dissolved silver to white AgCl."
        );

        dissolvedPath.addTransition(
            ReactionAction.ADD_K2CRO4_WITH_ACETATE_BUFFER,
            pbConfirmed,
            List.of(ObservedSignal.PB_YELLOW_PBCRO4_CONFIRMED),
            "K2CrO4 with acetate buffer confirmed lead as yellow PbCrO4. Continue dissolved phase for antimony confirmation."
        );

        pbConfirmed.addTransition(
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
