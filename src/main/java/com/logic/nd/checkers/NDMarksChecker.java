package com.logic.nd.checkers;

import com.logic.api.IFOLFormula;
import com.logic.api.IFormula;
import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.ASTConditional;
import com.logic.exps.asts.binary.ASTExistential;
import com.logic.exps.asts.binary.ASTOr;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.exps.asts.unary.ASTNot;
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
import com.logic.nd.exceptions.CloseMarkException;
import com.logic.nd.exceptions.MarkAssignException;
import com.logic.others.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NDMarksChecker implements INDVisitor<Void, Env<String, IASTExp>> {

    private final Map<IASTExp, IFormula> formulas;
    private final Map<String, IASTExp> hypotheses;

    NDMarksChecker(Map<IASTExp, IFormula> formulas, Map<String, IASTExp> hypotheses) {
        this.formulas = formulas;
        this.hypotheses = hypotheses;
    }

    public static void check(IASTND nd, Map<IASTExp, IFormula> formulas, Map<String, IASTExp> premises,
                             Map<String, IASTExp> hypotheses) {
        NDMarksChecker checker = new NDMarksChecker(formulas, hypotheses);
        Env<String, IASTExp> env = new Env<>();
        nd.accept(checker, env);


        premises.putAll(env.mapChild());
    }

    @Override
    public Void visit(ASTHypothesis h, Env<String, IASTExp> env) {
        if (h.hasErrors()) return null;

        IASTExp hyp = h.getConclusion();

        if (h.getM() != null && hypotheses.containsKey(h.getM()) && !hypotheses.get(h.getM()).equals(hyp)) {
            h.appendErrors(new MarkAssignException(h.getM(), hyp));
            return null;
        }

        hypotheses.put(h.getM(), hyp);
        env.bind(h.getM(), hyp);

        return null;
    }

    @Override
    public Void visit(ASTIImp r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;

        ASTConditional cond = (ASTConditional) r.getConclusion();
        IASTExp mark = ExpUtils.removeParenthesis(cond.getLeft());

        env = env.beginScope();
        r.getHyp().accept(this, env);

        if (r.getM() != null) {
            IASTExp refMark = env.findChild(r.getM());

            if ((hypotheses.containsKey(r.getM()) && !mark.equals(hypotheses.get(r.getM()))) ||
                    (refMark != null && !mark.equals(refMark))) {
                r.appendErrors(new CloseMarkException(r.getM(), mark, env));
                return null;
            }

            r.setCloseM(mark);
            env.removeAllChildren(r.getM());

        }

        env.endScope();

        return null;
    }

    @Override
    public Void visit(ASTINeg r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;

        ASTNot neg = (ASTNot) r.getConclusion();
        IASTExp mark = ExpUtils.removeParenthesis(neg.getExp());

        env = env.beginScope();
        r.getHyp().accept(this, env);

        if (r.getM() != null) {
            IASTExp refMark = env.findChild(r.getM());

            if ((hypotheses.containsKey(r.getM()) && !mark.equals(hypotheses.get(r.getM()))) ||
                    (refMark != null && !mark.equals(refMark))) {
                r.appendErrors(new CloseMarkException(r.getM(), mark, env));
                return null;
            }

            r.setCloseM(mark);
            env.removeAllChildren(r.getM());

        }

        env.endScope();

        return null;
    }

    @Override
    public Void visit(ASTERConj r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTELConj r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIRDis r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTILDis r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTAbsurdity r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;

        IASTExp mark = ExpUtils.negate(r.getConclusion());

        env = env.beginScope();
        r.getHyp().accept(this, env);

        if (r.getM() != null) {
            IASTExp refMark = env.findChild(r.getM());

            if ((hypotheses.containsKey(r.getM()) && !mark.equals(hypotheses.get(r.getM()))) ||
                    (refMark != null && !mark.equals(refMark))) {
                r.appendErrors(new CloseMarkException(r.getM(), mark, env));
                return null;
            }

            r.setCloseM(mark);
            env.removeAllChildren(r.getM());

        }

        env.endScope();

        return null;
    }

    @Override
    public Void visit(ASTIConj r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);
        return null;
    }

    @Override
    public Void visit(ASTEDis r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        ASTOr or = (ASTOr) r.getHyp1().getConclusion();
        IASTExp left = ExpUtils.removeParenthesis(or.getLeft());
        IASTExp right = ExpUtils.removeParenthesis(or.getRight());

        r.getHyp1().accept(this, env);

        env = env.beginScope();
        r.getHyp2().accept(this, env);

        if (r.getM() != null) {
            IASTExp refMark = env.findChild(r.getM());

            if ((hypotheses.containsKey(r.getM()) && !left.equals(hypotheses.get(r.getM()))) ||
                    (refMark != null && !left.equals(refMark))) {
                r.appendErrors(new CloseMarkException(r.getM(), left, env));
                return null;
            }

            r.setCloseM(left);
            env.removeAllChildren(r.getM());

        }

        env = env.endScope();

        env = env.beginScope();
        r.getHyp3().accept(this, env);

        if (r.getN() != null) {
            IASTExp refMark = env.findChild(r.getN());

            if ((hypotheses.containsKey(r.getN()) && !right.equals(hypotheses.get(r.getN()))) ||
                    (refMark != null && !right.equals(refMark))) {
                r.appendErrors(new CloseMarkException(r.getN(), right, env));
                return null;
            }

            r.setCloseN(right);
            env.removeAllChildren(r.getN());

        }

        env.endScope();

        return null;
    }

    @Override
    public Void visit(ASTEImp r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);

        return null;
    }

    @Override
    public Void visit(ASTENeg r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        r.getHyp1().accept(this, env);
        r.getHyp2().accept(this, env);

        return null;
    }

    @Override
    public Void visit(ASTEUni r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIExist r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTIUni r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        return r.getHyp().accept(this, env);
    }

    @Override
    public Void visit(ASTEExist r, Env<String, IASTExp> env) {
        if (r.hasErrors()) return null;
        ASTExistential exi = (ASTExistential) r.getHyp1().getConclusion();
        IASTExp mark = ExpUtils.removeParenthesis(exi.getRight());
        r.getHyp1().accept(this, env);

        env = env.beginScope();
        r.getHyp2().accept(this, env);

        if (r.getM() != null) {
            IASTExp refMark = env.findChild(r.getM());
            IASTExp mappingEx = null;

            if (refMark != null) {
                ASTVariable x = (ASTVariable) exi.getLeft();
                IASTExp psi = ExpUtils.removeParenthesis(exi.getRight());
                IFOLFormula psiXY = (IFOLFormula) formulas.get(refMark);

                List<ASTVariable> variables = new ArrayList<>();
                variables.add(x);
                psiXY.iterateVariables().forEachRemaining(variables::add);

                for (ASTVariable var : variables) {
                    mappingEx = FOLReplaceExps.replace(psi, x, var);
                    if (mappingEx.equals(refMark)) {
                        r.setMapping(var);
                        break;
                    }
                }

                r.setCloseM(mappingEx);
                env.removeAllChildren(r.getM());
            }

            if ((refMark != null && mappingEx == null)) {
                r.appendErrors(new CloseMarkException(r.getM(), null, env));
                return null;
            }

        }

        env.endScope();

        return null;
    }


}
