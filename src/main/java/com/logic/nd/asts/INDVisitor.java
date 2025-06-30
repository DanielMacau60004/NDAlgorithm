package com.logic.nd.asts;

import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.binary.ASTEImp;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.*;

public interface INDVisitor<T, E> {

    T visit(ASTHypothesis h, E env);

    T visit(ASTIImp r, E env);

    T visit(ASTINeg r, E env);

    T visit(ASTERConj r, E env);

    T visit(ASTELConj r, E env);

    T visit(ASTIRDis r, E env);

    T visit(ASTILDis r, E env);

    T visit(ASTAbsurdity r, E env);

    T visit(ASTIConj r, E env);

    T visit(ASTEDis r, E env);

    T visit(ASTEImp r, E env);

    T visit(ASTENeg r, E env);

    T visit(ASTEUni r, E env);

    T visit(ASTIExist r, E env);

    T visit(ASTIUni r, E env);

    T visit(ASTEExist r, E env);
}
