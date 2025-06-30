package api.generics;

import com.logic.api.IFOLFormula;
import com.logic.api.IPLFormula;
import com.logic.api.LogicAPI;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.others.ASTArbitrary;
import com.logic.exps.interpreters.FOLReplaceExps;
import com.logic.others.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

public class GenericsTest {

    static Stream<Object[]> testCasesPL() throws Exception {
        return Stream.of(
                new Object[]{
                        LogicAPI.parsePL("(α ↔ β) ∧ (γ → δ)"),
                        Map.of(new ASTArbitrary("α"), LogicAPI.parsePL("(p → q)").getAST(),
                                new ASTArbitrary("γ"), LogicAPI.parsePL("(p ∧ q)").getAST())
                },
                new Object[]{
                        LogicAPI.parsePL("(x ∨ β) → z"),
                        Map.of(new ASTArbitrary("β"), LogicAPI.parsePL("(r ∧ s)").getAST())
                },
                new Object[]{
                        LogicAPI.parsePL("¬(β ∧ γ) ∨ (γ ↔ d)"),
                        Map.of(new ASTArbitrary("β"), LogicAPI.parsePL("(e → f)").getAST(),
                                new ASTArbitrary("γ"), LogicAPI.parsePL("(g ∨ h)").getAST())
                }
        );
    }

    @ParameterizedTest
    @MethodSource("testCasesPL")
    public void testFormulaReplacementPL(IPLFormula formula, Map<IASTExp, IASTExp> replacements) {
        String result = Utils.getToken(
                FOLReplaceExps.replace(formula.getAST(), replacements).toString()
        );

        Assertions.assertDoesNotThrow(() -> LogicAPI.parsePL(result));

        String expected = formula.toString();
        for (Map.Entry<IASTExp, IASTExp> entry : replacements.entrySet()) {
            expected = expected.replace(entry.getKey().toString(), entry.getValue().toString());
        }

        expected = Utils.getToken(expected);
        Assertions.assertEquals(expected, result);

        System.out.println("Original Formula: " + formula);
        System.out.println("Modified Formula: " + Utils.getToken(result));
    }

    static Stream<Object[]> testCasesFOL() throws Exception {
        return Stream.of(
                new Object[]{
                        LogicAPI.parseFOL("∀x (φ → (α ∨ β))"),
                        Map.of(new ASTArbitrary("φ"), LogicAPI.parseFOL("(P(x) → Q(x))").getAST(),
                                new ASTArbitrary("γ"), LogicAPI.parseFOL("(Z(x,x) ∨ P(x))").getAST())
                },
                new Object[]{
                        LogicAPI.parseFOL("∀z (φ6 → φ7) ↔ (φ2 ∨ (φ4 ∧ φ5))"),
                        Map.of(new ASTArbitrary("φ6"), LogicAPI.parseFOL("(P(x) → Q(x))").getAST(),
                                new ASTArbitrary("φ7"), LogicAPI.parseFOL("(Z(x,x) ∨ P(x))").getAST(),
                                new ASTArbitrary("φ2"), LogicAPI.parseFOL("∀z (P(z) → Q(z))").getAST(),
                                new ASTArbitrary("φ4"), LogicAPI.parseFOL("Z(x,fun(x))").getAST(),
                                new ASTArbitrary("φ5"), LogicAPI.parseFOL("P(test)").getAST())
                }
        );
    }

    @ParameterizedTest
    @MethodSource("testCasesFOL")
    public void testFormulaReplacementFOL(IFOLFormula formula, Map<IASTExp, IASTExp> replacements) {
        String result = Utils.getToken(
                FOLReplaceExps.replace(formula.getAST(), replacements).toString()
        );

        Assertions.assertDoesNotThrow(() -> LogicAPI.parseFOL(result));

        String expected = formula.toString();
        for (Map.Entry<IASTExp, IASTExp> entry : replacements.entrySet()) {
            expected = expected.replace(entry.getKey().toString(), entry.getValue().toString());
        }

        expected = Utils.getToken(expected);
        Assertions.assertEquals(expected, result);

        System.out.println("Original Formula: " + formula);
        System.out.println("Modified Formula: " + Utils.getToken(result));
    }

}
