package com.logic.nd.algorithm.transition;

import com.logic.api.IFormula;
import com.logic.exps.asts.IASTExp;

import java.util.Map;
import java.util.Set;

/*
    TIME COMPLEXITY ANALYSIS:

    Definitions:
        m: number of expressions (AST nodes) in the graph
        d: number of disjunctions in m
        e: number of existential quantifiers in m
        u: number of universal quantifiers in m
        v: number of variables

    Propositional Logic (TransitionGraphPL):
        BEST CASE:     O(m)
        AVERAGE CASE:  O(m^d)
        WORST CASE:    O(m^m)

    First-Order Logic (TransitionGraphFOL):
        BEST CASE:     O(m * (u + e) * v)
        AVERAGE CASE:  O((m * (u + e) * v)^(d + e))
        WORST CASE:    O((m * (u + e) * v)^m)

 */

public interface ITransitionGraph {

    void build();

    Set<TransitionEdge> getEdges(IASTExp exp);

    Set<IFormula> getFormulas();
}
