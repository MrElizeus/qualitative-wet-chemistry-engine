package io.github.mrelizeus.qwcie.engine;

import io.github.mrelizeus.qwcie.domain.Observation;
import io.github.mrelizeus.qwcie.domain.ObservedSignal;
import io.github.mrelizeus.qwcie.domain.ReactionAction;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Node {

    private final String id;
    private final String label;
    private final Set<String> expectedSpecies;

    private final Map<ReactionAction, NodeTransition> actionTransitions = new EnumMap<>(ReactionAction.class);
    private final Map<Observation, Node> observationTransitions = new EnumMap<>(Observation.class);

    public Node(String id, String label) {
        this(id, label, Set.of());
    }

    public Node(String id, String label, Collection<String> expectedSpecies) {
        this.id = validateIdentifier(id, "node id");
        this.label = validateIdentifier(label, "node label");
        this.expectedSpecies = Set.copyOf(Objects.requireNonNull(expectedSpecies, "expected species cannot be null"));
    }

    /**
     * Legacy constructor preserved for backward compatibility with the previous simple graph API.
     */
    @Deprecated
    public Node(String label) {
        this(toLegacyId(label), label, Set.of());
    }

    public void addTransition(ReactionAction action, Node nextNode) {
        addTransition(action, nextNode, Set.of(), "");
    }

    public void addTransition(ReactionAction action, Node nextNode, Collection<ObservedSignal> signals, String message) {
        Objects.requireNonNull(action, "reaction action cannot be null");
        Objects.requireNonNull(nextNode, "next node cannot be null");
        Objects.requireNonNull(signals, "signals cannot be null");

        if (actionTransitions.containsKey(action)) {
            throw new IllegalArgumentException(
                "Transition already defined for action " + action + " in node " + id + "."
            );
        }

        actionTransitions.put(action, new NodeTransition(nextNode, List.copyOf(signals), message));
    }

    public Optional<NodeTransition> transitionFor(ReactionAction action) {
        Objects.requireNonNull(action, "reaction action cannot be null");
        return Optional.ofNullable(actionTransitions.get(action));
    }

    /**
     * Legacy API preserved for backward compatibility with the previous simple graph API.
     */
    @Deprecated
    public void addTransition(Observation observation, Node next) {
        Objects.requireNonNull(observation, "observation cannot be null");
        Objects.requireNonNull(next, "next node cannot be null");

        if (observationTransitions.containsKey(observation)) {
            throw new IllegalArgumentException(
                "Legacy transition already defined for observation " + observation + " in node " + id + "."
            );
        }

        observationTransitions.put(observation, next);
    }

    /**
     * Legacy API preserved for backward compatibility with the previous simple graph API.
     */
    @Deprecated
    public Node next(Observation observation) {
        Objects.requireNonNull(observation, "observation cannot be null");
        return observationTransitions.get(observation);
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Set<String> getExpectedSpecies() {
        return expectedSpecies;
    }

    public Set<ReactionAction> getAvailableActions() {
        return Set.copyOf(actionTransitions.keySet());
    }

    private static String validateIdentifier(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return trimmed;
    }

    private static String toLegacyId(String label) {
        String normalized = validateIdentifier(label, "node label");
        return normalized
            .toUpperCase(Locale.ROOT)
            .replaceAll("[^A-Z0-9]+", "_")
            .replaceAll("^_+|_+$", "");
    }
}
