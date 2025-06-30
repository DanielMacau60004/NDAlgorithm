package com.logic.nd.asts.others;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.AASTND;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTEDis extends AASTND implements IASTND {

    private final IASTND hyp1;
    private final IASTND hyp2;
    private final IASTND hyp3;
    private final IASTExp conclusion;

    private final String m, n;

    private IASTExp closeM;
    private IASTExp closeN;

    public ASTEDis(IASTND hyp1, IASTND hyp2, IASTND hyp3, IASTExp conclusion, String m, String n) {
        super(ERule.ELIM_DISJUNCTION);
        this.hyp1 = hyp1;
        this.hyp2 = hyp2;
        this.hyp3 = hyp3;
        this.conclusion = conclusion;
        this.m = m;
        this.n = n;
    }

    public void setCloseM(IASTExp closeM) {
        this.closeM = closeM;
    }

    public void setCloseN(IASTExp closeN) {
        this.closeN = closeN;
    }

    public IASTExp getCloseM() {
        return closeM;
    }

    public IASTExp getCloseN() {
        return closeN;
    }

    public String getM() {
        return m;
    }

    public String getN() {
        return n;
    }

    public IASTND getHyp3() {
        return hyp3;
    }

    public IASTND getHyp1() {
        return hyp1;
    }

    public IASTND getHyp2() {
        return hyp2;
    }

    public IASTExp getConclusion() {
        return conclusion;
    }

    @Override
    public <T, E> T accept(INDVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}
