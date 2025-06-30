package com.logic.nd.algorithm.transition;

import com.logic.api.IFormula;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.nd.ERule;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TransitionEdge {

    private final ERule rule;
    private final List<TransitionNode> transitions;
    private boolean produces;

    TransitionEdge(ERule rule, IFormula to, IFormula produces) {
        this(rule);
        addTransition(to, produces);
    }

    TransitionEdge(ERule rule, IFormula to) {
        this(rule);
        addTransition(to, null);
    }

    TransitionEdge(ERule rule) {
        this.rule = rule;
        this.transitions = new LinkedList<>();
        this.produces = false;
    }

    public boolean hasProduces() {
        return produces;
    }

    public TransitionEdge addTransition(IFormula to, IFormula produces, ASTVariable free) {
        transitions.add(new TransitionNode(to, produces, free));
        this.produces = produces != null;
        return this;
    }

    public TransitionEdge addTransition(IFormula to, IFormula produces) {
        transitions.add(new TransitionNode(to, produces, null));
        this.produces = produces != null;
        return this;
    }

    public TransitionEdge addTransition(IFormula to) {
        transitions.add(new TransitionNode(to, null, null));
        return this;
    }

    public List<TransitionNode> getTransitions() {
        return transitions;
    }

    public ERule getRule() {
        return rule;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(rule.name() + " ");
        for (TransitionNode transition : transitions)
            str.append("[").append(transition.getTo()).append(",")
                    .append(transition.getProduces()).append(",").append(transition.getFree()).append("] ");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitionEdge that = (TransitionEdge) o;
        return rule == that.rule && Objects.equals(transitions, that.transitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, transitions);
    }

}
