package api.fol;

import com.logic.api.LogicAPI;
import com.logic.exps.exceptions.AmbiguousException;
import com.logic.exps.exceptions.MissingParenthesisException;
import com.logic.others.Utils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertThrows;

public class SyntaxTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "∀x (P(x) ∧ Q(x)",
            "∃y (R(y) ∨ S(y)",
            "P(a) ∧ Q(b) ∨ R(c, f(x,y)",
            "P(a) ∧ Q(b) ∨ R(c(a(b(f,g), a)",
    })
    void testParenthesisExps(String expression) {
        Throwable thrown = assertThrows(MissingParenthesisException.class, () -> LogicAPI.parseFOL(expression));
        System.out.println(Utils.getToken(thrown.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "P(x) ∧ Q(x) ∧ R(x)",
            "∀x P(x) ∧ Q(x) ∧ R(x)",
            "∃y R(y) ∧ S(y) ∧ T(y)",
            "P(a) ∧ Q(b) ∨ R(c)",
            "P(x) → Q(x) ∧ R(x)",
    })
    void testAmbiguityExps(String expression) {
        Throwable thrown = assertThrows(AmbiguousException.class, () -> LogicAPI.parseFOL(expression));
        System.out.println(Utils.getToken(thrown.getMessage()));
    }

}
