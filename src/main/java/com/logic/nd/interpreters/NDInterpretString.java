package com.logic.nd.interpreters;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.binary.ASTEImp;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NDInterpretString implements INDVisitor<String, Integer> {

    NDInterpretString() {}

    public static String interpret(IASTND proof) {
        return proof.accept(new NDInterpretString(), 1);
    }

    private String printRule(ERule rule, List<String> marks, IASTExp conclusion, List<IASTND> hypotheses
            , Integer depth) {
        String marksStr = marks == null ? "" : marks.stream().map(m -> m != null ? "," + m : ",")
                .collect(Collectors.joining());
        String hypStr = hypotheses == null ? "" : hypotheses.stream().map(h ->
                        "\n" + "\t".repeat(depth) + h.accept(this, depth+1))
                .collect(Collectors.joining());

        return "[" + rule + marksStr + "] [" + conclusion + "." + hypStr + "]";
    }

    @Override
    public String visit(ASTHypothesis h, Integer depth) {
        return printRule(ERule.HYPOTHESIS, Collections.singletonList(h.getM()),
                h.getConclusion(), null, depth);
    }

    @Override
    public String visit(ASTIImp r, Integer depth) {
        return printRule(ERule.INTRO_IMPLICATION, Collections.singletonList(r.getM()),
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTINeg r, Integer depth) {
        return printRule(ERule.INTRO_NEGATION, Collections.singletonList(r.getM()),
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTERConj r, Integer depth) {
        return printRule(ERule.ELIM_CONJUNCTION_RIGHT, null,
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTELConj r, Integer depth) {
        return printRule(ERule.ELIM_CONJUNCTION_LEFT, null,
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTIRDis r, Integer depth) {
        return printRule(ERule.INTRO_DISJUNCTION_RIGHT, null,
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTILDis r, Integer depth) {
        return printRule(ERule.INTRO_DISJUNCTION_LEFT, null,
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTAbsurdity r, Integer depth) {
        return printRule(ERule.ABSURDITY, Collections.singletonList(r.getM()),
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTIConj r, Integer depth) {
        return printRule(ERule.INTRO_CONJUNCTION, null,
                r.getConclusion(), Arrays.asList(r.getHyp1(), r.getHyp2()), depth);
    }

    @Override
    public String visit(ASTEDis r, Integer depth) {
        return printRule(ERule.ELIM_DISJUNCTION, Arrays.asList(r.getM(), r.getN()),
                r.getConclusion(), Arrays.asList(r.getHyp1(), r.getHyp2(), r.getHyp3()), depth);
    }

    @Override
    public String visit(ASTEImp r, Integer depth) {
        return printRule(ERule.ELIM_IMPLICATION, null,
                r.getConclusion(), Arrays.asList(r.getHyp1(), r.getHyp2()), depth);
    }

    @Override
    public String visit(ASTENeg r, Integer depth) {
        return printRule(ERule.ELIM_NEGATION, null,
                r.getConclusion(), Arrays.asList(r.getHyp1(), r.getHyp2()), depth);
    }

    @Override
    public String visit(ASTEUni r, Integer depth) {
        return printRule(ERule.ELIM_UNIVERSAL, null,
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTIExist r, Integer depth) {
        return printRule(ERule.INTRO_EXISTENTIAL, null,
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTIUni r, Integer depth) {
        return printRule(ERule.INTRO_UNIVERSAL, null,
                r.getConclusion(), Collections.singletonList(r.getHyp()), depth);
    }

    @Override
    public String visit(ASTEExist r, Integer depth) {
        return printRule(ERule.ELIM_EXISTENTIAL, Collections.singletonList(r.getM()),
                r.getConclusion(), Arrays.asList(r.getHyp1(), r.getHyp2()), depth);
    }
}
