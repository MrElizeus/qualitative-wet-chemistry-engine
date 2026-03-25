package io.github.mrelizeus.qwcie.workflow.groupi;

import io.github.mrelizeus.qwcie.engine.core.Node;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowGraphFactory;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowDefinition;

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
        this.startNode = WorkflowGraphFactory.buildStartNode(
            MIXTURE_I_TO_III,
            GroupIWorkflowSpec.NODES,
            GroupIWorkflowSpec.TRANSITIONS
        );
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
