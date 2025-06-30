package com.logic.nd.algorithm.transition;

import com.logic.api.IFormula;
import com.logic.exps.asts.others.ASTVariable;

import java.util.Objects;

public class TransitionNode {

    private final IFormula to;
    private final IFormula produces;
    private final ASTVariable free;

    TransitionNode(IFormula to, IFormula produces, ASTVariable free) {
        this.to = to;
        this.produces = produces;
        this.free = free;
    }

    public IFormula getTo() {
        return to;
    }

    public IFormula getProduces() {
        return produces;
    }

    public ASTVariable getFree() {
        return free;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitionNode that = (TransitionNode) o;
        return Objects.equals(to, that.to) && Objects.equals(produces, that.produces) && Objects.equals(free, that.free);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, produces, free);
    }
}
