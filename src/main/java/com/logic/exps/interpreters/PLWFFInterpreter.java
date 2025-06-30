package com.logic.exps.interpreters;

import com.logic.api.IPLFormula;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;
import com.logic.exps.asts.PLExp;
import com.logic.exps.asts.binary.*;
import com.logic.exps.asts.others.*;
import com.logic.exps.asts.unary.ASTNot;
import com.logic.exps.asts.unary.ASTParenthesis;

import java.util.LinkedHashSet;
import java.util.Set;

public class PLWFFInterpreter implements IExpsVisitor<Void, Void> {

    public static final String ERROR_MESSAGE = "PL expressions only!";
    final Set<ASTLiteral> literals;
    final Set<ASTArbitrary> generics;

    PLWFFInterpreter() {
        literals = new LinkedHashSet<>();
        generics = new LinkedHashSet<>();
    }

    public static IPLFormula check(IASTExp exp) {
        PLWFFInterpreter checker = new PLWFFInterpreter();
        exp.accept(checker, null);

        return new PLExp(exp, checker.literals, checker.generics);
    }

    @Override
    public Void visit(ASTTop e, Void env) {
        return null;
    }

    @Override
    public Void visit(ASTBottom e, Void env) {
        return null;
    }

    @Override
    public Void visit(ASTConstant e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Void visit(ASTLiteral e, Void env) {
        literals.add(e);
        return null;
    }

    @Override
    public Void visit(ASTArbitrary e, Void env) {
        generics.add(e);
        return null;
    }

    @Override
    public Void visit(ASTVariable e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Void visit(ASTNot e, Void env) {
        e.getExp().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTAnd e, Void env) {
        e.getLeft().accept(this, env);
        e.getRight().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTOr e, Void env) {
        e.getLeft().accept(this, env);
        e.getRight().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTConditional e, Void env) {
        e.getLeft().accept(this, env);
        e.getRight().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTBiconditional e, Void env) {
        e.getLeft().accept(this, env);
        e.getRight().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTFun e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Void visit(ASTPred e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Void visit(ASTExistential e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Void visit(ASTUniversal e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Void visit(ASTParenthesis e, Void env) {
        e.getExp().accept(this, env);
        return null;
    }
}
