package com.logic.exps.asts.binary;


import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;
import com.logic.parser.Parser;

public class ASTConditional extends AASTBinaryExp {

    public ASTConditional(IASTExp e1, IASTExp e2) {
        super(e1, e2);
    }

    @Override
    public <T, E> T accept(IExpsVisitor<T, E> v, E env) {
        return v.visit(this, env);
    }

    @Override
    public String toString() {
        return left.toString() + " " + getToken(Parser.CONDITIONAL) + " " + right.toString();
    }
}
