package io.github.mrelizeus.qwcie.cli.app;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;
import io.github.mrelizeus.qwcie.engine.core.BranchingAnalysisSession;
import io.github.mrelizeus.qwcie.engine.core.BranchingTransitionOutcome;
import io.github.mrelizeus.qwcie.engine.core.FractionSnapshot;
import io.github.mrelizeus.qwcie.engine.core.FractionStatus;
import io.github.mrelizeus.qwcie.engine.core.FractionSwitchOutcome;
import io.github.mrelizeus.qwcie.engine.core.Node;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowDefinition;
import io.github.mrelizeus.qwcie.workflow.api.WorkflowRegistry;
import io.github.mrelizeus.qwcie.workflow.chloride.ChlorideMainlineWorkflowDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ChlorideMainlineInteractiveCli {

    public static void main(String[] args) {
        WorkflowRegistry registry = new WorkflowRegistry();
        WorkflowDefinition mainline = registry.get(ChlorideMainlineWorkflowDefinition.WORKFLOW_ID);

        BranchingAnalysisSession session = new BranchingAnalysisSession(
            mainline.startNode(),
            mainline.physicalSplitPolicies()
        );

        List<DecisionStep> decisionHistory = new ArrayList<>();
        List<ReactionAction> allActions = Arrays.stream(ReactionAction.values()).toList();

        printHeader(mainline.id());
        printCurrentState(session, decisionHistory);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printDecisionSequence(decisionHistory);
                printFractionSummary(session.listFractions(), session.getActiveFraction().fractionId());

                List<ReactionAction> selectableActions = printActions(allActions, session.getCurrentNode());
                System.out.print("Enter action number, 'ls' list fractions, 's <id>' switch, 'r' reset, or 'q' quit: ");

                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    System.out.println("Bye.");
                    break;
                }

                if (input.equalsIgnoreCase("r")) {
                    session = new BranchingAnalysisSession(mainline.startNode(), mainline.physicalSplitPolicies());
                    decisionHistory.clear();
                    System.out.println("Workflow reset.");
                    printCurrentState(session, decisionHistory);
                    continue;
                }

                if (isListFractionsCommand(input)) {
                    printFractions(session.listFractions(), session.getActiveFraction().fractionId());
                    continue;
                }

                if (input.toLowerCase(Locale.ROOT).startsWith("s ")) {
                    String targetFractionId = input.substring(2).trim();
                    FractionSwitchOutcome switchOutcome = session.switchActiveFraction(targetFractionId);
                    printSwitchOutcome(switchOutcome);
                    printCurrentState(session, decisionHistory);
                    continue;
                }

                int selectedIndex;
                try {
                    selectedIndex = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input. Use a number, 'l', 's <id>', 'r', or 'q'.");
                    continue;
                }

                if (selectedIndex < 1 || selectedIndex > selectableActions.size()) {
                    System.out.println("Invalid action number.");
                    continue;
                }

                ReactionAction action = selectableActions.get(selectedIndex - 1);
                String fromNodeId = session.getCurrentNode().getId();
                String fromFractionId = session.getActiveFraction().fractionId();

                BranchingTransitionOutcome outcome = session.applyAction(action);
                decisionHistory.add(new DecisionStep(
                    decisionHistory.size() + 1,
                    fromFractionId,
                    action,
                    fromNodeId,
                    outcome.activeFractionId(),
                    outcome.currentNodeId(),
                    outcome.transitioned()
                ));

                printOutcome(action, outcome);
                printCurrentState(session, decisionHistory);
            }
        }
    }

    private static void printHeader(String workflowId) {
        System.out.println("============================================");
        System.out.println("QWCIE Interactive Runner");
        System.out.println("Workflow: " + workflowId);
        System.out.println("Mode: branching analysis session");
        System.out.println("============================================");
    }

    private static void printCurrentState(BranchingAnalysisSession session, List<DecisionStep> decisionHistory) {
        FractionSnapshot activeFraction = session.getActiveFraction();
        Node currentNode = session.getCurrentNode();

        System.out.println("Active fraction: " + activeFraction.fractionId());
        System.out.println("Active phase/status: " + activeFraction.phase() + " / " + activeFraction.status());
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

        System.out.println("Decisions made: " + decisionHistory.size());
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
                    " %2d. [%s] %s | %s:%s -> %s:%s%n",
                    step.stepNumber(),
                    status,
                    step.action(),
                    step.fromFractionId(),
                    step.fromNodeId(),
                    step.toFractionId(),
                    step.toNodeId()
                );
            }
        }
        System.out.println();
    }

    private static void printFractionSummary(List<FractionSnapshot> fractions, String activeFractionId) {
        long pendingCount = fractions.stream().filter(snapshot -> snapshot.status() == FractionStatus.PENDING).count();
        long closedCount = fractions.stream().filter(snapshot -> snapshot.status() == FractionStatus.CLOSED).count();

        System.out.printf(
            Locale.ROOT,
            "Fractions summary: total=%d, active=%s, pending=%d, closed=%d%n%n",
            fractions.size(),
            activeFractionId,
            pendingCount,
            closedCount
        );
    }

    private static void printFractions(List<FractionSnapshot> fractions, String activeFractionId) {
        System.out.println("Fractions inventory:");
        for (FractionSnapshot fraction : fractions) {
            String marker = fraction.fractionId().equals(activeFractionId) ? "*" : " ";
            System.out.printf(
                Locale.ROOT,
                " %s %s | %s | %s | node=%s | origin=%s (%s)%n",
                marker,
                fraction.fractionId(),
                fraction.phase(),
                fraction.status(),
                fraction.currentNodeId(),
                fraction.origin().branchLabel(),
                fraction.origin().parentFractionId()
            );
        }
        System.out.println();
    }

    private static boolean isListFractionsCommand(String input) {
        String normalized = input.trim().toLowerCase(Locale.ROOT);
        return normalized.equals("l")
            || normalized.equals("ls")
            || normalized.equals("list")
            || normalized.equals("fractions");
    }

    private static List<ReactionAction> printActions(List<ReactionAction> allActions, Node currentNode) {
        List<ReactionAction> validActions = allActions
            .stream()
            .filter(action -> currentNode.getAvailableActions().contains(action))
            .toList();

        if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_CHLORIDE_SPLIT)) {
            System.out.println("Decision point: split chloride filtrate vs precipitate.");
            System.out.println(" - SELECT_POST_CHLORIDE_FILTRATE_BRANCH -> activa filtrado; la otra fraccion queda pendiente");
            System.out.println(" - SELECT_POST_CHLORIDE_PRECIPITATE_BRANCH -> activa precipitado; la otra fraccion queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_CONC_HCL_SPLIT)) {
            System.out.println("Decision point: split concentrated HCl residue/dissolved branches.");
            System.out.println(" - SELECT_RESIDUE_BRANCH -> activa residuo; la otra fraccion queda pendiente");
            System.out.println(" - SELECT_DISSOLVED_BRANCH -> activa disuelto; la otra fraccion queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_ALKALINE_OXIDATIVE_SPLIT_POINT)) {
            System.out.println("Decision point: split alkaline precipitate/solution.");
            System.out.println(" - SELECT_ALKALINE_PRECIPITATE_BRANCH -> activa precipitado; la otra fraccion queda pendiente");
            System.out.println(" - SELECT_ALKALINE_SOLUTION_BRANCH -> activa solucion; la otra fraccion queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_AMPHOTERIC_SPLIT_POINT)) {
            System.out.println("Decision point: split Sn/Al hydroxides vs Zn ammine.");
            System.out.println(" - SELECT_SN_AL_HYDROXIDE_BRANCH -> activa Sn/Al; la otra fraccion queda pendiente");
            System.out.println(" - SELECT_ZN_AMMINE_BRANCH -> activa Zn; la otra fraccion queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_SN_AL_HCL_SPLIT)) {
            System.out.println("Decision point: choose Sn or Al confirmation aliquot.");
            System.out.println(" - SELECT_SN_CONFIRMATION_PATH -> activa confirmacion de Sn; Al queda pendiente");
            System.out.println(" - SELECT_AL_CONFIRMATION_PATH -> activa confirmacion de Al; Sn queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_AMMONIACAL_SPLIT_POINT)) {
            System.out.println("Decision point: split Fe/Mn residue vs Ni/Cu/Cd ammine.");
            System.out.println(" - SELECT_FE_MN_RESIDUE_BRANCH -> activa Fe/Mn; la otra fraccion queda pendiente");
            System.out.println(" - SELECT_NI_CU_CD_AMMINE_BRANCH -> activa Ni/Cu/Cd; la otra fraccion queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.FE_MN_OXIDIZED_SOLUTION_PHASE)) {
            System.out.println("Decision point: split Fe vs Mn confirmation aliquots.");
            System.out.println(" - SELECT_FE_CONFIRMATION_BRANCH -> activa Fe; Mn queda pendiente");
            System.out.println(" - SELECT_MN_CONFIRMATION_BRANCH -> activa Mn; Fe queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.NI_CU_CD_ACIDIFIED_PHASE)) {
            System.out.println("Decision point: split Ni vs Cu/Cd path.");
            System.out.println(" - SELECT_NI_CONFIRMATION_BRANCH -> activa Ni; Cu/Cd queda pendiente");
            System.out.println(" - SELECT_CU_CD_SEPARATION_BRANCH -> activa Cu/Cd; Ni queda pendiente");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_CU_CD_HCL_SPLIT)) {
            System.out.println("Decision point: evaluate Cu ferrocyanide after HCl excess.");
            System.out.println(" - SELECT_PRECIPITATE_PERSISTS_BRANCH -> Cu confirmed in solid phase");
            System.out.println(" - SELECT_PRECIPITATE_DISSOLVES_BRANCH -> Continue Cd confirmation");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_NH4OH_HG_SPLIT)) {
            System.out.println("Decision point: evaluate mercury after NH4OH.");
            System.out.println(" - SELECT_HG_BLACK_RESIDUE_PRESENT_BRANCH -> Confirm mercury (Hg0 black residue)");
            System.out.println(" - SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH -> Continue silver ammine path");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_HNO3_SILVER_SPLIT)) {
            System.out.println("Decision point: evaluate silver after HNO3.");
            System.out.println(" - SELECT_AGCL_PRECIPITATE_PRESENT_BRANCH -> Confirm silver as AgCl(s)");
            System.out.println(" - SELECT_AGCL_PRECIPITATE_ABSENT_BRANCH -> Silver not confirmed");
            System.out.println();
        } else if (currentNode.getId().equals(ChlorideMainlineWorkflowDefinition.POST_K2CRO4_PB_SPLIT)) {
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
            case ADD_HCL_NH4OH_NH4CL -> "  [prepare amphoteric Sn/Al/Zn split]";
            case ADD_FE0_WITH_HEAT -> "  [reduce Sn with Fe0 + heat]";
            case ADD_HGCL2_EXCESS -> "  [Sn confirmation as white Hg2Cl2]";
            case ADD_NH4CH3COO_ALUMINON_NH4OH -> "  [Al aluminon red confirmation]";
            case ADD_HNO3_WITH_HEAT -> "  [oxidative dissolution for Fe/Mn]";
            case ADD_KSCN -> "  [Fe blood-red thiocyanate test]";
            case ADD_K4FE_CN6 -> "  [ferrocyanide precipitation test]";
            case ADD_NABIO3_WITH_HEAT -> "  [Mn permanganate test]";
            case ADD_CH3COOH -> "  [acidify current solution fraction]";
            case ADD_DMG_WITH_NH4OH -> "  [Ni scarlet DMG confirmation]";
            case ADD_HCL_EXCESS -> "  [check Cu precipitate persistence]";
            case SELECT_PRECIPITATE_PERSISTS_BRANCH -> "  [Cu solid persists after HCl]";
            case SELECT_PRECIPITATE_DISSOLVES_BRANCH -> "  [precipitate dissolves; evaluate Cd]";
            case ADD_H2SO4 -> "  [Cd white precipitate confirmation]";
            case SELECT_HG_BLACK_RESIDUE_PRESENT_BRANCH -> "  [Hg0 black residue observed]";
            case SELECT_HG_BLACK_RESIDUE_ABSENT_BRANCH -> "  [no Hg black residue; continue Ag path]";
            case SELECT_AGCL_PRECIPITATE_PRESENT_BRANCH -> "  [AgCl(s) observed; silver confirmed]";
            case SELECT_AGCL_PRECIPITATE_ABSENT_BRANCH -> "  [no AgCl(s); silver not confirmed]";
            case SELECT_PBCRO4_PRECIPITATE_PRESENT_BRANCH -> "  [PbCrO4(s) observed; lead confirmed]";
            case SELECT_PBCRO4_PRECIPITATE_ABSENT_BRANCH -> "  [no PbCrO4(s); continue Sb path]";
            case ADD_CONCENTRATED_HCL -> "  [acid treatment for split preparation]";
            default -> "";
        };
    }

    private static void printSwitchOutcome(FractionSwitchOutcome switchOutcome) {
        System.out.println("Switch active fraction:");
        System.out.println("Switched: " + switchOutcome.switched());
        System.out.println("Active fraction: " + switchOutcome.activeFractionId());
        System.out.println("Current node ID: " + switchOutcome.currentNodeId());
        System.out.println("Message: " + switchOutcome.message());
        System.out.println();
    }

    private static void printOutcome(ReactionAction action, BranchingTransitionOutcome outcome) {
        System.out.println("Action: " + action);
        System.out.println("Transitioned: " + outcome.transitioned());
        System.out.println("Active fraction: " + outcome.activeFractionId());
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

        if (outcome.createdFractions().isEmpty()) {
            System.out.println("Created fractions: none");
        } else {
            String created = outcome.createdFractions()
                .stream()
                .map(snapshot -> snapshot.fractionId() + "(" + snapshot.phase() + ":" + snapshot.currentNodeId() + ")")
                .reduce((left, right) -> left + ", " + right)
                .orElse("none");
            System.out.println("Created fractions: " + created);
        }

        if (outcome.closedFractionIds().isEmpty()) {
            System.out.println("Closed fractions: none");
        } else {
            String closed = outcome.closedFractionIds().stream().reduce((left, right) -> left + ", " + right).orElse("none");
            System.out.println("Closed fractions: " + closed);
        }

        if (outcome.activatedFractionIds().isEmpty()) {
            System.out.println("Activated fractions: none");
        } else {
            String activated = outcome.activatedFractionIds().stream().reduce((left, right) -> left + ", " + right).orElse("none");
            System.out.println("Activated fractions: " + activated);
        }

        System.out.println("Message: " + outcome.message());
        System.out.println();
    }

    private record DecisionStep(
        int stepNumber,
        String fromFractionId,
        ReactionAction action,
        String fromNodeId,
        String toFractionId,
        String toNodeId,
        boolean transitioned
    ) {
    }
}
