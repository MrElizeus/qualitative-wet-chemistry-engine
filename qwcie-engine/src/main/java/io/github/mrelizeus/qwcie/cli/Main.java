package io.github.mrelizeus.qwcie.cli;

import io.github.mrelizeus.qwcie.domain.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.ReactionAction;
import io.github.mrelizeus.qwcie.engine.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.TransitionOutcome;
import io.github.mrelizeus.qwcie.workflow.GroupIWorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.WorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.WorkflowRegistry;

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
            ReactionAction.ADD_CONCENTRATED_HCL,
            ReactionAction.SELECT_RESIDUE_BRANCH,
            ReactionAction.ADD_EXCESS_NH4OH,
            ReactionAction.ADD_HNO3
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
