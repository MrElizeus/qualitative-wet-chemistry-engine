package io.github.mrelizeus.qwcie.engine;

import io.github.mrelizeus.qwcie.domain.Observation;
import java.util.HashMap;
import java.util.Map;

public class Node {

    private String name;
    private Map<Observation, Node> transitions = new HashMap<>();

    public Node(String name) {
        this.name = name;
    }

    public void addTransition(Observation obs, Node next) {
        transitions.put(obs, next);
    }

    public Node next(Observation obs) {
        return transitions.get(obs);
    }

    public String getName() {
        return name;
    }
}