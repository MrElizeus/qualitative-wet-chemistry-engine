package io.github.mrelizeus.qwcie.workflow.chloride;

import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitBranchPolicy;
import io.github.mrelizeus.qwcie.engine.core.PhysicalSplitPolicy;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowTransitionSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChlorideMainlineSplitPolicyContractTest {

    @Test
    void containsExpectedPhysicalSplitNodes() {
        Set<String> actualSplitNodes = ChlorideMainlineWorkflowSpec.PHYSICAL_SPLIT_POLICIES
            .stream()
            .map(PhysicalSplitPolicy::nodeId)
            .collect(Collectors.toSet());

        Set<String> expectedSplitNodes = Set.of(
            ChlorideMainlineWorkflowDefinition.POST_CHLORIDE_SPLIT,
            ChlorideMainlineWorkflowDefinition.POST_CONC_HCL_SPLIT,
            ChlorideMainlineWorkflowDefinition.POST_ALKALINE_OXIDATIVE_SPLIT_POINT,
            ChlorideMainlineWorkflowDefinition.POST_AMPHOTERIC_SPLIT_POINT,
            ChlorideMainlineWorkflowDefinition.POST_SN_AL_HCL_SPLIT,
            ChlorideMainlineWorkflowDefinition.POST_AMMONIACAL_SPLIT_POINT,
            ChlorideMainlineWorkflowDefinition.FE_MN_OXIDIZED_SOLUTION_PHASE,
            ChlorideMainlineWorkflowDefinition.NI_CU_CD_ACIDIFIED_PHASE
        );

        assertEquals(expectedSplitNodes, actualSplitNodes);
    }

    @Test
    void everySplitBranchActionIsDefinedAsTransitionInTheSameNode() {
        Map<String, Set<ReactionAction>> transitionsByNode = ChlorideMainlineWorkflowSpec.TRANSITIONS
            .stream()
            .collect(
                Collectors.groupingBy(
                    WorkflowTransitionSpec::fromNodeId,
                    Collectors.mapping(WorkflowTransitionSpec::action, Collectors.toSet())
                )
            );

        for (PhysicalSplitPolicy splitPolicy : ChlorideMainlineWorkflowSpec.PHYSICAL_SPLIT_POLICIES) {
            Set<ReactionAction> nodeActions = transitionsByNode.getOrDefault(splitPolicy.nodeId(), Set.of());
            for (PhysicalSplitBranchPolicy branch : splitPolicy.branches()) {
                assertTrue(
                    nodeActions.contains(branch.action()),
                    "missing split branch action " + branch.action() + " in node " + splitPolicy.nodeId()
                );
            }
        }
    }

    @Test
    void hasNoDuplicatedSplitNodesOrDuplicatedBranchActionsWithinANode() {
        Map<String, Long> splitNodeFrequency = ChlorideMainlineWorkflowSpec.PHYSICAL_SPLIT_POLICIES
            .stream()
            .collect(Collectors.groupingBy(PhysicalSplitPolicy::nodeId, Collectors.counting()));

        for (Map.Entry<String, Long> entry : splitNodeFrequency.entrySet()) {
            assertEquals(1L, entry.getValue(), "duplicated split policy in node " + entry.getKey());
        }

        for (PhysicalSplitPolicy splitPolicy : ChlorideMainlineWorkflowSpec.PHYSICAL_SPLIT_POLICIES) {
            Map<ReactionAction, Long> branchActionFrequency = splitPolicy.branches()
                .stream()
                .map(PhysicalSplitBranchPolicy::action)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            for (Map.Entry<ReactionAction, Long> entry : branchActionFrequency.entrySet()) {
                assertEquals(
                    1L,
                    entry.getValue(),
                    "duplicated split branch action " + entry.getKey() + " in node " + splitPolicy.nodeId()
                );
            }
        }
    }
}
