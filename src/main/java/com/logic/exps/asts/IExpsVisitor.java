package com.logic.exps.asts;


import com.logic.exps.asts.binary.*;
import com.logic.exps.asts.others.*;
import com.logic.exps.asts.unary.ASTNot;
import com.logic.exps.asts.unary.ASTParenthesis;

public interface IExpsVisitor<T, E> {


    T visit(ASTTop e, E env);

    T visit(ASTBottom e, E env);


    T visit(ASTConstant e, E env);

    T visit(ASTLiteral e, E env);

    T visit(ASTArbitrary e, E env);

    T visit(ASTVariable e, E env);


    T visit(ASTNot e, E env);

    T visit(ASTAnd e, E env);

    T visit(ASTOr e, E env);

    T visit(ASTConditional e, E env);

    T visit(ASTBiconditional e, E env);


    T visit(ASTFun e, E env);

    T visit(ASTPred e, E env);

    T visit(ASTExistential e, E env);

    T visit(ASTUniversal e, E env);


    T visit(ASTParenthesis e, E env);

}
