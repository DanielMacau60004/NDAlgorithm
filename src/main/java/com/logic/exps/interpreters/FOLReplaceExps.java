package com.logic.exps.interpreters;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;
import com.logic.exps.asts.binary.*;
import com.logic.exps.asts.others.*;
import com.logic.exps.asts.unary.ASTNot;
import com.logic.exps.asts.unary.ASTParenthesis;

import java.util.Map;

public class FOLReplaceExps implements IExpsVisitor<IASTExp, Void> {

    private final Map<IASTExp, IASTExp> replacers;

    FOLReplaceExps(Map<IASTExp, IASTExp> replacers) {
        this.replacers = replacers;
    }

    public static IASTExp replace(IASTExp exp, IASTExp oldExp, IASTExp newExp) {
        return exp.accept(new FOLReplaceExps(Map.of(oldExp, newExp)), null);
    }

    public static IASTExp replace(IASTExp exp, Map<IASTExp, IASTExp> replacers) {
        return exp.accept(new FOLReplaceExps(replacers), null);
    }

    @Override
    public IASTExp visit(ASTTop e, Void env) {
        return replacers.getOrDefault(e, e);
    }

    @Override
    public IASTExp visit(ASTBottom e, Void env) {
        return replacers.getOrDefault(e, e);
    }

    @Override
    public IASTExp visit(ASTConstant e, Void env) {
        return replacers.getOrDefault(e, e);
    }

    @Override
    public IASTExp visit(ASTLiteral e, Void env) {
        return replacers.getOrDefault(e, e);
    }

    @Override
    public IASTExp visit(ASTArbitrary e, Void env) {
        return replacers.getOrDefault(e, e);
    }

    @Override
    public IASTExp visit(ASTVariable e, Void env) {
        return replacers.getOrDefault(e, e);
    }

    @Override
    public IASTExp visit(ASTNot e, Void env) {
        return replacers.getOrDefault(e, new ASTNot(e.getExp().accept(this, env)));
    }

    @Override
    public IASTExp visit(ASTAnd e, Void env) {
        return replacers.getOrDefault(e, new ASTAnd(e.getLeft().accept(this, env), e.getRight().accept(this, env)));
    }

    @Override
    public IASTExp visit(ASTOr e, Void env) {
        return replacers.getOrDefault(e, new ASTOr(e.getLeft().accept(this, env), e.getRight().accept(this, env)));
    }

    @Override
    public IASTExp visit(ASTConditional e, Void env) {
        return replacers.getOrDefault(e, new ASTConditional(e.getLeft().accept(this, env), e.getRight().accept(this, env)));
    }

    @Override
    public IASTExp visit(ASTBiconditional e, Void env) {
        return replacers.getOrDefault(e, new ASTBiconditional(e.getLeft().accept(this, env), e.getRight().accept(this, env)));
    }

    @Override
    public IASTExp visit(ASTFun e, Void env) {
        return replacers.getOrDefault(e,
                new ASTFun(e.getName(), e.getTerms().stream().map(t -> t.accept(this, env)).toList()));
    }

    @Override
    public IASTExp visit(ASTPred e, Void env) {
        return replacers.getOrDefault(e, new ASTPred(e.getName(), e.getTerms().stream().map(t -> t.accept(this, env)).toList()));
    }

    @Override
    public IASTExp visit(ASTExistential e, Void env) {
        return replacers.getOrDefault(e, new ASTExistential(e.getLeft().accept(this, env), e.getRight().accept(this, env)));
    }

    @Override
    public IASTExp visit(ASTUniversal e, Void env) {
        return replacers.getOrDefault(e, new ASTUniversal(e.getLeft().accept(this, env), e.getRight().accept(this, env)));
    }

    @Override
    public IASTExp visit(ASTParenthesis e, Void env) {
        return replacers.getOrDefault(e, new ASTParenthesis(e.getExp().accept(this, env)));
    }
}
