package com.logic.nd.asts.unary;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTINeg extends AASTUnaryND {

    private final String m;
    private IASTExp closeM;

    public ASTINeg(IASTND hypothesis, IASTExp conclusion, String m) {
        super(ERule.INTRO_NEGATION, hypothesis, conclusion);
        this.m = m;
    }

    public void setCloseM(IASTExp closeM) {
        this.closeM = closeM;
    }

    public IASTExp getCloseM() {
        return closeM;
    }

    public String getM() {
        return m;
    }

    @Override
    public <T, E> T accept(INDVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}
