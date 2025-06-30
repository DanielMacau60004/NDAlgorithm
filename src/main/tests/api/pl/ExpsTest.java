package api.pl;

import com.logic.api.LogicAPI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExpsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "aa",
            "a a",
            "Z",
            "a ∧",
            "a ∧ bb",
            "a ∧ T",
            "a(",
            "((a(",
            "a ∧ a ∧ p",
            "a ∧ a → p",
            "a) ∧ (a → p",
            "¬(¬(¬p))",
            "¬¬(¬p)",
            "(p)"
    })
    void testIncorrect1Exps(String expression) {
        assertThrows(Throwable.class, () -> LogicAPI.parsePL(expression));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "p", "q", "p ∧ q", "(a ∧ b) ∧ c", "p ∧ q", "q ∧ p",
            "q ∧ p", "r", "p ∧ (r ∧ q)",
            "a ∧ b", "(c ∧ d) ∧ r", "(a ∧ d) ∧ r",
            "p ∧ (q ∧ r)", "(r ∧ p) ∧ q",

            "p → p", "p → (q → p)", "p → q", "q → r", "p → r",
            "p → ((p → q) → q)", "(p → q) → (p → r)", "q → (p → r)",
            "(p → q) → p", "q → p", "p → (q → r)", "q → (p → r)",
            "p → (q → r)", "p → q", "p → r", "(p → p) → q", "(q → r) → r",
            "(p → (q → r)) → ((p → q) → (p → r))",

            "p ∧ q", "p → q", "(p ∧ q) → p",
            "p → (q ∧ r)", "p → q",
            "((p ∧ q) → q) → (q → p)", "q → p",
            "(p ∧ q) → r", "p → (q → r)",
            "(p → q) ∧ (p → r)", "p → (q ∧ r)",
            "p → (q ∧ r)", "(p → q) ∧ (p → r)",

            "p ∨ q", "q ∨ p", "p ∨ q", "p ∨ (q ∨ r)",
            "(p ∨ q) ∨ r", "p ∨ (q ∨ r)",
            "(p ∨ q) ∨ (r ∨ a)", "(p ∨ a) ∨ (r ∨ q)",

            "p ∧ (q ∨ r)", "(p ∧ q) ∨ (p ∧ r)",
            "(p ∨ q) ∧ (p ∨ r)", "p ∨ (q ∧ r)",
            "(p ∧ q) ∨ (p ∧ r)", "p ∧ (q ∨ r)",
            "p ∨ (q ∧ r)", "(p ∨ q) ∧ (p ∨ r)",

            "(p → q) ∨ q", "p → q",
            "p ∨ q", "(p → q) → q",
            "(p → q) → (p → r)", "(p ∨ r) → (q → r)",
            "(p → q) ∨ (p → r)", "p → (q ∨ r)",

            "(p → q) ∧ (q → p)", "(p ∨ q) → (p ∧ q)",
            "(p ∨ q) → (p ∧ q)", "(p → q) ∧ (q → p)",
            "(q → r) ∧ (q ∨ p)", "(p → q) → (r ∧ q)",

            "p ↔ q", "q ↔ p", "p", "(p ↔ q) ↔ r", "q ↔ r",
            "(p ↔ q) ↔ (q ↔ p)",

            "(p ∨ q) ↔ q", "p → q", "(p ∧ q) ↔ p", "p → q",
            "p → q", "(p ∨ q) ↔ q", "p → q", "(p ∧ q) ↔ p",
            "(p → q) ∧ (q → p)", "p ↔ q", "(p ∧ q) → ((p → q) → p)",
            "((p → q) ↔ p) → (p ↔ q)",
            "((p ∨ q) ↔ q) ↔ p", "p ↔ q",
            "p → (q ↔ r)", "(p ∧ q) ↔ (p ∧ r)",
            "(p ∨ (q ∧ r)) ↔ ((p ∨ q) ∧ (p ∨ r))",

            "p", "¬¬p", "¬p", "¬(p ∧ q)", "p → ¬p", "¬p",
            "¬(p → q)", "¬q", "¬(p ∧ q)", "p → ¬q",
            "p → q", "¬q → ¬p", "¬((p ∧ ¬p) ∨ (q ∧ ¬q))",
            "¬(p ∨ q)", "¬p ∧ ¬q", "¬p ∨ ¬q", "¬(p ∧ q)",

            "¬p", "p → q", "p ∧ ¬p", "q",
            "p ∨ q", "¬p → q", "p → q", "p ∧ ¬q", "r",
            "p ∨ q", "p ↔ q", "¬(p ∧ q)", "r",

            "¬¬p", "p", "p ∨ ¬p", "¬(¬p ∨ ¬q)", "p ∧ q",
            "¬(p ∧ q)", "¬p ∨ ¬q",

            "¬(p → q)", "p", "(p → q) → p", "p",
            "p ↔ ¬¬q", "p ↔ q", "(p → q) → q", "¬q → p",
            "¬p ∧ ¬q", "¬(p ∨ q)", "p ∨ (p → q)",
            "(p → q) ∨ (q → r)",
            "¬p → q", "r ∨ ¬q", "p → (a ∨ b)", "¬r ∧ ¬b", "a",
            "p → (q ∨ r)", "(p → q) ∨ (p → r)",
            "¬(p ∧ q) ↔ (¬p ∨ ¬q)",

            "(a ∧ q)", "¬(a ∧ q)", "¬(¬a ∧ ¬¬¬¬q)"

    })
    void testCorrect1Exps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "p", "q", "r", "a", "b", "x", "y", "z", "⊤", "⊥",

            "¬p", "¬q", "¬r", "¬(p ∧ q)", "¬(p ∨ q)", "¬(p → q)", "¬(p ↔ q)",

            "p ∧ q", "p ∨ q", "p → q", "p ↔ q",
            "(p ∧ q) ∨ (r ∧ s)", "(p → q) ∧ (q → p)", "(p ↔ q) → r",
            "¬(p ∧ q) ∨ r", "¬(p ∧ q) ∧ (p → q)",
            "p ∧ (q ∨ r)", "(p ∧ q) ∨ (r ∧ s)", "(p → q) ∧ (r → s)",
            "((p ∧ q) → r) ∧ ((r ∨ s) → t)",
            "(p → (q → r)) ↔ (p → r)", "p ↔ (q ∨ r)",
            "(p ∧ q) → (r ∨ (s ∧ t))", "(p → q) ∧ (q → p)",
            "(p ↔ q) → (r → s)", "(p → q) → (p ∨ r)",
            "¬p ∨ (q ∧ r)", "¬(p ∧ (q ∨ r))",
            "(p → q) → (r ∨ s)", "p ↔ (q ∨ r)", "(p → (q → r))",
            "p → (q ∨ (r ∧ s))", "(p ∨ (q ∧ r)) → p",
            "(p → q) → ((p → q) → r)", "¬(p ∨ q) ∨ (p ∧ q)"
    })
    void testCorrect2Exps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "((p ∧ q) ∨ r)", "(p ∨ (q ∧ r))", "(p → (q → r))", "(p → (q → r)) → p",
            "(p → q)", "(p → q) → r", "(p → (q → r)) → (p → r)",
            "(p ∧ q) → ((r → s) ∨ (t → u))",

            "¬¬p", "¬¬¬p",
            "¬(¬p ∧ q)", "¬(p ∨ ¬q)", "¬(p ∧ q) ∧ ¬(r ∨ s)",

            "p ↔ p", "p ↔ q", "p ↔ (q ↔ p)", "(p ↔ q) ↔ p", "(p ∨ q) ↔ p",
            "(p ↔ (q ∧ r)) → s", "(p ∧ q) ↔ (r ∨ s)",

            "p ↔ (q → r)", "p ↔ (q ∧ r)", "(p ↔ q) ↔ (r ∧ s)", "(p ↔ q) ↔ (p ∨ q)",
            "(p ↔ (q → r)) ∧ (r → s)", "(p ↔ q) → (r → s)", "(p ↔ q) ∨ r"
    })
    void testCorrectBoundaryExps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a a", "p → → q", "p → (q", "(p ∧ q", "p ∨", "∧ p", "p ∨ (", "p → q)",
            "((a ∧ b)", "p ↔", "p ∧ (q ∨ r", "(p → q ∨", "p ∧ (¬q ∨", "⊥ → (p →",
            "(p ∧ q ∨ r", "p → (q →", "¬(p → (q →"
    })
    void testIncorrect2Exps(String expression) {
        assertThrows(Throwable.class, () -> LogicAPI.parsePL(expression));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "(p → (q ∨ (r ∧ s)))", "((p → q) ↔ (r → s)) ∧ (p ∧ q)", "(p → (q → (r ∨ s)))",
            "(¬p → q) ↔ (r → (s ∧ t))", "(p → (q ↔ r)) ∨ ((s → t) ∧ r)",
            "(p ∧ (q → r)) ∨ ((p → q) → (r ∨ s))", "(p → q) ∨ ((r → s) ↔ (p → q))",
            "(p ↔ (q ∧ r)) → ((s ∨ t) ↔ (u → v))"
    })
    void testComplexExps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "φ ∧ ψ", "α ∨ β", "γ → δ", "φ2 ∧ φ3", "φ4 → φ5", "φ6 ↔ φ7",
            "(φ ∧ (ψ → α))", "(φ6 → φ7) ∧ (φ2 ↔ φ3)", "(β ∧ γ) → δ"
    })
    void testGreekSymbolsExps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "⊤", "⊥", "⊤ ∧ ⊥", "⊥ ∨ ⊤", "⊤ → ⊥", "⊥ ↔ ⊤",
            "(⊥ ∧ ⊤) → (⊥ ∨ ⊤)", "(⊥ → ⊤) ↔ (⊥ ∨ ⊤)", "⊤ → (⊥ ∨ ⊤)",
            "(⊥ → (⊤ ∧ ⊥)) ↔ ⊥"
    })
    void testSpecialLogicSymbolsExps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "(φ ∧ ψ) → (α ∨ β)",
            "(φ2 ∨ φ3) → (φ4 ∧ φ5)",
            "(φ6 → φ7) ↔ (φ2 ∨ (φ4 ∧ φ5))",
            "(α ↔ β) ∧ (γ → δ)",
            "((φ → (ψ ↔ α)) ∨ (β ∧ (γ → δ)))",
            "(φ2 ∧ φ3) ↔ ((φ4 ∨ φ5) → (φ6 ∧ φ7))",
            "(α ∨ (β ∧ (γ → δ))) ↔ (φ1 → φ)",
            "((φ6 ∧ φ7) → (φ2 ∨ φ3)) ↔ (φ4 ∧ φ5)",
            "(φ6 → (φ4 ∧ (φ5 ∨ φ2))) ↔ (φ3 → α)",
            "(φ ∧ ψ) → ((φ2 → φ3) ∧ (φ4 ∨ φ5))"
    })
    void testComplexGreekSymbolsExps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "(p ∧ φ) → (q ∨ ψ)",
            "(r → (α ∧ φ6)) ↔ (β ∨ φ2)",
            "(a ∧ b) → (γ ∨ (δ ∧ φ1))",
            "(x ∨ (φ ∧ ψ)) → (y ∧ (α → β))",
            "(p → (φ2 ∧ φ3)) ↔ ((r ∨ s) ∧ (φ6 ∨ φ7))",
            "(z ∧ φ) → (p ∨ (q ∧ α))",
            "(p ↔ (r ∨ φ6)) → (q ∧ (φ3 → φ))",
            "(x ∨ (y ∧ (α → β))) → (φ2 ∧ φ)",
            "(a → (β ∨ γ)) ∧ (δ → (φ6 ∨ φ7))",
            "(p ∧ (q ∨ (r → φ6))) ↔ (α ∧ (β ∨ (γ → δ)))"
    })
    void testMixGreekAndLettersExps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parsePL(expression));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "(p ∧ φ) → (q ∨ ψ).dwadawd",
            "(r → (α ∧ b)) ∨ (β ∨ φ2).WDAWDAWDC ADAW DA DA",
    })
    void testEOF(String expression) {
        assertDoesNotThrow(() -> System.out.println(LogicAPI.parsePL(expression)));
    }

}
