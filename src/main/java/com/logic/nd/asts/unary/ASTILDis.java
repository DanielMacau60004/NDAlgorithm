package com.logic.nd.asts.unary;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTILDis extends AASTUnaryND {

    public ASTILDis(IASTND hypothesis, IASTExp conclusion) {
        super(ERule.INTRO_DISJUNCTION_LEFT, hypothesis, conclusion);
    }

    @Override
    public <T, E> T accept(INDVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}
