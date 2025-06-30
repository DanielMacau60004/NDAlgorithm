package com.logic.exps.asts.others;


import com.logic.exps.asts.IExpsVisitor;

public class ASTVariable extends AASTTerm {

    public ASTVariable(String value) {
        super(value);
    }

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

}