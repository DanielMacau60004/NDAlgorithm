package com.logic.exps.asts.binary;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;
import com.logic.parser.Parser;

public class ASTUniversal extends AASTBinaryExp {

    public ASTUniversal(IASTExp variable, IASTExp exp) {
        super(variable, exp);
    }

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

    @Override
    public String toString() {
        return getToken(Parser.UNIVERSAL) + left.toString() + " " + right.toString();
    }

}
