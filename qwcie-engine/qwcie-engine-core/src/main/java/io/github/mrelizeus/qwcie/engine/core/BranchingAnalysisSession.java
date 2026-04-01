package io.github.mrelizeus.qwcie.engine.core;

import io.github.mrelizeus.qwcie.domain.protocol.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.protocol.ReactionAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class BranchingAnalysisSession {

    private final Map<String, FractionState> fractionsById = new LinkedHashMap<>();
    private final Map<String, PhysicalSplitPolicy> splitPoliciesByNodeId;
    private int fractionSequence;
    private String activeFractionId;

    public BranchingAnalysisSession(Node startNode) {
        this(startNode, List.of());
    }

    public BranchingAnalysisSession(Node startNode, Collection<PhysicalSplitPolicy> splitPolicies) {
        Node normalizedStartNode = Objects.requireNonNull(startNode, "start node cannot be null");
        splitPoliciesByNodeId = indexSplitPolicies(splitPolicies);

        FractionState rootFraction = new FractionState(
            nextFractionId(),
            normalizedStartNode,
            FractionPhase.MIXED,
            FractionStatus.ACTIVE,
            FractionOrigin.root()
        );
        fractionsById.put(rootFraction.id(), rootFraction);
        activeFractionId = rootFraction.id();
    }

    public BranchingTransitionOutcome applyAction(ReactionAction action) {
        Objects.requireNonNull(action, "reaction action cannot be null");

        FractionState activeFraction = activeFractionState();
        Node activeNode = activeFraction.currentNode();

        PhysicalSplitPolicy splitPolicy = splitPoliciesByNodeId.get(activeNode.getId());
        if (splitPolicy != null && isSplitBranchAction(splitPolicy, action)) {
            return applySplitSelection(activeFraction, splitPolicy, action);
        }

        Optional<NodeTransition> maybeTransition = activeNode.transitionFor(action);
        if (maybeTransition.isEmpty()) {
            return new BranchingTransitionOutcome(
                false,
                activeFraction.id(),
                activeNode.getId(),
                List.of(),
                "No transition defined for action " + action + " from node " + activeNode.getId() + ".",
                List.of(),
                List.of(),
                List.of()
            );
        }

        NodeTransition transition = maybeTransition.get();
        activeFraction.setCurrentNode(transition.getNextNode());

        String message = transition.getMessage().isBlank()
            ? "Transitioned to node " + transition.getNextNode().getId() + "."
            : transition.getMessage();
        return new BranchingTransitionOutcome(
            true,
            activeFraction.id(),
            activeFraction.currentNode().getId(),
            transition.getSignals(),
            message,
            List.of(),
            List.of(),
            List.of()
        );
    }

    public FractionSwitchOutcome switchActiveFraction(String fractionId) {
        String normalizedFractionId = requireText(fractionId, "fraction id");
        FractionState currentActive = activeFractionState();

        FractionState targetFraction = fractionsById.get(normalizedFractionId);
        if (targetFraction == null) {
            return new FractionSwitchOutcome(
                false,
                currentActive.id(),
                currentActive.currentNode().getId(),
                "Fraction " + normalizedFractionId + " does not exist."
            );
        }

        if (targetFraction.status() == FractionStatus.CLOSED) {
            return new FractionSwitchOutcome(
                false,
                currentActive.id(),
                currentActive.currentNode().getId(),
                "Fraction " + normalizedFractionId + " is closed and cannot be activated."
            );
        }

        if (normalizedFractionId.equals(activeFractionId)) {
            return new FractionSwitchOutcome(
                true,
                currentActive.id(),
                currentActive.currentNode().getId(),
                "Fraction " + normalizedFractionId + " is already active."
            );
        }

        if (currentActive.status() == FractionStatus.ACTIVE) {
            currentActive.setStatus(FractionStatus.PENDING);
        }

        targetFraction.setStatus(FractionStatus.ACTIVE);
        activeFractionId = targetFraction.id();

        return new FractionSwitchOutcome(
            true,
            targetFraction.id(),
            targetFraction.currentNode().getId(),
            "Switched active fraction to " + targetFraction.id() + "."
        );
    }

    public FractionSnapshot getActiveFraction() {
        return activeFractionState().snapshot();
    }

    public Node getCurrentNode() {
        return activeFractionState().currentNode();
    }

    public List<FractionSnapshot> listFractions() {
        return fractionsById.values().stream().map(FractionState::snapshot).toList();
    }

    private BranchingTransitionOutcome applySplitSelection(
        FractionState parentFraction,
        PhysicalSplitPolicy splitPolicy,
        ReactionAction selectedAction
    ) {
        Node splitNode = parentFraction.currentNode();
        Optional<NodeTransition> selectedTransition = splitNode.transitionFor(selectedAction);
        if (selectedTransition.isEmpty()) {
            return new BranchingTransitionOutcome(
                false,
                parentFraction.id(),
                splitNode.getId(),
                List.of(),
                "No transition defined for action " + selectedAction + " from node " + splitNode.getId() + ".",
                List.of(),
                List.of(),
                List.of()
            );
        }

        SplitMaterializationResult splitResult = materializePhysicalSplit(parentFraction, splitPolicy, selectedAction);
        String message = selectedTransition.get().getMessage().isBlank()
            ? "Transitioned to node " + splitResult.currentNodeId() + "."
            : selectedTransition.get().getMessage();

        return new BranchingTransitionOutcome(
            true,
            splitResult.activatedFractionId(),
            splitResult.currentNodeId(),
            splitResult.selectedSignals(),
            message + " " + splitResult.message(),
            splitResult.createdFractions(),
            List.of(splitResult.closedFractionId()),
            List.of(splitResult.activatedFractionId())
        );
    }

    private SplitMaterializationResult materializePhysicalSplit(
        FractionState parentFraction,
        PhysicalSplitPolicy splitPolicy,
        ReactionAction selectedAction
    ) {
        String currentNodeId = parentFraction.currentNode().getId();
        if (!currentNodeId.equals(splitPolicy.nodeId())) {
            throw new IllegalStateException(
                "split policy node " + splitPolicy.nodeId() + " does not match current node " + currentNodeId + "."
            );
        }

        PhysicalSplitBranchPolicy selectedBranchPolicy = splitPolicy.branches()
            .stream()
            .filter(branch -> branch.action() == selectedAction)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "selected split action " + selectedAction + " is not configured for node " + currentNodeId + "."
            ));

        List<FractionState> createdStates = new ArrayList<>();
        FractionState selectedFraction = null;
        List<ObservedSignal> selectedSignals = List.of();
        for (PhysicalSplitBranchPolicy branch : splitPolicy.branches()) {
            if (!branch.relevant()) {
                continue;
            }

            NodeTransition branchTransition = parentFraction
                .currentNode()
                .transitionFor(branch.action())
                .orElseThrow(() -> new IllegalStateException(
                    "split branch action " + branch.action() + " is not defined in node " + currentNodeId + "."
                ));

            FractionState childFraction = new FractionState(
                nextFractionId(),
                branchTransition.getNextNode(),
                branch.phase(),
                FractionStatus.PENDING,
                FractionOrigin.fromSplit(parentFraction.id(), currentNodeId, branch.label())
            );
            fractionsById.put(childFraction.id(), childFraction);
            createdStates.add(childFraction);

            if (branch.action() == selectedBranchPolicy.action()) {
                selectedFraction = childFraction;
                selectedSignals = branchTransition.getSignals();
            }
        }

        if (createdStates.isEmpty()) {
            throw new IllegalStateException("split policy for node " + currentNodeId + " has no relevant branches.");
        }
        if (selectedFraction == null) {
            throw new IllegalStateException(
                "selected split action " + selectedAction + " did not produce a relevant fraction in node " + currentNodeId + "."
            );
        }

        parentFraction.setStatus(FractionStatus.CLOSED);

        selectedFraction.setStatus(FractionStatus.ACTIVE);
        activeFractionId = selectedFraction.id();

        List<FractionSnapshot> createdSnapshots = createdStates.stream().map(FractionState::snapshot).toList();
        String splitMessage = "Physical split at node "
            + currentNodeId
            + ": created "
            + createdSnapshots.size()
            + " fraction(s), selected branch "
            + selectedBranchPolicy.label()
            + " -> active fraction "
            + selectedFraction.id()
            + ".";

        return new SplitMaterializationResult(
            parentFraction.id(),
            selectedFraction.id(),
            selectedFraction.currentNode().getId(),
            selectedSignals,
            createdSnapshots,
            splitMessage
        );
    }

    private boolean isSplitBranchAction(PhysicalSplitPolicy splitPolicy, ReactionAction action) {
        return splitPolicy.branches().stream().anyMatch(branch -> branch.action() == action);
    }

    private FractionState activeFractionState() {
        FractionState activeFraction = fractionsById.get(activeFractionId);
        if (activeFraction == null) {
            throw new IllegalStateException("active fraction is missing: " + activeFractionId + ".");
        }
        return activeFraction;
    }

    private Map<String, PhysicalSplitPolicy> indexSplitPolicies(Collection<PhysicalSplitPolicy> splitPolicies) {
        Objects.requireNonNull(splitPolicies, "split policies cannot be null");

        Map<String, PhysicalSplitPolicy> indexedPolicies = new LinkedHashMap<>();
        for (PhysicalSplitPolicy splitPolicy : splitPolicies) {
            Objects.requireNonNull(splitPolicy, "split policy cannot be null");
            if (indexedPolicies.containsKey(splitPolicy.nodeId())) {
                throw new IllegalArgumentException("duplicate split policy for node " + splitPolicy.nodeId() + ".");
            }
            indexedPolicies.put(splitPolicy.nodeId(), splitPolicy);
        }
        return Map.copyOf(indexedPolicies);
    }

    private String nextFractionId() {
        fractionSequence += 1;
        return "F" + fractionSequence;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return normalized;
    }

    private record SplitMaterializationResult(
        String closedFractionId,
        String activatedFractionId,
        String currentNodeId,
        List<ObservedSignal> selectedSignals,
        List<FractionSnapshot> createdFractions,
        String message
    ) {
    }

    private static final class FractionState {

        private final String id;
        private Node currentNode;
        private final FractionPhase phase;
        private FractionStatus status;
        private final FractionOrigin origin;

        FractionState(
            String id,
            Node currentNode,
            FractionPhase phase,
            FractionStatus status,
            FractionOrigin origin
        ) {
            this.id = requireText(id, "fraction id");
            this.currentNode = Objects.requireNonNull(currentNode, "current node cannot be null");
            this.phase = Objects.requireNonNull(phase, "fraction phase cannot be null");
            this.status = Objects.requireNonNull(status, "fraction status cannot be null");
            this.origin = Objects.requireNonNull(origin, "fraction origin cannot be null");
        }

        String id() {
            return id;
        }

        Node currentNode() {
            return currentNode;
        }

        FractionStatus status() {
            return status;
        }

        void setCurrentNode(Node currentNode) {
            this.currentNode = Objects.requireNonNull(currentNode, "current node cannot be null");
        }

        void setStatus(FractionStatus status) {
            this.status = Objects.requireNonNull(status, "fraction status cannot be null");
        }

        FractionSnapshot snapshot() {
            return new FractionSnapshot(
                id,
                currentNode.getId(),
                currentNode.getLabel(),
                currentNode.getExpectedSpecies(),
                phase,
                status,
                origin
            );
        }
    }
}
