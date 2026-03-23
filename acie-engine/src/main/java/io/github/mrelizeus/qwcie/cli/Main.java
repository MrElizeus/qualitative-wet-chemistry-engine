package io.github.mrelizeus.qwcie.cli;

import io.github.mrelizeus.qwcie.domain.Observation;
import io.github.mrelizeus.qwcie.engine.AnalysisEngine;
import io.github.mrelizeus.qwcie.engine.Node;

public class Main {

    public static void main(String[] args) {

        // Crear nodos (simulación simple)
        Node start = new Node("Inicio");
        Node group1 = new Node("Grupo I precipitado");
        Node noPrecip = new Node("No precipita");

        // Definir transiciones
        start.addTransition(Observation.WHITE_PRECIPITATE, group1);
        start.addTransition(Observation.NO_CHANGE, noPrecip);

        // Crear motor
        AnalysisEngine engine = new AnalysisEngine(start);

        System.out.println("Estado inicial: " + engine.getCurrentNode().getName());

        // Simular usuario
        Observation observation = Observation.WHITE_PRECIPITATE;
        boolean transitioned = engine.applyObservation(observation);

        if (transitioned) {
            System.out.println("Ahora estás en: " + engine.getCurrentNode().getName());
        } else {
            System.out.println("No hay transición para: " + observation);
        }
    }
}
