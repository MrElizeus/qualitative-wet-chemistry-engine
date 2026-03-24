package io.github.mrelizeus.qwcie.cli.app;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.core.TransitionOutcome;
import io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowRegistry;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        WorkflowRegistry registry = new WorkflowRegistry();
        WorkflowDefinition groupI = registry.get(GroupIWorkflowDefinition.WORKFLOW_ID);

        AnalysisEngine engine = new AnalysisEngine(groupI.startNode());

        System.out.println("Workflow: " + groupI.id());
        System.out.println("Starting node: " + engine.getCurrentNode().getId());

        List<ReactionAction> scenario = List.of(
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

        for (ReactionAction action : scenario) {
            TransitionOutcome outcome = engine.applyAction(action);
            printOutcome(action, outcome);
        }
    }

    private static void printOutcome(ReactionAction action, TransitionOutcome outcome) {
        System.out.println("Action: " + action);
        System.out.println("Transitioned: " + outcome.transitioned());
        System.out.println("Current node: " + outcome.currentNodeId());

        if (outcome.signals().isEmpty()) {
            System.out.println("Observed signals: none");
        } else {
            String signals = outcome
                .signals()
                .stream()
                .map(ObservedSignal::name)
                .reduce((left, right) -> left + ", " + right)
                .orElse("none");
            System.out.println("Observed signals: " + signals);
        }

        System.out.println("Message: " + outcome.message());
        System.out.println();
    }
}
