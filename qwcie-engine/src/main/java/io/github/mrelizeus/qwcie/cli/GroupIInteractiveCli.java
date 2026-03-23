package io.github.mrelizeus.qwcie.cli;

import io.github.mrelizeus.qwcie.domain.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.ReactionAction;
import io.github.mrelizeus.qwcie.engine.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.Node;
import io.github.mrelizeus.qwcie.engine.TransitionOutcome;
import io.github.mrelizeus.qwcie.workflow.GroupIWorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.WorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.WorkflowRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class GroupIInteractiveCli {

    public static void main(String[] args) {
        WorkflowRegistry registry = new WorkflowRegistry();
        WorkflowDefinition groupI = registry.get(GroupIWorkflowDefinition.WORKFLOW_ID);

        AnalysisEngine engine = new AnalysisEngine(groupI.startNode());

        printHeader(groupI.id());
        printCurrentNode(engine.getCurrentNode());

        List<ReactionAction> allActions = Arrays.stream(ReactionAction.values()).toList();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printActions(allActions, engine.getCurrentNode());
                System.out.print("Enter action number, 'r' to reset, or 'q' to quit: ");

                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    System.out.println("Bye.");
                    break;
                }

                if (input.equalsIgnoreCase("r")) {
                    engine = new AnalysisEngine(groupI.startNode());
                    System.out.println("Workflow reset.");
                    printCurrentNode(engine.getCurrentNode());
                    continue;
                }

                int selectedIndex;
                try {
                    selectedIndex = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input. Please enter a number, 'r', or 'q'.");
                    continue;
                }

                if (selectedIndex < 1 || selectedIndex > allActions.size()) {
                    System.out.println("Invalid action number.");
                    continue;
                }

                ReactionAction action = allActions.get(selectedIndex - 1);
                TransitionOutcome outcome = engine.applyAction(action);
                printOutcome(action, outcome);
                printCurrentNode(engine.getCurrentNode());
            }
        }
    }

    private static void printHeader(String workflowId) {
        System.out.println("============================================");
        System.out.println("QWCIE Interactive Runner");
        System.out.println("Workflow: " + workflowId);
        System.out.println("============================================");
    }

    private static void printCurrentNode(Node currentNode) {
        System.out.println("Current node ID: " + currentNode.getId());
        System.out.println("Current node label: " + currentNode.getLabel());

        if (currentNode.getExpectedSpecies().isEmpty()) {
            System.out.println("Expected species: none");
        } else {
            String species = currentNode
                .getExpectedSpecies()
                .stream()
                .sorted()
                .reduce((left, right) -> left + ", " + right)
                .orElse("none");
            System.out.println("Expected species: " + species);
        }

        System.out.println();
    }

    private static void printActions(List<ReactionAction> allActions, Node currentNode) {
        System.out.println("Available actions:");

        for (int i = 0; i < allActions.size(); i++) {
            ReactionAction action = allActions.get(i);
            boolean validFromCurrentNode = currentNode.getAvailableActions().contains(action);
            String marker = validFromCurrentNode ? "*" : " ";
            System.out.printf(Locale.ROOT, "%2d. [%s] %s%n", i + 1, marker, action);
        }

        System.out.println("Legend: [*] action is valid from current node");
        System.out.println();
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
}
