package com.logic.nd;

import com.logic.api.IFormula;
import com.logic.api.INDProof;
import com.logic.nd.asts.IASTND;
import com.logic.others.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NDProof implements INDProof {

    private final IFormula conclusion;
    private final Set<IFormula> premises;
    private final Map<String, IFormula> hypotheses;
    private final IASTND proof;
    private final int height;
    private final int size;

    public NDProof(IFormula conclusion, Set<IFormula> premises, Map<String, IFormula> hypotheses, IASTND proof, int height, int size) {
        this.conclusion = conclusion;
        this.premises = premises;
        this.hypotheses = hypotheses;
        this.proof = proof;
        this.height = height;
        this.size = size;
    }

    @Override
    public IFormula getConclusion() {
        return conclusion;
    }

    @Override
    public Iterator<Map.Entry<String, IFormula>> getHypotheses() {
        return hypotheses.entrySet().iterator();
    }

    @Override
    public Iterator<IFormula> getPremises() {
        return premises.iterator();
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public IASTND getAST() {
        return this.proof;
    }

    @Override
    public String toString() {
        return Utils.getToken(proof.toString());
    }
}
