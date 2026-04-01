package io.github.mrelizeus.qwcie.workflow.chloride;

import io.github.mrelizeus.qwcie.engine.core.Node;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitPolicy;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowGraphFactory;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowSplitPolicyValidator;

import java.util.List;

public class ChlorideMainlineWorkflowDefinition implements WorkflowDefinition {

    public static final String WORKFLOW_ID = "chloride-first-mainline";

    public static final String MIXTURE_I_TO_III = "MIXTURE_I_TO_III";
    public static final String CHLORIDE_PRECIPITATE_NODE = "CHLORIDE_PRECIPITATE_NODE";
    public static final String POST_CHLORIDE_SPLIT = "POST_CHLORIDE_SPLIT";
    public static final String POST_CHLORIDE_FILTRATE = "POST_CHLORIDE_FILTRATE";
    public static final String POST_CHLORIDE_PRECIPITATE_RESIDUE = "POST_CHLORIDE_PRECIPITATE_RESIDUE";
    public static final String POST_ALKALINE_OXIDATIVE_SPLIT_POINT = "POST_ALKALINE_OXIDATIVE_SPLIT_POINT";
    public static final String ALKALINE_PRECIPITATE_PHASE = "ALKALINE_PRECIPITATE_PHASE";
    public static final String ACID_DISSOLVED_PRECIPITATE_PHASE = "ACID_DISSOLVED_PRECIPITATE_PHASE";
    public static final String ALKALINE_SOLUTION_PHASE = "ALKALINE_SOLUTION_PHASE";
    public static final String POST_AMPHOTERIC_SPLIT_POINT = "POST_AMPHOTERIC_SPLIT_POINT";
    public static final String SN_AL_HYDROXIDE_PRECIPITATE_PHASE = "SN_AL_HYDROXIDE_PRECIPITATE_PHASE";
    public static final String ZN_AMMINE_PHASE = "ZN_AMMINE_PHASE";
    public static final String POST_SN_AL_HCL_SPLIT = "POST_SN_AL_HCL_SPLIT";
    public static final String SN_CHLORO_COMPLEX_PATH = "SN_CHLORO_COMPLEX_PATH";
    public static final String SN2_REDUCED_PATH = "SN2_REDUCED_PATH";
    public static final String SN_CONFIRMED_HG2CL2_WHITE = "SN_CONFIRMED_HG2CL2_WHITE";
    public static final String AL3_CONFIRMATION_PATH = "AL3_CONFIRMATION_PATH";
    public static final String AL_CONFIRMED_RED_AL_OH3 = "AL_CONFIRMED_RED_AL_OH3";
    public static final String ZN_ACIDIFIED_PATH = "ZN_ACIDIFIED_PATH";
    public static final String ZN_CONFIRMED_WHITE_ZN2_FE_CN6 = "ZN_CONFIRMED_WHITE_ZN2_FE_CN6";
    public static final String POST_AMMONIACAL_SPLIT_POINT = "POST_AMMONIACAL_SPLIT_POINT";
    public static final String FE_MN_RESIDUE_PHASE = "FE_MN_RESIDUE_PHASE";
    public static final String NI_CU_CD_AMMINE_PHASE = "NI_CU_CD_AMMINE_PHASE";
    public static final String FE_MN_OXIDIZED_SOLUTION_PHASE = "FE_MN_OXIDIZED_SOLUTION_PHASE";
    public static final String FE_CONFIRMATION_PATH = "FE_CONFIRMATION_PATH";
    public static final String FE_PRUSSIAN_BLUE_CONFIRMED = "FE_PRUSSIAN_BLUE_CONFIRMED";
    public static final String FE_THIOCYANATE_BLOOD_RED_CONFIRMED = "FE_THIOCYANATE_BLOOD_RED_CONFIRMED";
    public static final String MN_CONFIRMATION_PATH = "MN_CONFIRMATION_PATH";
    public static final String MN_PERMANGANATE_CONFIRMED = "MN_PERMANGANATE_CONFIRMED";
    public static final String NI_CU_CD_ACIDIFIED_PHASE = "NI_CU_CD_ACIDIFIED_PHASE";
    public static final String NI_CONFIRMATION_PATH = "NI_CONFIRMATION_PATH";
    public static final String NI_DMG_SCARLET_CONFIRMED = "NI_DMG_SCARLET_CONFIRMED";
    public static final String CU_CD_SEPARATION_PATH = "CU_CD_SEPARATION_PATH";
    public static final String CU_CD_FERROCYANIDE_STAGE = "CU_CD_FERROCYANIDE_STAGE";
    public static final String POST_CU_CD_HCL_SPLIT = "POST_CU_CD_HCL_SPLIT";
    public static final String CU_CONFIRMED_SOLID_FERROCYANIDE = "CU_CONFIRMED_SOLID_FERROCYANIDE";
    public static final String CD_CANDIDATE_PATH = "CD_CANDIDATE_PATH";
    public static final String CD_WHITE_FERROCYANIDE_CONFIRMED = "CD_WHITE_FERROCYANIDE_CONFIRMED";
    public static final String POST_CONC_HCL_SPLIT = "POST_CONC_HCL_SPLIT";
    public static final String CHLORIDE_RESIDUE_PATH_AG_PB_HG = "CHLORIDE_RESIDUE_PATH_AG_PB_HG";
    public static final String CHLORIDE_DISSOLVED_PATH_SB_PB_COMPLEX = "CHLORIDE_DISSOLVED_PATH_SB_PB_COMPLEX";
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

    public ChlorideMainlineWorkflowDefinition() {
        WorkflowSplitPolicyValidator.validate(
            ChlorideMainlineWorkflowSpec.NODES,
            ChlorideMainlineWorkflowSpec.TRANSITIONS,
            ChlorideMainlineWorkflowSpec.PHYSICAL_SPLIT_POLICIES
        );
        this.startNode = WorkflowGraphFactory.buildStartNode(
            MIXTURE_I_TO_III,
            ChlorideMainlineWorkflowSpec.NODES,
            ChlorideMainlineWorkflowSpec.TRANSITIONS
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

    @Override
    public List<PhysicalSplitPolicy> physicalSplitPolicies() {
        return ChlorideMainlineWorkflowSpec.PHYSICAL_SPLIT_POLICIES;
    }
}
