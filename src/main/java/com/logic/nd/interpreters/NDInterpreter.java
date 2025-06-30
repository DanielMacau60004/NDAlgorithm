package com.logic.nd.interpreters;

import com.logic.api.IFormula;
import com.logic.api.INDProof;
import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackType;
import com.logic.nd.NDProof;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.binary.ASTEImp;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.*;
import com.logic.nd.exceptions.NDException;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.others.Env;

import java.util.*;

public class NDInterpreter implements INDVisitor<Integer, Env<Integer, IASTExp>> {

    private final List<NDRuleException> errors;

    private int size;

    NDInterpreter() {
        this.errors = new ArrayList<>();
        this.size = 0;
    }

    public static INDProof interpret(IASTND nd, Map<IASTExp, IFormula> formulas, Map<String, IASTExp> premises,
                                     Map<String, IASTExp> hypotheses) {
        NDInterpreter interpreter = new NDInterpreter();
        int height = nd.accept(interpreter, new Env<>());

        Set<IFormula> permSet = new HashSet<>();
        for (Map.Entry<String, IASTExp> entry : premises.entrySet())
            permSet.add(formulas.get(entry.getValue()));

        Map<String, IFormula> hypMap = new HashMap<>();
        for (Map.Entry<String, IASTExp> entry : hypotheses.entrySet())
            hypMap.put(entry.getKey(), formulas.get(entry.getValue()));

        INDProof proofObj = new NDProof(formulas.get(nd.getConclusion()), permSet, hypMap, nd, height, interpreter.size);

        if (!interpreter.errors.isEmpty())
            throw new NDException(proofObj, FeedbackType.SEMANTIC_ERROR, interpreter.errors);

        return proofObj;
    }

    private boolean checkForErrors(IASTND proof) {
        if (proof.hasErrors()) {
            errors.addAll(proof.getErrors());
            return true;
        }

        return false;
    }

    @Override
    public Integer visit(ASTHypothesis h, Env<Integer, IASTExp> env) {
        if (checkForErrors(h))
            return 0;

        size++;
        return 1;
    }

    @Override
    public Integer visit(ASTIImp r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTINeg r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTERConj r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTELConj r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTIRDis r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTILDis r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTAbsurdity r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTIConj r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return Math.max(r.getHyp1().accept(this, env), r.getHyp2().accept(this, env)) + 1;
    }

    @Override
    public Integer visit(ASTEDis r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return Math.max(r.getHyp1().accept(this, env),
                Math.max(r.getHyp2().accept(this, env), r.getHyp3().accept(this, env))) + 1;
    }

    @Override
    public Integer visit(ASTEImp r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return Math.max(r.getHyp1().accept(this, env), r.getHyp2().accept(this, env)) + 1;
    }

    @Override
    public Integer visit(ASTENeg r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return Math.max(r.getHyp1().accept(this, env), r.getHyp2().accept(this, env)) + 1;
    }

    @Override
    public Integer visit(ASTEUni r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTIExist r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTIUni r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return r.getHyp().accept(this, env) + 1;
    }

    @Override
    public Integer visit(ASTEExist r, Env<Integer, IASTExp> env) {
        if (checkForErrors(r))
            return 0;

        size++;
        return Math.max(r.getHyp1().accept(this, env), r.getHyp2().accept(this, env)) + 1;
    }

}
