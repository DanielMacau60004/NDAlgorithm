package com.logic.nd.algorithm.proofs;

import com.logic.api.IFOLFormula;
import com.logic.api.IFormula;
import com.logic.exps.asts.others.ASTVariable;

import java.util.*;

public class GoalNode {

    protected final BitGraphHandler handler; //TODO this is object does not change

    private final short exp;
    private final BitArray assumptions;
    private final BitArray noFree;
    private boolean isClosed;
    private int height;
    private final int hash;

    public GoalNode(Short exp, BitArray assumptions, int height, BitArray noFree, BitGraphHandler handler) {
        this(exp, assumptions, null, height, noFree, handler);
    }

    GoalNode(Short exp, BitArray assumptions, Short assumption, int height
            , BitArray noFree, BitGraphHandler handler) {
        this.exp = exp;
        this.assumptions = assumptions;
        this.height = height;
        this.noFree = noFree;
        this.handler = handler;

        if (assumption != null)
            assumptions.set(assumption);

        resetClose();

        hash = Objects.hash(assumptions, exp, noFree);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public IFormula getExp() {
        return handler.get(exp);
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed() {
        isClosed = true;
    }

    public void resetClose() {
        isClosed = (assumptions.contains(exp) || handler.getPremises().contains(exp))
                && (noFree == null || !noFree.contains(exp));
    }

    public Integer numberOfHypotheses() {
        return assumptions.length();
    }

    public Set<IFormula> getAssumptions() {
        return handler.fromBitSet(assumptions);
    }

    public GoalNode transit(IFormula exp, IFormula assumption, ASTVariable notFree) {
        BitArray noFree = this.noFree;

        if (notFree != null) {
            noFree = new BitArray(this.noFree);

            for (short i : assumptions.getData())
                if (((IFOLFormula) handler.get(i)).appearsFreeVariable(notFree))
                    noFree.set(i);

            BitArray premises = handler.getPremises();
            for (short i : premises.getData())
                if (((IFOLFormula) handler.get(i)).appearsFreeVariable(notFree))
                    noFree.set(i);
        }

        BitArray assumptions = this.assumptions;
        if (assumption != null) {
            assumptions = new BitArray(this.assumptions);
            assumptions.set(handler.getIndex(assumption));
        }

        if (assumption != null)
            return new GoalNode(handler.getIndex(exp), assumptions, handler.getIndex(assumption),
                    height + 1, noFree, handler);
        return new GoalNode(handler.getIndex(exp), assumptions, height + 1, noFree, handler);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoalNode stateNode = (GoalNode) o;
        return exp == stateNode.exp && Objects.equals(assumptions, stateNode.assumptions)
                && Objects.equals(noFree, stateNode.noFree);
    }

    @Override
    public int hashCode() {
        return hash;
    }

}
