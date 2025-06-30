package com.logic.nd.asts.unary;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTIImp extends AASTUnaryND {

    private final String m;
    private IASTExp closeM;

    public ASTIImp(IASTND hypothesis, IASTExp conclusion, String m) {
        super(ERule.INTRO_IMPLICATION, hypothesis, conclusion);
        this.m = m;
    }

    public String getM() {
        return m;
    }

    public void setCloseM(IASTExp closeM) {
        this.closeM = closeM;
    }

    public IASTExp getCloseM() {
        return closeM;
    }

    @Override
    public <T, E> T accept(INDVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}
