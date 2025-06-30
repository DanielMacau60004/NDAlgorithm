package com.logic.exps.asts.others;


import com.logic.exps.asts.AASTExp;
import com.logic.exps.asts.IExpsVisitor;
import com.logic.parser.Parser;

public class ASTBottom extends AASTExp {

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

    @Override
    public String toString() {
        return getToken(Parser.BOTTOM);
    }
}
