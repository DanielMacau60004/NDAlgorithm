package api.fol;

import com.logic.api.LogicAPI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExpsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "φ", "ψ", "α", "β", "γ", "φ1", "φ2", "φ3", "φ4", "φ5", "φ6", "φ7",
            "Father(x)", "Adult(y)", "L(z, x)",
            "∀x (P(x) → Q(x))", "∃y R(y)", "¬∃z (S(z) ∧ T(z))", "∀x ∃y (L(x, y) → M(y, x))",
            "P(x) → (Q(y) ∧ R(z))", "¬(A(x) ∨ B(y))", "(C(x) → D(y)) ↔ (E(z) ∧ F(x))",
            "∃x ∀y (G(x, y) ∨ H(y, x))", "∀x (I(x) ↔ J(x))", "¬∀y (K(y) ∧ L(y))",
            "¬∀x (M(x) → N(x))", "∃y (O(y) ∨ P(y))", "(Q(x) → R(y)) ∨ (S(z) → T(x))",
            "∀x ∃y ∃z (U(x, y, z) ∧ V(y, x, z))", "¬(W(x) → X(y))", "Y(x) ↔ ¬Z(y)",
            "∀x (∃y (F(x, y) ∨ G(y, x)) → ∀z H(z, x))", "¬∃x (I(x) → J(x))",
            "(K(x) ∨ L(y)) → (M(z) ∧ N(w))", "∃x (P(x) ↔ Q(x))",
            "∀x ∃y (R(x, y) ∨ S(y, x))", "∀x (T(x) → (U(x) ↔ V(x)))",
            "(W(x) ∨ X(y)) → ¬Y(z)", "¬(Z(x) → ∀y A(y))",
            "∀x ∃y ∃z (B(x) ∧ (C(y) ∨ D(z)))", "∃x (E(f(x, x)) ∧ F(f(c,x), f(c,d)))"
    })
    void testCorrectExps(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parseFOL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "∀", "∃", "¬", "→", "↔", "∧", "∨", "( )", ",",
            "∀x (P(x) Q(x))", "∃y R(y", "¬∃z (S(z ∧ T(z))", "∀x ∃y L(x y) → M(y, x))",
            "P(x) → Q(y) ∧ R(z))", "¬(A(x) ∨ B(y", "(C(x) → D(y)) ↔ (E(z) ∧ F(x",
            "∃x ∀y (G(x, y ∨ H(y, x))", "∀x (I(x) ↔ J(x)", "¬∀y (K(y ∧ L(y))",
            "height(x + weight(y))", "f(x - g(y))", "daniel(x * h(z))", "p(x y) → q(y, x)",
            "¬∀x (M(x → N(x))", "∃y (O(y ∨ P(y))", "(Q(x) → R(y)) ∨ (S(z) → T(x)",
            "∀x (∃y (F(x, y) ∨ G(y, x)) → ∀z H(z, x)", "¬∃w (I(w) → J(w",
            "(K(x) ∨ L(y)) → (M(z) ∧ N(w)", "∃x (P(x) ↔ Q(x"
    })
    void testIncorrectExps(String expression) {
        assertThrows(Throwable.class, () -> LogicAPI.parseFOL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "∀x P(x)", "∃y Q(y)", "∀x ∃y R(x, y)", "∃x ∀y (P(x) → Q(y))",
            "∀x (P(x) ∧ ∃y Q(y))", "∀x ∃y ∃z ((P(x) ∧ Q(y)) → R(z))",
            "∃x ∃y ∀z ((P(x) ∨ Q(y)) ∧ R(z))", "∀x ∃y (P(y) ∧ Q(x))",
            "∃x ∀y P(y) → ∃z Q(z)", "∀x ∃y (R(x, y) ↔ S(y, x))",
            "∀x (∃y (P(x) → Q(y)) → ∀z R(z))", "¬∀x ∃y (S(x) ∧ T(y))",
            "∃x (P(x) → ∀y (Q(y) ∨ R(y)))", "∀x ∃y ∀z P(x, y, z)",
            "∃x ∃y ∀z (P(x) ∧ (Q(y) ∨ R(z)))", "∀x ∃y (A(x) ↔ B(y))",
            "∀x (P(x) → (∃y Q(y) ∨ ∀z R(z)))", "∃x ∀y (P(y) → Q(x))",
            "∀x (∃y (P(y) ↔ Q(x)) ∧ ∀z R(z, x))", "∃x ∃y ((P(x) ∧ Q(y)) → ∀z R(z))"
    })
    void testCorrectQuantifiers(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parseFOL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "∀x (φ → (α ∨ β))",
            "∃y (φ2 ∧ φ3) → (φ4 ∧ φ5)",
            "∀z (φ6 → φ7) ↔ (φ2 ∨ (φ4 ∧ φ5))",
            "(α ↔ β) ∧ (γ → δ)",
            "((φ → (ψ ↔ α)) ∨ (β ∧ (γ → δ)))",
            "∃x (φ2 ∧ φ3) ↔ ((φ4 ∨ φ5) → (φ6 ∧ φ7))",
            "(α ∨ (β ∧ (γ → δ))) ↔ (φ1 → φ)",
            "((φ6 ∧ φ7) → (φ2 ∨ φ3)) ↔ (φ4 ∧ φ5)",
            "(φ6 → (φ4 ∧ (φ5 ∨ φ2))) ↔ (φ3 → α)",
            "(φ ∧ ψ) → ((φ2 → φ3) ∧ (φ4 ∨ φ5))",
            "(φ ∧ φ1) → ((φ2 → φ) ∧ (φ3 ∨ φ2))"
    })
    void testComplexGreekPredicatesFOLOperations(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parseFOL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "P(x)", "Q(y)",
            "Father(x, y)", "Adult(z)",
            "(Father(x, y) ∧ Height(x))", "(L(x) → L(y))",
            "Father(x, y) ∧ Weight(x)", "(Height(x) ∨ Weight(y))",
            "β ∧ L(x)", "(Father(x, y) → L(x))",
            "(Height(x) ∧ Weight(y))", "(P(x) ∧ Q(x))",
            "∃y (Father(x, y) ∧ Adult(y))", "∃x (Father(x, y) ∧ Weight(x))",
            "(L(x) ∧ ∃y Father(x, y))", "L(x) ∧ ¬Father(y, z)",
            "(Height(x) → Weight(x))", "L(x) → (Father(x, y) ∧ Weight(y))",
            "Father(x, y) ∧ (Height(x) ∨ Weight(y))", "(L(x) ∧ ∃y Father(x, y))",
            "(P(x) → Q(y))", "∃x (Father(x, y) ∧ L(z))",
            "(Father(x, y) ∧ Weight(x))", "(L(x) ∧ ¬Height(x))",
            "∃x (Father(x, x) ∧ L(y))", "Father(x, y) ∨ L(z)",
            "(Height(x) ∧ (Father(x, y) ∨ L(y)))", "¬(L(x) ∧ Father(x, y))",
            "∀x (Height(x) → ∃y (Weight(y) ∧ Father(x, y)))",
            "(P(x) ∧ Q(x))", "(Father(x, y) ∧ L(z))",
            "Height(x) → (Weight(x) ∧ Father(x, y))", "(L(x) → (Height(x) ∨ Father(x, y)))",
            "Father(x, y) ∧ (L(x) → Weight(x))", "(∃y L(y) ∧ ∀x Father(x, y))",
            "L(x) → ∃y (Father(x, y) ∧ Weight(x))", "(Height(x) → L(x))",
            "¬Father(x, y) ∧ (L(x) → Height(y))", "∀x (L(x) → ∃y (Weight(y) ∧ Father(x, y)))",
            "(P(x) ∧ (Q(y) ∨ R(x)))", "(∃y Father(x, y) ∧ ∀z (L(z) ∧ Weight(z)))",
            "(Father(x, y) ∧ ¬L(y))", "(∃x Father(x, y) ∧ Height(x))",
            "(P(x) ∧ (Weight(x) ∧ Q(x)))", "(Father(x, y) ∧ (Height(x) → L(x)))",
            "P(x) ∧ ∀y (Q(y) → Father(x, y))", "(Height(x) → (Father(x, y) ∨ L(x)))",
            "Father(x, z) → (L(y) ∨ ¬Height(x))", "∃x (L(x) ∧ ∀y Father(x, y))",
            "∀x (Father(x, y) → (L(x) ∧ Weight(y)))", "(L(x) → ¬Height(x))",
            "(Father(x, y) ∧ (Height(x) → Weight(x)))", "(L(x) ∧ (Height(x) → Father(x, y)))",
            "(Height(x) ∧ ∀y (Father(x, y) → L(y)))", "∃x (L(x) ∧ Father(x, y))",
            "(Father(x, y) → (Height(x) ∧ L(x)))", "(¬P(x) ∧ (Q(x) ∨ L(x)))",
            "Father(x, y) → ∀z (Height(x) ∧ L(z))", "(Height(x) ∧ (Weight(y) → Father(x, y)))",
            "(Height(x) → ¬Father(x, y))", "(L(x) ∧ (Father(x, y) ∨ ¬Weight(y)))",
            "P(x) ∨ Q(x)", "(Weight(x) → (Height(y) ∨ Father(x, y)))",
            "(Father(x, y) ∧ ¬L(x))", "¬(P(x) ∧ Q(x))",
            "(Height(x) → (L(x) ∨ Father(x, y)))", "∃y (P(x) ∧ L(y))",
            "(Father(x, y) → (Height(x) ∧ Weight(y)))", "¬Father(x, y) ∨ (Height(x) ∧ Weight(x))",
            "(L(x) ∧ ¬Weight(y))", "(Height(x) → (L(x) ∧ Father(x, y)))",
            "(L(x) ∧ (Father(x, y) → ¬Weight(x)))", "∀x (Father(x, y) ∨ ¬L(x))",
            "(P(x) ∨ (L(y) ∧ Father(x, y)))", "(Height(x) ∧ ∀y (L(y) → Father(x, y)))",
            "((P(x) ∧ Q(x)) ∧ ∀y Father(x, y))", "(L(x) ∧ (Father(x, y) → Weight(x)))",
            "(Father(x, y) ∧ ∃z (Height(z) ∧ L(z)))", "∀x (L(x) → (Father(x, y) ∧ ¬Weight(x)))",
            "(Height(x) → ∀y (L(y) → Father(x, y)))", "(∃x Father(x, y) ∧ (L(x) ∧ Height(x)))",
            "(Father(x, y) ∧ (L(x) → Height(y)))", "∃x (P(x) ∧ ∀y Father(x, y))",
            "(Height(x) ∧ (Father(x, y) → Weight(x)))", "(Height(x) ∧ ¬L(x))",
            "(P(x) ∧ (Father(x, y) ∨ Weight(x)))", "∀x (Father(x, y) → ∃z (Height(z) ∧ L(x)))",
            "(Father(x, y) ∧ (P(x) → L(x)))", "(Father(x, y) → ¬Height(x))",
            "∀x (L(x) → (P(x) ∧ Q(y)))", "(Father(x, y) ∧ (L(x) ∧ ¬Weight(x)))",
            "∃x (Father(x, y) ∧ Height(x))", "¬(Father(x, y) ∧ P(x))",
            "(Father(x, y) ∧ (L(x) ∧ Weight(y)))", "(Height(x) ∨ ∃y Father(x, y))",
            "(Father(x, y) ∧ ∀z (L(z) ∧ P(z)))", "∃x (P(x) ∧ Weight(x))",
            "(Father(x, y) ∧ (L(x) ∨ ¬Weight(y)))",

            "∀x (L(x) → (P(x) ∧ Q(y))) ∧ ∀x (L(x) → (P(x) ∧ Q(y)))",
            "∀x (L(x) → (P(x) ∧ Q(y))) ∧ ∀x (L(x) → (P(x) ∧ Q(y)))",
            "∀x (L(x) → (P(x) ∧ Q(y))) ∧ (∀x (L(x) → (P(x) ∧ Q(y))) ∧ ∀x (L(x) → (P(x) ∧ Q(y))))",
    })
    void testMultiple(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parseFOL(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "(P(x) ∧ Q(y))",
            "Q(a1, a2)"
    })
    void testSimple(String expression) {
        assertDoesNotThrow(() -> LogicAPI.parseFOL(expression));
    }
}
