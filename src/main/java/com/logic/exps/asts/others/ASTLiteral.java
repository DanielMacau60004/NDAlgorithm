package com.logic.exps.asts.others;


import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;

public class ASTLiteral extends ASTPred implements IASTExp {

    public ASTLiteral(String name) {
        super(name);
    }

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

    @Override
    public String toString() {
        return predicate;
    }

}
