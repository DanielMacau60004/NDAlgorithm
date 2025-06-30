package api.fol;

import com.logic.api.IFOLFormula;
import com.logic.api.LogicAPI;
import com.logic.exps.asts.others.AASTTerm;
import com.logic.exps.asts.others.ASTFun;
import com.logic.exps.asts.others.ASTPred;
import com.logic.exps.asts.others.ASTVariable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExpsWithWFFTest {

    @ParameterizedTest
    @CsvSource({
            "'∀x (φ → (α ∨ β))', '', ''",
            "'P(x) ∧ (Father(x, y) ∧ Q)', '', 'x,y'",
            "'P(x) ∧ Father(x, dani)', '', 'x'",
            "'∃x (P(x) ∧ ∀y Father(x, y))', 'x,y', ''",
            "'∀x (L(x) → (P(f(x)) ∧ Q(y)))', 'x', 'y'",
            "'∀x (L(x) → (P(dani) ∧ Q(y))) ∧ ∀y (L(x) → (P(dani) ∧ Q(y)))','x,y', 'x,y'",
            "'∀x ∃y φ', 'x,y', ''",
            "'∀x φ ∧ ψ ','',''",
            "'ψ ∧ ∀x φ','',''",
            "'∀y ∀x ∃z ψ ∧ (∀x φ ∧ ψ)','',''"
    })
    void test(String expression, String boundedStr, String unboundedStr) {
        AtomicReference<IFOLFormula> exp = new AtomicReference<>();
        assertDoesNotThrow(() -> exp.set(LogicAPI.parseFOL(expression)));

        if (!boundedStr.isEmpty()) {
            Set<String> bounded = new HashSet<>(Arrays.asList(boundedStr.split(",")));
            Iterator<ASTVariable> it = exp.get().iterateBoundedVariables();
            Set<String> iteratedBounded = new HashSet<>();
            it.forEachRemaining(i -> iteratedBounded.add(i.toString()));

            Assertions.assertEquals(iteratedBounded, bounded);
        }

        if (!unboundedStr.isEmpty()) {
            Set<String> unbounded = new HashSet<>(Arrays.asList(unboundedStr.split(",")));
            Iterator<ASTVariable> it = exp.get().iterateUnboundedVariables();
            Set<String> iteratedUnbounded = new HashSet<>();
            it.forEachRemaining(i -> iteratedUnbounded.add(i.toString()));

            Assertions.assertEquals(iteratedUnbounded, unbounded);
        }

        IFOLFormula fol = exp.get();
        System.out.println(fol);

        List<ASTFun> functions = new ArrayList<>();
        fol.iterateFunctions().forEachRemaining(functions::add);
        System.out.println("Functions: " + functions);

        List<ASTPred> predicates = new ArrayList<>();
        fol.iteratePredicates().forEachRemaining(predicates::add);
        System.out.println("Predicates: " + predicates);

        List<ASTVariable> bounded = new ArrayList<>();
        fol.iterateBoundedVariables().forEachRemaining(bounded::add);
        System.out.println("Bounded Variables: " + bounded);

        List<ASTVariable> unbounded = new ArrayList<>();
        fol.iterateUnboundedVariables().forEachRemaining(unbounded::add);
        System.out.println("Unbounded Variables: " + unbounded);

        List<AASTTerm> terms = new ArrayList<>();
        fol.iterateTerms().forEachRemaining(terms::add);
        System.out.println("Terms: " + terms);

        List<ASTVariable> variables = new ArrayList<>();
        fol.iterateVariables().forEachRemaining(variables::add);
        System.out.println("Variables: " + variables);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "P(x) ∧ P(x, y)",
            "P(f(x)) ∧ P(f(x, y(a, b(c, t))))",
            "P ∧ P(x)",
            "L(dani, dani(x))"
    })
    void testComplexGreekPredicatesFOLOperations(String expression) {
        Throwable thrown = assertThrows(Throwable.class, () -> LogicAPI.parseFOL(expression));
        System.out.println("Thrown Exception: " + thrown.getMessage());
    }

}
