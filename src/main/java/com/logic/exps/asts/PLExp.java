package com.logic.exps.asts;

import com.logic.api.IPLFormula;
import com.logic.exps.asts.others.ASTArbitrary;
import com.logic.exps.asts.others.ASTLiteral;
import com.logic.exps.interpreters.PLInterpreter;
import com.logic.others.Utils;

import java.util.*;

public class PLExp implements IPLFormula {

    private final IASTExp exp;
    private final Set<ASTLiteral> literals;
    private final Set<ASTArbitrary> generics;

    public PLExp(IASTExp exp, Set<ASTLiteral> literals, Set<ASTArbitrary> generics) {
        this.exp = exp;
        this.literals = literals;
        this.generics = generics;
    }

    @Override
    public IASTExp getAST() {
        return exp;
    }

    @Override
    public Iterator<ASTArbitrary> iterateGenerics() {
        return generics.iterator();
    }

    @Override
    public boolean hasGenerics() {
        return !generics.isEmpty();
    }

    @Override
    public Iterator<ASTLiteral> iterateLiterals() {
        return literals.iterator();
    }

    @Override
    public boolean interpret(Map<ASTLiteral, Boolean> interpretation) {
        return PLInterpreter.interpret(exp, interpretation);
    }

    @Override
    public Map<Map<ASTLiteral, Boolean>, Boolean> getTruthTable() {
        List<ASTLiteral> literalList = new ArrayList<>(literals);
        int numCombinations = (int) Math.pow(2, literals.size());

        Map<Map<ASTLiteral, Boolean>, Boolean> table = new LinkedHashMap<>();

        for (int i = 0; i < numCombinations; i++) {
            Map<ASTLiteral, Boolean> interpretation = new LinkedHashMap<>();
            for (int j = 0; j < literalList.size(); j++) {
                boolean truthValue = (i & (1 << j)) != 0;
                interpretation.put(literalList.get(j), truthValue);
            }
            table.put(interpretation, interpret(interpretation));
        }

        return table;
    }

    private Map<Map<ASTLiteral, Boolean>, Boolean> getShortenTruthTable(
            Map<Map<ASTLiteral, Boolean>, Boolean> table, Set<ASTLiteral> literals) {
        Set<Map<ASTLiteral, Boolean>> interpretations = new HashSet<>(table.keySet());

        for (Map<ASTLiteral, Boolean> interpretation : interpretations) {
            Boolean bool = table.get(interpretation);
            table.remove(interpretation);

            interpretation.keySet().retainAll(literals);
            Boolean newBool = table.get(interpretation);

            if (newBool == null)
                table.put(interpretation, bool);
            else if (newBool != bool)
                return null;
        }

        return table;
    }

    @Override
    public boolean isEquivalentTo(IPLFormula other) {
        Set<ASTLiteral> intersection = new LinkedHashSet<>(literals);
        Iterator<ASTLiteral> it = other.iterateLiterals();

        Set<ASTLiteral> iteratedBounded = new HashSet<>();
        it.forEachRemaining(iteratedBounded::add);
        intersection.retainAll(iteratedBounded);

        Map<Map<ASTLiteral, Boolean>, Boolean> truthTable = getShortenTruthTable(getTruthTable(), intersection);
        if (truthTable == null)
            return false;

        Map<Map<ASTLiteral, Boolean>, Boolean> truthTableOther = getShortenTruthTable(other.getTruthTable(), intersection);
        if (truthTableOther == null)
            return false;

        return truthTable.equals(truthTableOther);
    }

    @Override
    public String toString() {
        return Utils.getToken(exp.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PLExp plExp = (PLExp) o;
        return exp.equals(plExp.getAST());
    }

    @Override
    public int hashCode() {
        return exp.hashCode();
    }
}
