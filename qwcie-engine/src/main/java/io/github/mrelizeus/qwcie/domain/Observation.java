package io.github.mrelizeus.qwcie.domain;

/**
 * Legacy observation model kept only for backward compatibility with the initial graph API.
 * New workflows should use {@link ReactionAction} and {@link ObservedSignal}.
 */
@Deprecated
public enum Observation {
    WHITE_PRECIPITATE,
    YELLOW_PRECIPITATE,
    BLACK_PRECIPITATE,
    NO_CHANGE
}
