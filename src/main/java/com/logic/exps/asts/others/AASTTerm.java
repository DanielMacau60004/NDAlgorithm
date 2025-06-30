package com.logic.exps.asts.others;

import com.logic.exps.asts.AASTExp;

public abstract class AASTTerm extends AASTExp {

    protected final String name;

    public AASTTerm(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }

}
