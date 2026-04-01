package io.github.mrelizeus.qwcie.engine.core;

import java.util.Objects;

public record FractionOrigin(
    String parentFractionId,
    String splitNodeId,
    String branchLabel
) {

    public FractionOrigin {
        parentFractionId = requireText(parentFractionId, "parent fraction id");
        splitNodeId = requireText(splitNodeId, "split node id");
        branchLabel = requireText(branchLabel, "branch label");
    }

    public static FractionOrigin root() {
        return new FractionOrigin("ROOT", "ROOT", "Initial mixture");
    }

    public static FractionOrigin fromSplit(String parentFractionId, String splitNodeId, String branchLabel) {
        return new FractionOrigin(parentFractionId, splitNodeId, branchLabel);
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return normalized;
    }
}
