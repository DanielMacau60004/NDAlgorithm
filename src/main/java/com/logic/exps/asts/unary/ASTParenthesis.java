package com.logic.exps.asts.unary;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;
import com.logic.parser.Parser;

public class ASTParenthesis extends AASTUnaryExp {

    private final IASTExp e;

    public ASTParenthesis(IASTExp e) {
        super(e);
        this.e = e;
    }

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

    @Override
    public String toString() {
        return getToken(Parser.LPAR) + e.toString() + getToken(Parser.RPAR);
    }
}
