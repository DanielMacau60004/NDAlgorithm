package com.logic.exps.interpreters;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.IExpsVisitor;
import com.logic.exps.asts.binary.*;
import com.logic.exps.asts.others.*;
import com.logic.exps.asts.unary.ASTNot;
import com.logic.exps.asts.unary.ASTParenthesis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PLInterpreter implements IExpsVisitor<Boolean, Void> {

    public static final String ERROR_MESSAGE = "PL expressions only!";
    public static final String ERROR_MESSAGE_ARBITRARY = "Cannot evaluate an expression with arbitrary expressions!";
    public static final String ERROR_INTERPRETATION = "Missing literals %s in the interpretation!";

    private final Map<ASTLiteral, Boolean> interpretation;

    private final Set<String> missing;

    PLInterpreter(Map<ASTLiteral, Boolean> interpretation) {
        this.interpretation = interpretation;
        this.missing = new HashSet<>();
    }

    public static boolean interpret(IASTExp exp, Map<ASTLiteral, Boolean> interpretation) {
        PLInterpreter interpreter = new PLInterpreter(interpretation);
        Boolean result = exp.accept(interpreter, null);
        if (result == null)
            throw new RuntimeException(String.format(ERROR_INTERPRETATION, interpreter.missing));
        return result;
    }

    @Override
    public Boolean visit(ASTTop e, Void env) {
        return true;
    }

    @Override
    public Boolean visit(ASTBottom e, Void env) {
        return false;
    }

    @Override
    public Boolean visit(ASTConstant e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Boolean visit(ASTLiteral e, Void env) {
        Boolean bool = interpretation.get(e);
        if (bool == null) {
            missing.add(e.getName());
            return null;
        }

        return bool;
    }

    @Override
    public Boolean visit(ASTArbitrary e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE_ARBITRARY);
    }

    @Override
    public Boolean visit(ASTVariable e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Boolean visit(ASTNot e, Void env) {
        return !e.getExp().accept(this, env);
    }

    @Override
    public Boolean visit(ASTAnd e, Void env) {
        Boolean left = e.getLeft().accept(this, env);
        Boolean right = e.getRight().accept(this, env);

        if (Boolean.FALSE.equals(left) || Boolean.FALSE.equals(right))
            return false;
        if (left == null || right == null)
            return null;
        return left && right;
    }

    @Override
    public Boolean visit(ASTOr e, Void env) {
        Boolean left = e.getLeft().accept(this, env);
        Boolean right = e.getRight().accept(this, env);

        if (Boolean.TRUE.equals(left) || Boolean.TRUE.equals(right))
            return true;
        if (left == null || right == null)
            return null;
        return false;
    }

    @Override
    public Boolean visit(ASTConditional e, Void env) {
        Boolean left = e.getLeft().accept(this, env);
        Boolean right = e.getRight().accept(this, env);

        if (left == null || right == null)
            return null;
        return !e.getLeft().accept(this, env) || e.getRight().accept(this, env);
    }

    @Override
    public Boolean visit(ASTBiconditional e, Void env) {
        Boolean left = e.getLeft().accept(this, env);
        Boolean right = e.getRight().accept(this, env);

        if (left == null || right == null)
            return null;
        return e.getLeft().accept(this, env) == e.getRight().accept(this, env);
    }

    @Override
    public Boolean visit(ASTFun e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Boolean visit(ASTPred e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Boolean visit(ASTExistential e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Boolean visit(ASTUniversal e, Void env) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public Boolean visit(ASTParenthesis e, Void env) {
        return e.getExp().accept(this, env);
    }

}
