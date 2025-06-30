package com.logic.nd.asts.binary;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTENeg extends AASTBinaryND {

    public ASTENeg(IASTND hypothesis1, IASTND hypothesis2, IASTExp conclusion) {
        super(ERule.ELIM_NEGATION, hypothesis1, hypothesis2, conclusion);
    }

    @Override
    public <T, E> T accept(INDVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}
