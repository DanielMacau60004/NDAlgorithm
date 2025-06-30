package com.logic.nd.asts;

import com.logic.exps.asts.IASTExp;
import com.logic.nd.ERule;
import com.logic.nd.exceptions.NDRuleException;

import java.util.List;

public interface IASTND {

    IASTExp getConclusion();

    <T, E> T accept(INDVisitor<T, E> v, E env);

    ERule getRule();

    boolean hasErrors();

    void appendErrors(NDRuleException error);

    List<NDRuleException> getErrors();
}
