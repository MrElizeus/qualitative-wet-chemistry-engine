package io.github.mrelizeus.qwcie.cli.app;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.core.Node;
import io.github.mrelizeus.qwcie.engine.core.TransitionOutcome;
import io.github.mrelizeus.qwcie.workflow.groupi.GroupIWorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class GroupIInteractiveCli {

    public static void main(String[] args) {
        WorkflowRegistry registry = new WorkflowRegistry();
        WorkflowDefinition groupI = registry.get(GroupIWorkflowDefinition.WORKFLOW_ID);

        AnalysisEngine engine = new AnalysisEngine(groupI.startNode());
        List<DecisionStep> decisionHistory = new ArrayList<>();

        printHeader(groupI.id());
        printCurrentNode(engine.getCurrentNode(), decisionHistory);

        List<ReactionAction> allActions = Arrays.stream(ReactionAction.values()).toList();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printDecisionSequence(decisionHistory);
                List<ReactionAction> selectableActions = printActions(allActions, engine.getCurrentNode());
                System.out.print("Enter action number, 'r' to reset, or 'q' to quit: ");

                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    System.out.println("Bye.");
                    break;
                }

                if (input.equalsIgnoreCase("r")) {
                    engine = new AnalysisEngine(groupI.startNode());
                    decisionHistory.clear();
                    System.out.println("Workflow reset.");
                    printCurrentNode(engine.getCurrentNode(), decisionHistory);
                    continue;
                }

                int selectedIndex;
                try {
                    selectedIndex = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input. Please enter a number, 'r', or 'q'.");
                    continue;
                }

                if (selectedIndex < 1 || selectedIndex > selectableActions.size()) {
                    System.out.println("Invalid action number.");
                    continue;
                }

                ReactionAction action = selectableActions.get(selectedIndex - 1);
                String fromNodeId = engine.getCurrentNode().getId();
                TransitionOutcome outcome = engine.applyAction(action);
                decisionHistory.add(new DecisionStep(
                    decisionHistory.size() + 1,
                    action,
                    fromNodeId,
                    outcome.currentNodeId(),
                    outcome.transitioned()
                ));
                printOutcome(action, outcome);
                printCurrentNode(engine.getCurrentNode(), decisionHistory);
            }
        }
    }

    private static void printHeader(String workflowId) {
        System.out.println("============================================");
        System.out.println("QWCIE Interactive Runner");
        System.out.println("Workflow: " + workflowId);
        System.out.println("============================================");
    }

    private static void printCurrentNode(Node currentNode, List<DecisionStep> decisionHistory) {
        System.out.println("Current node ID: " + currentNode.getId());
        System.out.println("Current node label: " + currentNode.getLabel());

        if (currentNode.getExpectedSpecies().isEmpty()) {
            System.out.println("Expected species: none");
        } else {
            String species = currentNode
                .getExpectedSpecies()
                .stream()
                .map(specie -> specie.notation())
                .sorted()
                .reduce((left, right) -> left + ", " + right)
                .orElse("none");
            System.out.println("Expected species: " + species);
        }

        if (decisionHistory.isEmpty()) {
            System.out.println("Decisions made: 0");
        } else {
            System.out.println("Decisions made: " + decisionHistory.size());
        }

        System.out.println();
    }

    private static void printDecisionSequence(List<DecisionStep> decisionHistory) {
        System.out.println("Decision sequence:");
        if (decisionHistory.isEmpty()) {
            System.out.println(" - none yet");
        } else {
            for (DecisionStep step : decisionHistory) {
                String status = step.transitioned() ? "OK" : "NO-TRANSITION";
                System.out.printf(
                    Locale.ROOT,
                    " %2d. [%s] %s | %s -> %s%n",
                    step.stepNumber(),
                    status,
                    step.action(),
                    step.fromNodeId(),
                    step.toNodeId()
                );
            }
        }
        System.out.println();
    }

    private static List<ReactionAction> printActions(List<ReactionAction> allActions, Node currentNode) {
        List<ReactionAction> validActions = allActions
            .stream()
            .filter(action -> currentNode.getAvailableActions().contains(action))
            .toList();

        if (currentNode.getId().equals(GroupIWorkflowDefinition.POST_CHLORIDE_SPLIT)) {
            System.out.println("Decision point: choose one branch for this run.");
            System.out.println(" - SELECT_POST_CHLORIDE_FILTRATE_BRANCH -> Filtrate branch (Fe/Mn/Cu/Cd/Ni/Sn/Al/Zn)");
            System.out.println(" - SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH -> Precipitate branch (Ag/Pb/Hg/Sb)");
            System.out.println();
        } else if (currentNode.getId().equals(GroupIWorkflowDefinition.POST_NH4OH_HG_SPLIT)) {
            System.out.println("Decision point: evaluate mercury after NH4OH.");
            System.out.println(" - SELECT_HG_BLACK_RESIDUE_PRESENT_BRANCH -> Confirm mercury (Hg0 black residue)");
            System.out.println(" - SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH -> Continue silver ammine path");
            System.out.println();
        } else if (currentNode.getId().equals(GroupIWorkflowDefinition.POST_HNO3_SILVER_SPLIT)) {
            System.out.println("Decision point: evaluate silver after HNO3.");
            System.out.println(" - SELECT_AGCL_PRECIPITATE_PRESENT_BRANCH -> Confirm silver as AgCl(s)");
            System.out.println(" - SELECT_AGCL_PRECIPITATE_ABSENT_BRANCH -> Silver not confirmed");
            System.out.println();
        } else if (currentNode.getId().equals(GroupIWorkflowDefinition.POST_K2CRO4_PB_SPLIT)) {
            System.out.println("Decision point: evaluate lead after K2CrO4 + acetate buffer.");
            System.out.println(" - SELECT_PBCRO4_PRECIPITATE_PRESENT_BRANCH -> Confirm lead as PbCrO4(s)");
            System.out.println(" - SELECT_PBCRO4_PRECIPITATE_ABSENT_BRANCH -> Continue antimony confirmation path");
            System.out.println();
        }

        System.out.println("Available actions from current node:");
        for (int i = 0; i < validActions.size(); i++) {
            ReactionAction action = validActions.get(i);
            System.out.printf(Locale.ROOT, "%2d. %s%s%n", i + 1, action, actionHint(action));
        }

        System.out.println();
        return validActions;
    }

    private static String actionHint(ReactionAction action) {
        return switch (action) {
            case FILTER_POST_CHLORIDE_FILTRATE -> "  [prepare post-chloride split]";
            case SELECT_POST_CHLORIDE_FILTRATE_BRANCH -> "  [filtrate path]";
            case SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH -> "  [precipitate path]";
            case SELECT_HG_BLACK_RESIDUE_PRESENT_BRANCH -> "  [Hg0 black residue observed]";
            case SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH -> "  [no Hg black residue; continue Ag path]";
            case SELECT_AGCL_PRECIPITATE_PRESENT_BRANCH -> "  [AgCl(s) observed; silver confirmed]";
            case SELECT_AGCL_PRECIPITATE_ABSENT_BRANCH -> "  [no AgCl(s); silver not confirmed]";
            case SELECT_PBCRO4_PRECIPITATE_PRESENT_BRANCH -> "  [PbCrO4(s) observed; lead confirmed]";
            case SELECT_PBCRO4_PRECIPITATE_ABSENT_BRANCH -> "  [no PbCrO4(s); continue Sb path]";
            case SELECT_RESIDUE_BRANCH -> "  [Ag/Pb/Hg residue branch]";
            case SELECT_DISSOLVED_BRANCH -> "  [Sb/Pb dissolved branch]";
            case SELECT_ALKALINE_PRECIPITATE_BRANCH -> "  [alkaline precipitate branch]";
            case SELECT_ALKALINE_SOLUTION_BRANCH -> "  [alkaline solution branch]";
            default -> "";
        };
    }

    private static void printOutcome(ReactionAction action, TransitionOutcome outcome) {
        System.out.println("Action: " + action);
        System.out.println("Transitioned: " + outcome.transitioned());
        System.out.println("Current node ID: " + outcome.currentNodeId());

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

    private record DecisionStep(
        int stepNumber,
        ReactionAction action,
        String fromNodeId,
        String toNodeId,
        boolean transitioned
    ) {
    }
}
