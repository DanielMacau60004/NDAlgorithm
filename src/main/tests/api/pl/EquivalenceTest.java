package api.pl;

import com.logic.api.IPLFormula;
import com.logic.api.LogicAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EquivalenceTest {

    // Helper method to parse and evaluate equivalences of expressions
    private boolean evaluateEquivalence(String exp1, String exp2) throws Exception {
        IPLFormula e1 = LogicAPI.parsePL(exp1);
        IPLFormula e2 = LogicAPI.parsePL(exp2);

        return e1.isEquivalentTo(e2);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a, b",
            "a ∧ b, a ∨ b",
            "a → b, b → a",
            "a ∨ (b ∧ c), (a ∧ b) ∨ c",
            "¬a ∧ b, ¬(a ∨ b)",
            "a ∧ (b ∨ c), (a ∨ b) ∧ (a ∨ c)",
            "a → (b → c), (a ∨ b) → c",
            "(a → b) → c, a → (b → c)",
            "¬(a ∨ b), ¬a ∨ ¬b",
            "a → (b → c), (a → b) → c"
    })
    void testNonEquivalentPropositionalLogicExpressions(String expressions) throws Exception {
        String[] exprParts = expressions.split(", ");
        String exp1 = exprParts[0].trim();
        String exp2 = exprParts[1].trim();

        boolean result = evaluateEquivalence(exp1, exp2);

        Assertions.assertFalse(result, "Expected " + exp1 + " to not be equivalent to " + exp2);
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "a → b, ¬a ∨ b",
            "¬(a ∧ b), ¬a ∨ ¬b",
            "¬¬a, a",
            "a ∧ (b ∨ c), (a ∧ b) ∨ (a ∧ c)",
            "a ∨ (b ∧ c), (a ∨ b) ∧ (a ∨ c)",
            "a ∧ (a ∨ b), a",
            "a ∨ ¬a, ⊤",
            "a ∧ ¬a, ⊥",
            "a → b, ¬b → ¬a",
            "a ∨ (a ∧ b), a",
            "a ∧ (a ∨ b), a"
    })
    void testPropositionalLogicEquivalences(String expressions) throws Exception {
        String[] exprParts = expressions.split(", ");
        String exp1 = exprParts[0].trim();
        String exp2 = exprParts[1].trim();

        boolean result = evaluateEquivalence(exp1, exp2);

        Assertions.assertTrue(result, "Expected " + exp1 + " to be equivalent to " + exp2);
    }
}