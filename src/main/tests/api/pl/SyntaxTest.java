package api.pl;

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
            "(a ∧ a",
            "((a ∧ a",
            "((a ∧ a ∧ a",
    })
    void testParenthesisExps(String expression) {
        Throwable thrown = assertThrows(MissingParenthesisException.class, () -> LogicAPI.parsePL(expression));
        System.out.println(Utils.getToken(thrown.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a ∧ b ∧ c",
            "a ∧ b ∧ c ∧ d",
            "a ∨ (b ∧ c) ∧ d",
            "a ∧ (b ∧ c) ∨ d",
            "a → b ∧ c",
    })
    void testAmbiguityExps(String expression) {
        Throwable thrown = assertThrows(AmbiguousException.class, () -> LogicAPI.parsePL(expression));
        System.out.println(Utils.getToken(thrown.getMessage()));
    }


}
