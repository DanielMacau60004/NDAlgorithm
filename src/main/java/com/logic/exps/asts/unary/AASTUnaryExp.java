package com.logic.exps.asts.unary;

import com.logic.exps.asts.AASTExp;
import com.logic.exps.asts.IASTExp;

public abstract class AASTUnaryExp extends AASTExp implements IASTExp {

    protected final IASTExp exp;

    public AASTUnaryExp(IASTExp exp) {
        this.exp = exp;
    }

    public IASTExp getExp() {
        return exp;
    }

}
