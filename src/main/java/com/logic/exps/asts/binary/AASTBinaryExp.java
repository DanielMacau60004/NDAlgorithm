package com.logic.exps.asts.binary;

import com.logic.exps.asts.AASTExp;
import com.logic.exps.asts.IASTExp;

public abstract class AASTBinaryExp extends AASTExp implements IASTExp {

    protected final IASTExp left;
    protected final IASTExp right;

    public AASTBinaryExp(IASTExp left, IASTExp right) {
        this.left = left;
        this.right = right;
    }

    public IASTExp getLeft() {
        return left;
    }

    public IASTExp getRight() {
        return right;
    }
}

