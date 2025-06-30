package com.logic.nd.checkers;

import com.logic.api.IFormula;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.exceptions.ExpException;
import com.logic.exps.interpreters.FOLWFFInterpreter;
import com.logic.exps.interpreters.PLWFFInterpreter;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.binary.ASTEImp;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.*;

import java.util.HashMap;
import java.util.Map;

public class NDWWFExpsChecker implements INDVisitor<Void, Void> {

    private final boolean fol;

    private final Map<IASTExp, IFormula> formulas;

    NDWWFExpsChecker(boolean fol, Map<IASTExp, IFormula> formulas) {
        this.fol = fol;
        this.formulas = formulas;
    }

    public static Map<IASTExp, IFormula> checkPL(IASTND nd) {
        NDWWFExpsChecker checker = new NDWWFExpsChecker(false, new HashMap<>());
        nd.accept(checker, null);
        return checker.formulas;
    }

    public static Map<IASTExp, IFormula> checkFOL(IASTND nd) {
        NDWWFExpsChecker checker = new NDWWFExpsChecker(true, new HashMap<>());
        nd.accept(checker, null);
        return checker.formulas;
    }

    private IFormula verifyAndCreateFormula(IASTExp exp) {
        return fol ? FOLWFFInterpreter.check(exp)
                : PLWFFInterpreter.check(exp);
    }

    @Override
    public Void visit(ASTHypothesis h, Void env) {
        IASTExp exp = h.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));
        return null;
    }

    @Override
    public Void visit(ASTIImp r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTINeg r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTERConj r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTELConj r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIRDis r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTILDis r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTAbsurdity r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIConj r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTEDis r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        r.getHyp3().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTEImp r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTENeg r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTEUni r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIExist r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIUni r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTEExist r, Void env) {
        IASTExp exp = r.getConclusion();
        formulas.put(exp, verifyAndCreateFormula(exp));

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

}