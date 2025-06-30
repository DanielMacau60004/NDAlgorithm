package com.logic.nd.algorithm.proofs;

import com.logic.api.IFormula;

import java.util.*;

public class BitGraphHandler {

    private final Map<IFormula, Short> indexedFormulas;
    private final Map<Short, IFormula> formulas;

    private final BitArray premises;

    public BitGraphHandler(Set<IFormula> premises, Set<IFormula> formulas) {
        this.indexedFormulas  = new HashMap<>(formulas.size());
        this.formulas  = new HashMap<>(premises.size());

        short index = 1;
        for(IFormula formula : formulas) {
            this.indexedFormulas.put(formula, index);
            this.formulas.put(index++, formula);
        }

        this.premises = toBitSet(premises);
    }

    public Set<IFormula> fromBitSet(BitArray bitArray) {
        Set<IFormula> result = new HashSet<>();
        for (short i : bitArray.getData()) {
            result.add(formulas.get(i));
        }
        return result;
    }
    public BitArray toBitSet(Set<IFormula> set) {
        BitArray bitArray = new BitArray();
        for (IFormula f : set)
            bitArray.set(indexedFormulas.get(f));

        return bitArray;
    }

    public BitArray getPremises() {return premises;}

    public short getIndex(IFormula formula) {return indexedFormulas.get(formula);}
    public IFormula get(short index) {return formulas.get(index);}

}
