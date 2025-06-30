package com.logic.nd.checkers;

import com.logic.api.IFOLFormula;
import com.logic.api.IFormula;
import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.*;
import com.logic.exps.asts.others.AASTTerm;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.exps.asts.unary.ASTNot;
import com.logic.exps.interpreters.FOLWFFInterpreter;
import com.logic.exps.interpreters.FOLReplaceExps;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.binary.ASTEImp;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.*;
import com.logic.nd.exceptions.sideconditions.EUniInvalidMappingException;
import com.logic.nd.exceptions.rules.*;
import com.logic.nd.exceptions.sideconditions.IExistInvalidMappingException;
import com.logic.nd.exceptions.sideconditions.IUniInvalidMappingException;

import java.util.*;

public class NDWWFChecker implements INDVisitor<Void, Void> {

    private final Map<IASTExp, IFormula> formulas;

    NDWWFChecker(Map<IASTExp, IFormula> formulas) {
        this.formulas = formulas;
    }

    public static void check(IASTND nd, Map<IASTExp, IFormula> formulas) {
        NDWWFChecker checker = new NDWWFChecker(formulas);
        nd.accept(checker, null);
    }

    @Override
    public Void visit(ASTHypothesis h, Void env) {
        return null;
    }

    @Override
    public Void visit(ASTIImp r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(exp instanceof ASTConditional cond)) {
            r.appendErrors(new IImpException(r));
            return null;
        }

        IASTExp right = ExpUtils.removeParenthesis(cond.getRight());

        if (!right.equals(r.getHyp().getConclusion())) {
            r.appendErrors(new IImpException(r, cond));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTINeg r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(exp instanceof ASTNot neg)) {
            r.appendErrors(new INegException(r));
            return null;
        }

        if (!r.getHyp().getConclusion().equals(ExpUtils.BOT)) {
            r.appendErrors(new INegException(r, neg));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTERConj r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(r.getHyp().getConclusion() instanceof ASTAnd and)) {
            r.appendErrors(new ERConjException(r));
            return null;
        }

        IASTExp left = ExpUtils.removeParenthesis(and.getLeft());
        if (!left.equals(exp)) {
            r.appendErrors(new ERConjException(r, and));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTELConj r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(r.getHyp().getConclusion() instanceof ASTAnd and)) {
            r.appendErrors(new ELConjException(r));
            return null;
        }

        IASTExp right = ExpUtils.removeParenthesis(and.getRight());
        if (!right.equals(exp)) {
            r.appendErrors(new ELConjException(r, and));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIRDis r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(exp instanceof ASTOr or)) {
            r.appendErrors(new IRDisException(r));
            return null;
        }

        IASTExp left = ExpUtils.removeParenthesis(or.getLeft());
        if (!left.equals(r.getHyp().getConclusion())) {
            r.appendErrors(new IRDisException(r, or));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTILDis r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(exp instanceof ASTOr or)) {
            r.appendErrors(new ILDisException(r));
            return null;
        }

        IASTExp right = ExpUtils.removeParenthesis(or.getRight());
        if (!right.equals(r.getHyp().getConclusion())) {
            r.appendErrors(new ILDisException(r, or));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTAbsurdity r, Void env) {
        if(r.hasErrors()) return null;
        if (!r.getHyp().getConclusion().equals(ExpUtils.BOT)) {
            r.appendErrors(new AbsurdityException(r));
            return null;
        }

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIConj r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(exp instanceof ASTAnd and)) {
            r.appendErrors(new IConjException(r));
            return null;
        }

        IASTExp left = ExpUtils.removeParenthesis(and.getLeft());
        IASTExp right = ExpUtils.removeParenthesis(and.getRight());

        if (!left.equals(r.getHyp1().getConclusion()) || !right.equals(r.getHyp2().getConclusion())) {
            r.appendErrors(new IConjException(r, and));
            return null;
        }

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);

        return null;
    }

    @Override
    public Void visit(ASTEDis r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!(r.getHyp1().getConclusion() instanceof ASTOr or)) {
            r.appendErrors(new EDisException(r));
            return null;
        }

        IASTExp left = ExpUtils.removeParenthesis(r.getHyp2().getConclusion());
        IASTExp right = ExpUtils.removeParenthesis(r.getHyp3().getConclusion());

        if (!exp.equals(left) || !exp.equals(right)) {
            r.appendErrors(new EDisException(r, or));
            return null;
        }

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        r.getHyp3().accept(this, env);

        return null;
    }

    @Override
    public Void visit(ASTEImp r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        IASTExp other = r.getHyp1().getConclusion();
        if (!(r.getHyp2().getConclusion() instanceof ASTConditional imp)) {
            r.appendErrors(new EImpException(r));
            return null;
        }

        IASTExp left = ExpUtils.removeParenthesis(imp.getLeft());
        IASTExp right = ExpUtils.removeParenthesis(imp.getRight());
        if (!left.equals(other) || !right.equals(exp)) {
            r.appendErrors(new EImpException(r, imp));
            return null;
        }

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);

        return null;
    }

    @Override
    public Void visit(ASTENeg r, Void env) {
        if(r.hasErrors()) return null;
        IASTExp exp = r.getConclusion();

        if (!exp.equals(ExpUtils.BOT)) {
            r.appendErrors(new ENegException(r, r.getHyp1().getConclusion()));
            return null;
        }

        IASTExp leftNot = ExpUtils.invert(r.getHyp2().getConclusion());
        IASTExp rightNot = ExpUtils.invert(r.getHyp1().getConclusion());
        if (!r.getHyp1().getConclusion().equals(leftNot) &&
                !r.getHyp2().getConclusion().equals(rightNot)) {
            r.appendErrors(new ENegException(r, r.getHyp1().getConclusion()));
            return null;
        }

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);

        return null;
    }

    @Override
    public Void visit(ASTEUni r, Void env) {
        if(r.hasErrors()) return null;

        if (!(r.getHyp().getConclusion() instanceof ASTUniversal uni)) {
            r.appendErrors(new EUniException(r));
            return null;
        }

        //Find mapping
        ASTVariable x = (ASTVariable) uni.getLeft();
        IASTExp psi = ExpUtils.removeParenthesis(uni.getRight());
        IFOLFormula psiXT = (IFOLFormula) formulas.get(r.getConclusion());

        List<AASTTerm> terms = new ArrayList<>();
        terms.add(x);
        psiXT.iterateTerms().forEachRemaining(terms::add);

        Set<IASTExp> outcomes = new HashSet<>();
        if (r.getMapping() == null)
            for (AASTTerm term : terms) {

                IASTExp cExp = FOLReplaceExps.replace(psi, x, term);
                outcomes.add(cExp);
                if (cExp.equals(psiXT.getAST())) {
                    r.setMapping(term);
                    break;
                }
            }

        if (r.getMapping() == null) {
            r.appendErrors(new EUniInvalidMappingException(r,  x, psi, psiXT.getAST(), outcomes));
            return null;
        }

        formulas.put(psi, FOLWFFInterpreter.check(psi));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIExist r, Void env) {
        if(r.hasErrors()) return null;
        if (!(r.getConclusion() instanceof ASTExistential exi)) {
            r.appendErrors(new IExistException(r));
            return null;
        }

        //Find mapping
        ASTVariable x = (ASTVariable) exi.getLeft();
        IASTExp psi = ExpUtils.removeParenthesis(exi.getRight());
        IFOLFormula psiXT = (IFOLFormula) formulas.get(r.getHyp().getConclusion());

        List<AASTTerm> terms = new ArrayList<>();
        terms.add(x);
        psiXT.iterateTerms().forEachRemaining(terms::add);

        Set<IASTExp> outcomes = new HashSet<>();
        if (r.getMapping() == null)
            for (AASTTerm term : terms) {

                IASTExp cExp = FOLReplaceExps.replace(psi, x, term);
                outcomes.add(cExp);
                if (cExp.equals(psiXT.getAST())) {
                    r.setMapping(term);
                    break;
                }
            }

        if (r.getMapping() == null) {
            r.appendErrors(new IExistInvalidMappingException(r, x, psi, psiXT.getAST(), outcomes));
            return null;
        }

        formulas.put(psi, FOLWFFInterpreter.check(psi));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIUni r, Void env) {
        if(r.hasErrors()) return null;

        if (!(r.getConclusion() instanceof ASTUniversal uni)) {
            r.appendErrors(new IUniException(r));
            return null;
        }

        //Find mapping
        ASTVariable x = (ASTVariable) uni.getLeft();
        IASTExp psi = ExpUtils.removeParenthesis(uni.getRight());
        IFOLFormula psiXY = (IFOLFormula) formulas.get(r.getHyp().getConclusion());

        List<ASTVariable> variables = new ArrayList<>();
        variables.add(x); //It can be itself
        psiXY.iterateVariables().forEachRemaining(variables::add);

        Set<IASTExp> outcomes = new HashSet<>();
        if (r.getMapping() == null)
            for (ASTVariable var : variables) {

                IASTExp cExp = FOLReplaceExps.replace(psi, x, var);
                outcomes.add(cExp);
                if (cExp.equals(psiXY.getAST())) {
                    r.setMapping(var);
                    break;
                }
            }

        if (r.getMapping() == null) {
            r.appendErrors(new IUniInvalidMappingException(r, x, psi, psiXY.getAST(), outcomes));
            return null;
        }

        formulas.put(psi, FOLWFFInterpreter.check(psi));

        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTEExist r, Void env) {
        if(r.hasErrors()) return null;
        if (!(r.getHyp1().getConclusion() instanceof ASTExistential exist)) {
            r.appendErrors(new EExistException(r));
            return null;
        }

        if (!r.getConclusion().equals(r.getHyp2().getConclusion())) {
            r.appendErrors(new EExistException(r, exist));
            return null;
        }

        IASTExp exp = ExpUtils.removeParenthesis(exist.getRight());
        formulas.put(exp, FOLWFFInterpreter.check(exp));

        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);

        return null;
    }

}