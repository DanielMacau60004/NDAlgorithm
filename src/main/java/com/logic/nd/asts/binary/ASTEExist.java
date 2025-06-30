package com.logic.nd.asts.binary;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTEExist extends AASTBinaryND {

    private final String m;
    private ASTVariable mapping;

    private IASTExp closeM;

    public ASTEExist(IASTND hypothesis1, IASTND hypothesis2, IASTExp conclusion, String m) {
        super(ERule.ELIM_EXISTENTIAL,hypothesis1, hypothesis2, conclusion);
        this.m = m;
    }

    public String getM() {
        return m;
    }

    public void setMapping(ASTVariable mapping) {
        this.mapping = mapping;
    }

    public ASTVariable getMapping() {
        return mapping;
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
