package com.logic.exps.asts.others;


import com.logic.exps.asts.AASTExp;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;

public class ASTArbitrary extends AASTExp implements IASTExp {

    private final String id;

    public ASTArbitrary(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

    @Override
    public String toString() {
        return id;
    }

}
