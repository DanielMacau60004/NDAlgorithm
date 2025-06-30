package com.logic.exps.asts.others;

import com.logic.exps.asts.AASTExp;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTPred extends AASTExp {

    protected final String predicate;
    private final List<IASTExp> terms;

    public ASTPred(String predicate) {
        this(predicate, new LinkedList<>());
    }

    public ASTPred(String predicate, List<IASTExp> terms) {
        this.predicate = predicate;
        this.terms = terms;
    }

    public String getName() {
        return predicate;
    }

    public List<IASTExp> getTerms() {
        return terms;
    }

    public void addTerm(IASTExp term) {
        terms.add(term);
    }

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

    @Override
    public String toString() {
        return predicate + (terms.isEmpty() ? "" :
                "(" + terms.stream().map(Object::toString).collect(Collectors.joining(",")) + ")");
    }
}

