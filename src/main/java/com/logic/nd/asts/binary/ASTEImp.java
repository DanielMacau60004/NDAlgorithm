package com.logic.nd.asts.binary;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.interpreters.NDInterpretString;

public class ASTEImp extends AASTBinaryND {

    public ASTEImp(IASTND hypothesis1, IASTND hypothesis2, IASTExp conclusion) {
        super(ERule.ELIM_IMPLICATION, hypothesis1, hypothesis2, conclusion);
    }

    @Override
    public <T, E> T accept(INDVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}
