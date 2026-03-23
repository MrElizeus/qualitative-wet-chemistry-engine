package io.github.mrelizeus.qwcie;

import io.github.mrelizeus.qwcie.domain.Observation;
import io.github.mrelizeus.qwcie.engine.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    void appliesTransitionWhenObservationExists() {
        Node start = new Node("Inicio");
        Node group1 = new Node("Grupo I precipitado");
        start.addTransition(Observation.WHITE_PRECIPITATE, group1);

        AnalysisEngine engine = new AnalysisEngine(start);

        boolean transitioned = engine.applyObservation(Observation.WHITE_PRECIPITATE);

        assertTrue(transitioned);
        assertSame(group1, engine.getCurrentNode());
    }

    @Test
    void keepsCurrentNodeWhenTransitionDoesNotExist() {
        Node start = new Node("Inicio");
        AnalysisEngine engine = new AnalysisEngine(start);

        boolean transitioned = engine.applyObservation(Observation.BLACK_PRECIPITATE);

        assertFalse(transitioned);
        assertSame(start, engine.getCurrentNode());
    }
}
