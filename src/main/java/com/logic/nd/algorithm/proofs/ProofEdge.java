package com.logic.nd.algorithm.proofs;

import com.logic.api.IFormula;
import com.logic.nd.ERule;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ProofEdge {

    private final ERule rule;
    private final List<ProofTransitionEdge> transitions;

    public ProofEdge(ERule rule, GoalNode transition, IFormula produces) {
        this.rule = rule;
        this.transitions = new LinkedList<>();

        transitions.add(new ProofTransitionEdge(transition, produces));
    }

    public ProofEdge(ERule rule) {
        this.rule = rule;
        this.transitions = new LinkedList<>();
    }

    public void addTransition(GoalNode to, IFormula produces) {
        transitions.add(new ProofTransitionEdge(to, produces));
    }

    public int height() {
        return transitions.stream().mapToInt(i -> i.getNode().getHeight()).sum();
    }

    public List<ProofTransitionEdge> getTransitions() {
        return transitions;
    }

    public boolean isClosed() {
        return transitions.stream().allMatch(s -> s.getNode().isClosed());
    }

    public ERule getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return transitions.toString() + " closed: " + isClosed() + " rule: " + rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofEdge edge = (ProofEdge) o;
        return rule == edge.rule && Objects.equals(transitions, edge.transitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, transitions);
    }


}
