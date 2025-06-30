package com.logic.nd.checkers;

import com.logic.api.IFOLFormula;
import com.logic.api.IFormula;
import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.ASTExistential;
import com.logic.exps.asts.binary.ASTUniversal;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.binary.ASTEImp;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.*;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDTextException;
import com.logic.nd.exceptions.sideconditions.EUniNotFreeVariableException;
import com.logic.nd.exceptions.sideconditions.FreeVariableException;
import com.logic.nd.exceptions.sideconditions.IExistNotFreeVariableException;
import com.logic.others.Env;
import com.logic.others.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NDSideCondChecker implements INDVisitor<Void, Env<String, ASTHypothesis>> {

    private final Map<IASTExp, IFormula> formulas;

    NDSideCondChecker(Map<IASTExp, IFormula> formulas) {
        this.formulas = formulas;
    }

    public static Map<IASTExp, IFormula> check(IASTND nd, Map<IASTExp, IFormula> formulas, Map<String, IASTExp> premises) {
        NDSideCondChecker checker = new NDSideCondChecker(formulas);

        //Stores all opened hypotheses
        Env<String, ASTHypothesis> env = new Env<>();
        nd.accept(checker, env);

        return checker.formulas;
    }

    @Override
    public Void visit(ASTHypothesis h, Env<String, ASTHypothesis> env) {
        env.bind(h.getM(), h);

        return null;
    }

    @Override
    public Void visit(ASTIImp r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        env = env.beginScope();
        r.getHyp().accept(this, env);
        if (r.getCloseM() != null)
            env.removeAllChildren(r.getM());
        env.endScope();
        return null;
    }

    @Override
    public Void visit(ASTINeg r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        env = env.beginScope();
        r.getHyp().accept(this, env);
        if (r.getCloseM() != null)
            env.removeAllChildren(r.getM());
        env.endScope();
        return null;
    }

    @Override
    public Void visit(ASTERConj r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTELConj r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIRDis r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTILDis r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTAbsurdity r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        env = env.beginScope();
        r.getHyp().accept(this, env);
        if (r.getCloseM() != null)
            env.removeAllChildren(r.getM());
        env.endScope();
        return null;
    }

    @Override
    public Void visit(ASTIConj r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTEDis r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        r.getHyp1().accept(this, env);

        env = env.beginScope();
        r.getHyp2().accept(this, env);
        if (r.getCloseM() != null)
            env.removeAllChildren(r.getM());
        env.endScope();

        env = env.beginScope();
        r.getHyp3().accept(this, env);
        if (r.getCloseN() != null)
            env.removeAllChildren(r.getN());
        env.endScope();
        return null;
    }

    @Override
    public Void visit(ASTEImp r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTENeg r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTEUni r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        ASTUniversal uni = (ASTUniversal) r.getHyp().getConclusion();
        IFOLFormula psi = (IFOLFormula) formulas.get(ExpUtils.removeParenthesis(uni.getRight()));

        if (r.getMapping() instanceof ASTVariable x && psi.isABoundedVariable(x)) {
            r.appendErrors(new EUniNotFreeVariableException(r, x, uni.getLeft(), psi));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIExist r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        ASTExistential exi = (ASTExistential) r.getConclusion();
        IFOLFormula psi = (IFOLFormula) formulas.get(ExpUtils.removeParenthesis(exi.getRight()));

        if (r.getMapping() instanceof ASTVariable x && psi.isABoundedVariable(x)) {
            r.appendErrors(new IExistNotFreeVariableException(r, x, exi.getLeft(), psi));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIUni r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;

        r.getHyp().accept(this, env);

        ASTUniversal uni = (ASTUniversal) r.getConclusion();
        IFOLFormula psi = (IFOLFormula) formulas.get(ExpUtils.removeParenthesis(uni.getRight()));

        if (!uni.getLeft().equals(r.getMapping()) && psi.appearsFreeVariable(r.getMapping())) {
            r.appendErrors(new FreeVariableException(List.of(r.getHyp()), r.getMapping(), (ASTVariable) uni.getLeft()));
            return null;
        }

        List<IASTND> freeFormulas = new LinkedList<>();
        for (Map.Entry<String, ASTHypothesis> e : env.mapChild().entrySet()) {
            IFOLFormula formula = (IFOLFormula) formulas.get(e.getValue().getConclusion());

            if (formula.appearsFreeVariable(r.getMapping()))
                freeFormulas.add(e.getValue());
        }

        if (!freeFormulas.isEmpty()) {
            r.appendErrors(new FreeVariableException(freeFormulas, r.getMapping(), null));
            return null;
        }

        return null;
    }

    @Override
    public Void visit(ASTEExist r, Env<String, ASTHypothesis> env) {
        if (r.hasErrors()) return null;
        ASTExistential exi = (ASTExistential) r.getHyp1().getConclusion();
        IFOLFormula psi = (IFOLFormula) formulas.get(ExpUtils.removeParenthesis(exi.getRight()));
        IFOLFormula exp = (IFOLFormula) formulas.get(r.getConclusion());

        r.getHyp1().accept(this, env);

        env = env.beginScope();
        r.getHyp2().accept(this, env);

        if (!exi.getLeft().equals(r.getMapping()) && psi.appearsFreeVariable(r.getMapping())) {
            r.appendErrors(new FreeVariableException(List.of(r.getHyp1()), r.getMapping(), (ASTVariable) exi.getLeft()));
            return null;
        }

        if (exp.appearsFreeVariable(r.getMapping())) {
            r.appendErrors(new FreeVariableException(List.of(r.getHyp2()), r.getMapping(), null));
            return null;
        }

        List<IASTND> freeFormulas = new LinkedList<>();
        for (Map.Entry<String, ASTHypothesis> e : env.mapChild().entrySet()) {
            IFOLFormula formula = (IFOLFormula) formulas.get(e.getValue().getConclusion());

            if (!e.getValue().getConclusion().equals(r.getCloseM()) && formula != null && formula.appearsFreeVariable(r.getMapping()))
                freeFormulas.add(e.getValue());
        }

        if (!freeFormulas.isEmpty()) {
            r.appendErrors(new FreeVariableException(freeFormulas, r.getMapping(), null));
            return null;
        }

        if (r.getCloseM() != null)
            env.removeAllChildren(r.getM());
        env.endScope();


        return null;
    }

}