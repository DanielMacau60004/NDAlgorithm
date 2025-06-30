package com.logic.nd.asts.others;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.AASTND;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTHypothesis extends AASTND implements IASTND {

    private final IASTExp hyp;
    private final String m;

    public ASTHypothesis(IASTExp hyp, String m) {
        super(ERule.HYPOTHESIS);
        this.hyp = hyp;
        this.m = m;
    }

    public String getM() {
        return m;
    }

    public IASTExp getConclusion() {
        return hyp;
    }

    @Override
    public <T, E> T accept(INDVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}
