package com.logic.nd.asts;

import com.logic.nd.ERule;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.interpreters.NDInterpretString;
import com.logic.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public abstract class AASTND implements IASTND {

    protected ERule rule;

    protected String getToken(int kind) {
        return Parser.tokenImage[kind].replace("\"", "");
    }
    protected List<NDRuleException> errors;

    public AASTND(ERule rule) {
        this.rule = rule;
        errors = new ArrayList<>();
    }

    @Override
    public ERule getRule() {
        return rule;
    }

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public void appendErrors(NDRuleException error) {
        errors.add(error);
    }

    @Override
    public List<NDRuleException> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AASTND s)
            return this.toString().equals(s.toString());
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return NDInterpretString.interpret(this);
    }
}
