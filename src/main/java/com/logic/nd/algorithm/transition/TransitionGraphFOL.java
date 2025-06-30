package com.logic.nd.algorithm.transition;

import com.logic.api.IFOLFormula;
import com.logic.api.IFormula;
import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.ASTExistential;
import com.logic.exps.asts.binary.ASTUniversal;
import com.logic.exps.asts.others.AASTTerm;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.exps.interpreters.FOLWFFInterpreter;
import com.logic.exps.interpreters.FOLReplaceExps;
import com.logic.nd.ERule;
import com.logic.others.Utils;

import java.util.*;

public class TransitionGraphFOL extends TransitionGraphPL implements ITransitionGraph {

    private final Set<ASTExistential> existentials;
    private final Set<AASTTerm> terms;

    public TransitionGraphFOL(Set<IFormula> expressions, Set<ERule> forbiddenRules,
                              Set<AASTTerm> terms) {
        super(expressions, forbiddenRules);
        this.terms = terms;

        existentials = new HashSet<>();
    }

    @Override
    public void build() {
        super.build();

        if (!forbiddenRules.contains(ERule.ELIM_EXISTENTIAL))
            graph.forEach((e, ts) -> existentials.forEach(exi -> ts.addAll(existentialERule(e, exi))));
    }

    @Override
    protected IFOLFormula getFormula(IASTExp exp) {
        formulas.putIfAbsent(exp, FOLWFFInterpreter.check(exp));
        return (IFOLFormula) formulas.get(exp);
    }

    protected void addNode(IASTExp node, boolean canGen) {
        super.addNode(node, canGen);

        if (node instanceof ASTExistential exi)
            existentials.add(exi);
    }

    private void existentialIRule(ASTExistential exi) {
        IASTExp psi = ExpUtils.removeParenthesis(exi.getRight());
        ASTVariable xVar = (ASTVariable) exi.getLeft();

        for (AASTTerm term : terms) {
            IASTExp psiXT = FOLReplaceExps.replace(psi, xVar, term);

            if ((term instanceof ASTVariable t && !getFormula(psiXT).isFreeVariable(t)))
                continue;

            addEdge(exi, new TransitionEdge(ERule.INTRO_EXISTENTIAL)
                    .addTransition(getFormula(psiXT)), true);
        }
    }

    private void universalERule(ASTUniversal uni) {
        IASTExp psi = ExpUtils.removeParenthesis(uni.getRight());
        ASTVariable xVar = (ASTVariable) uni.getLeft();

        for (AASTTerm term : terms) {
            IASTExp psiXT = FOLReplaceExps.replace(psi, xVar, term);

            if ((term instanceof ASTVariable t && !getFormula(psi).isFreeVariable(t)))
                continue;

            addEdge(psiXT, new TransitionEdge(ERule.ELIM_UNIVERSAL)
                    .addTransition(getFormula(uni)), true);
        }
    }

    private void universalIRule(ASTUniversal uni) {
        IASTExp psi = ExpUtils.removeParenthesis(uni.getRight());
        ASTVariable xVar = (ASTVariable) uni.getLeft();

        for (AASTTerm t : terms) {
            if (!(t instanceof ASTVariable yVar))
                continue;

            IASTExp psiXY = FOLReplaceExps.replace(psi, xVar, yVar);
            if ((!uni.getLeft().equals(yVar) && getFormula(psi).appearsFreeVariable(yVar)) ||
                    !FOLReplaceExps.replace(psiXY, yVar, xVar).equals(psi))
                continue;

            addEdge(uni, new TransitionEdge(ERule.INTRO_UNIVERSAL)
                    .addTransition(getFormula(psiXY), null, yVar), true);
        }
    }

    private List<TransitionEdge> existentialERule(IASTExp exp, ASTExistential exi) {
        IASTExp psi = ExpUtils.removeParenthesis(exi.getRight());
        ASTVariable xVar = (ASTVariable) exi.getLeft();

        List<TransitionEdge> edges = new ArrayList<>();
        for (AASTTerm t : terms) {
            if (!(t instanceof ASTVariable yVar))
                continue;

            IASTExp psiXY = FOLReplaceExps.replace(psi, xVar, yVar);
            if ((getFormula(exp).appearsFreeVariable(yVar))
                    || (!exi.getLeft().equals(yVar) && (getFormula(psi).appearsFreeVariable(yVar))) ||
                    !FOLReplaceExps.replace(psiXY, t, xVar).equals(psi))
                continue;

            edges.add(new TransitionEdge(ERule.ELIM_EXISTENTIAL)
                    .addTransition(getFormula(exi))
                    .addTransition(getFormula(exp), getFormula(psiXY), psi.equals(psiXY) ? xVar : yVar));
        }

        return edges;
    }

    protected void genTopDown(IASTExp exp) {
        super.genTopDown(exp);
        exp = ExpUtils.removeParenthesis(exp);

        if (exp instanceof ASTUniversal uni) {
            universalERule(uni);
            universalIRule(uni);
        } else if (exp instanceof ASTExistential exi)
            existentialIRule(exi);

    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Formulas: ").append(Utils.getToken(formulas.values().toString())).append("\n");
        str.append("Total nodes: ").append(graph.size()).append("\n");
        str.append("Total edges: ").append(graph.values().stream().mapToInt(Set::size).sum()).append("\n");
        str.append("Disjunctions: ").append(disjunctions).append("\n");
        str.append("Existentials: ").append(existentials).append("\n");
        for (Map.Entry<IASTExp, Set<TransitionEdge>> entry : graph.entrySet()) {
            str.append(entry.getKey()).append(":  \n");
            for (TransitionEdge transition : entry.getValue())
                str.append("\t").append(transition).append("\n");
        }
        return str.toString();
    }
}
