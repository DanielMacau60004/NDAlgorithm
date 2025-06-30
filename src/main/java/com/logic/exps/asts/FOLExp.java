package com.logic.exps.asts;

import com.logic.api.IFOLFormula;
import com.logic.api.IFormula;
import com.logic.exps.asts.others.*;
import com.logic.others.Utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FOLExp implements IFOLFormula {

    private final IASTExp exp;

    private final Set<ASTFun> functions;
    private final Set<ASTPred> predicates;

    private final Set<ASTVariable> boundedVariables;
    private final Set<ASTVariable> unboundedVariables;

    private final Set<ASTArbitrary> generics;

    public FOLExp(IASTExp exp,
                  Set<ASTFun> functions, Set<ASTPred> predicates,
                  Set<ASTVariable> boundedVariables, Set<ASTVariable> unboundedVariables
            , Set<ASTArbitrary> generics) {
        this.exp = exp;
        this.functions = functions;
        this.predicates = predicates;
        this.boundedVariables = boundedVariables;
        this.unboundedVariables = unboundedVariables;

        this.generics = generics;

    }

    @Override
    public IASTExp getAST() {
        return exp;
    }

    @Override
    public Iterator<ASTArbitrary> iterateGenerics() {
        return generics.iterator();
    }

    @Override
    public boolean hasGenerics() {
        return !generics.isEmpty();
    }

    @Override
    public Iterator<ASTFun> iterateFunctions() {
        return functions.iterator();
    }

    @Override
    public Iterator<ASTPred> iteratePredicates() {
        return predicates.iterator();
    }

    @Override
    public Iterator<ASTVariable> iterateBoundedVariables() {
        return boundedVariables.iterator();
    }

    @Override
    public Iterator<AASTTerm> iterateTerms() {
        Set<AASTTerm> terms = new HashSet<>();
        terms.addAll(functions);
        terms.addAll(boundedVariables);
        terms.addAll(unboundedVariables);

        return terms.iterator();
    }

    @Override
    public Iterator<ASTVariable> iterateVariables() {
        Set<ASTVariable> variables = new HashSet<>();
        variables.addAll(boundedVariables);
        variables.addAll(unboundedVariables);

        return variables.iterator();
    }

    @Override
    public boolean isABoundedVariable(ASTVariable variable) {
        return boundedVariables.contains(variable);
    }

    @Override
    public boolean isAnUnboundedVariable(ASTVariable variable) {
        return unboundedVariables.contains(variable);
    }

    @Override
    public boolean isAVariable(ASTVariable variable) {
        return boundedVariables.contains(variable) || unboundedVariables.contains(variable);
    }

    @Override
    public boolean appearsFreeVariable(ASTVariable variable) {
        if (hasGenerics())
            return isAnUnboundedVariable(variable) || !isAVariable(variable);
        return isAnUnboundedVariable(variable);
    }

    @Override
    public boolean isFreeVariable(ASTVariable variable) {
        return !isABoundedVariable(variable);
    }

    @Override
    public Iterator<ASTVariable> iterateUnboundedVariables() {
        return unboundedVariables.iterator();
    }

    @Override
    public boolean isASentence() {
        return unboundedVariables.isEmpty();
    }

    @Override
    public String toString() {
        return Utils.getToken(exp.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IFormula formula)) return false;
        return exp.equals(formula.getAST());
    }

    @Override
    public int hashCode() {
        return exp.hashCode();
    }
}
