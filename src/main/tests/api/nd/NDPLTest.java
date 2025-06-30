package api.nd;

import com.logic.api.IFormula;
import com.logic.api.INDProof;
import com.logic.api.IPLFormula;
import com.logic.api.LogicAPI;
import com.logic.nd.algorithm.AlgoProofPLBuilder;
import com.logic.nd.algorithm.AlgoProofPLMainGoalBuilder;
import com.logic.nd.algorithm.AlgoProofPLGoalBuilder;
import com.logic.nd.algorithm.AlgoSettingsBuilder;
import com.logic.nd.algorithm.proofs.strategies.HeightTrimStrategy;
import com.logic.nd.algorithm.proofs.strategies.SizeTrimStrategy;
import com.logic.others.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

public class NDPLTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "p, q, p ∧ q",
            "(a ∧ b) ∧ c, b",
            "p ∧ q, q ∧ p",
            "q ∧ p, r, p ∧ (r ∧ q)",
            "a ∧ b, (c ∧ d) ∧ r, (a ∧ d) ∧ r",
            "p ∧ (q ∧ r), (r ∧ p) ∧ q",

            // 4.3 Implication
            "p → p",
            "p → (q → p)",
            "p → q, q → r, p → r",
            "p → ((p → q) → q)",
            "(p → q) → (p → r), q → (p → r)",
            "(p → q) → p, q → p",
            "p → (q → r), q → (p → r)",
            "p → (q → r), p → q, p → r",
            "(p → p) → q, (q → r) → r",
            "(p → (q → r)) → ((p → q) → (p → r))",

            // Mixed problems with conjunction
            "p ∧ q, p → q",
            "(p ∧ q) → p",
            "p → (q ∧ r), p → q",
            "((p ∧ q) → q) → (q → p), q → p",
            "(p ∧ q) → r, p → (q → r)",
            "(p → q) ∧ (p → r), p → (q ∧ r)",
            "p → (q ∧ r), (p → q) ∧ (p → r)",

            // 4.4 Disjunction
            "p ∨ q, q ∨ p",
            "p ∨ q, p ∨ (q ∨ r)",
            "(p ∨ q) ∨ r, p ∨ (q ∨ r)",
            "(p ∨ q) ∨ (r ∨ a), (p ∨ a) ∨ (r ∨ q)",

            // Mixed problems with conjunction
            "p ∧ (q ∨ r), (p ∧ q) ∨ (p ∧ r)",
            "(p ∨ q) ∧ (p ∨ r), p ∨ (q ∧ r)",
            "(p ∧ q) ∨ (p ∧ r), p ∧ (q ∨ r)",
            "p ∨ (q ∧ r), (p ∨ q) ∧ (p ∨ r)",

            // Mixed problems with implication
            "(p → q) ∨ q, p → q",
            "p ∨ q, (p → q) → q",
            "(p → q) → (p → r), (p ∨ r) → (q → r)",
            "(p → q) ∨ (p → r), p → (q ∨ r)",

            // Mixed problems with conjunction and implication
            "(p → q) ∧ (q → p), (p ∨ q) → (p ∧ q)",
            "(p ∨ q) → (p ∧ q), (p → q) ∧ (q → p)",
            "(q → r) ∧ (q ∨ p), (p → q) → (r ∧ q)",

            // Mixed problems
            "(p ∧ q) → ((p → q) → p)",

            // 4.6 Negation
            "p, ¬¬p",
            "¬p, ¬(p ∧ q)",
            "p → ¬p, ¬p",
            "¬(p → q), ¬q",
            "¬(p ∧ q), p → ¬q",
            "p → q, ¬q → ¬p",
            "¬((p ∧ ¬p) ∨ (q ∧ ¬q))",
            "¬(p ∨ q), ¬p ∧ ¬q",
            "¬p ∨ ¬q, ¬(p ∧ q)",

            // Ex falso quodlibet
            "¬p, p → q",
            "p ∧ ¬p, q",
            "p ∨ q, ¬p → q",
            "p → q, p ∧ ¬q, r",

            // Indirect proofs
            "¬¬p, p",
            "p ∨ ¬p",
            "¬(¬p ∨ ¬q), p ∧ q",
            "¬(p ∧ q), ¬p ∨ ¬q",

            // Mixed problems
            "¬(p → q), p",
            "(p → q) → p, p",
            "(p → q) → q, ¬q → p",
            "¬p ∧ ¬q, ¬(p ∨ q)",
            "p ∨ (p → q)",
            "(p → q) ∨ (q → r)",
            "¬p → q, r ∨ ¬q, p → (a ∨ b), ¬r ∧ ¬b, a",
            "p → (q ∨ r), (p → q) ∨ (p → r)"
    })
    void testAlgorithmWithParser(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);

            Set<IFormula> premisesProof = new HashSet<>();
            proof.getPremises().forEachRemaining(premisesProof::add);
            Assertions.assertEquals(premises, premisesProof);
        });

    }


    @ParameterizedTest
    @ValueSource(strings = {
            "a → (a ∨ b)",
            "(a ∨ a) → a",
            "(a ∧ b) → a",
            "a → (b → a)",
            "((a → b) ∧ (b → c)) → (a → c)",
            "(a → (b → c)) → ((a → b) → (a → c))",
            "(a → b) → (a → (b ∨ c))",
            "(b → c) → ((a ∧ b) → c)",
            "¬(a ∨ b) → ¬a",
            "(b → c) → ((a ∧ b) → (a ∧ c))",

            "¬a → (a → b)",
            "((a → b) ∧ ¬b) → ¬a",
            "(a → ¬¬a) ∧ (¬¬a → a)",
            "((a → b) → (¬b → ¬a)) ∧ ((¬b → ¬a) → (a → b))",
            "⊥ → a",
            //"⊤",
            "a ∨ ¬a",
            "((a → d) → a) → a",
            "a ∨ (a → b)",
            "(a → b) ∨ (b → d)",

            "¬a ∨ b, a → b",
            "a → b, ¬a ∨ b",
            "¬(a ∧ b), ¬a ∨ ¬b",
            "¬a ∨ ¬b, ¬(a ∧ b)",
            "¬(a ∨ b), ¬a ∧ ¬b",
            "¬a ∧ ¬b, ¬(a ∨ b)",
            "a ∨ (b ∧ c), (a ∨ b) ∧ (a ∨ c)",
            "(a ∨ b) ∧ (a ∨ c), a ∨ (b ∧ c)",
            "a ∧ (b ∨ c), (a ∧ b) ∨ (a ∧ c)",
            "(a ∧ b) ∨ (a ∧ c), a ∧ (b ∨ c)",
            "(a → b) ∧ (b → a), ((a ∧ c) → (b ∧ c)) ∧ ((b ∧ c) → (a ∧ c))",
            "¬(¬a ∨ ¬b), a ∧ b"
    })
    void testBig(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);

            Set<IFormula> premisesProof = new HashSet<>();
            proof.getPremises().forEachRemaining(premisesProof::add);
            Assertions.assertEquals(premises, premisesProof);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a → a",
            "a → (a ∨ b)",
            "(a ∨ a) → a",
            "(a ∧ b) → a",
            "a → (b → a)",
            "¬a → (a → b)",
            "¬(a ∨ b) → ¬a",
            "(b → c) → ((a ∧ b) → (a ∧ c))",
            "((a → b) ∧ ¬b) → ¬a",
            "a ∨ (a → b)",
            "(a → b) ∨ (b → d)",
            "(a → ¬¬a) ∧ (¬¬a → a)",
            "((a → b) ∧ (b → c)) → (a → c)",
            "(b → c) → ((a ∧ b) → c)",
            "(a → (b → c)) → ((a → b) → (a → c))",
            "((a → b) → (¬b → ¬a)) ∧ ((¬b → ¬a) → (a → b))",
            "a ∨ ¬a",
            "((a → d) → a) → a",
            "¬¬¬p → ¬p",
            "⊥ → a",
            "¬(¬a ∨ ¬b) → (a ∧ b)",
            "((a → b) ∧ (b → a)) → (((a ∧ c) → (b ∧ c)) ∧ ((b ∧ c) → (a ∧ c)))",
            "(p ∨ q) → (q ∨ p)",
            "(p ∨ q) → (p ∨ (q ∨ p))",
            "((p ∨ q) ∨ r) → (p ∨ (q ∨ r))",
            "(¬p ∨ ¬q) → ¬(p ∧ q)",
            "((p ∨ q) ∨ (r ∨ s)) → ((p ∨ s) ∨ (r ∨ q))",
            "(¬(p ∧ q) → (¬p ∨ ¬q)) ∧ ((¬p ∨ ¬q) → ¬(p ∧ q))",
            "((a ∨ b) ∧ (a ∨ c)) → (a ∨(b ∧ c))",
            "¬(¬a ∨ ¬b) → (a ∧ b)",
            "(a ∨ a) → a",
            "(a → ¬¬a) ∧ (¬¬a → a)",
    })
    void testMore(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .setAlgoSettingsBuilder(new AlgoSettingsBuilder()
                                    .setTrimStrategy(new HeightTrimStrategy())
                            /*.setTimeout(10000000)
                            .setHeightLimit(100000)
                            .setHypothesesPerState(1000000)
                            .setTotalClosedNodes(10000000))*/)
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);

            Set<IFormula> premisesProof = new HashSet<>();
            proof.getPremises().forEachRemaining(premisesProof::add);
            Assertions.assertEquals(premises, premisesProof);
        });
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "a ∨ ¬a"
    })
    void testSingle(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .setGoal(new AlgoProofPLGoalBuilder(LogicAPI.parsePL("⊥"))
                            .addHypothesis(LogicAPI.parsePL("¬(a ∨ ¬a)"))
                            .addHypothesis(LogicAPI.parsePL("¬a")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                            //.setTrimStrategy(new SizeTrimStrategy())
                            //.setBuildStrategy(new LinearBuildStrategy())
                    )
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a → ¬¬a"
    })
    void testSingle2(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .setGoal(new AlgoProofPLGoalBuilder(LogicAPI.parsePL("(¬a ∨ ¬¬a) → (a → ¬¬a)"))
                            .addHypothesis(LogicAPI.parsePL("a")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                            .setTrimStrategy(new SizeTrimStrategy())
                            //.setBuildStrategy(new LinearBuildStrategy())
                    )
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);
        });
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "[→I, 1] [(a ∨ a) → a. [∨E, 3, 3] [a. [H, 1] [a ∨ a.] [H, 3] [a.] [H, 3] [a.]]]",
            "[→I, 1] [(a ∨ a) → a. [∨E, , 3] [a. [H, 1] [a ∨ a.] [H, 3] [a.] [H, 3] [a.]]]",
            "[→I, 1] [(a ∨ a) → a. [∨E, , ] [a. [H, 1] [a ∨ a.] [H, 2] [a.] [H, 3] [a.]]]"
    })
    void testSingle1(String exp) {

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = LogicAPI.parseNDPLProof(exp);
            System.out.print("{");
            proof.getPremises().forEachRemaining(i -> System.out.print(Utils.getToken(i + ".")));
            System.out.println("} |= " + proof.getConclusion());
            //System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            //System.out.println(proof);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "((a → a) ∧ (a → a)) ∧ ((a → a) ∧ (a → a))",
            "(p ∧ q) → (r ∨ s), (p → r) ∨ (q → s)",
            "((p → q) → (¬p ∨ q)) ∧ ((¬p ∨ q) → (p → q))",
            "(((p ∧ q) ∨ (p ∧ ¬q)) ∨ (¬p ∧ q)) ∨ (¬p ∧ ¬q)",
            "a → ¬¬ a ",
            "(((p → (q ∨ s)) ∧ ((p ∧ r) → s)) ∧ ((s ∧ t) → (p ∨ ¬q))) → (((p ∧ (q → r)) → s) ∧ (((q ∧ s) ∧ t) → p))",
            "((p ∨ q) ∨ (r ∨ s)) → ((p ∨ s) ∨ (r ∨ q))",
            "(s ∨ t) → (s → ¬t), (s → ¬t) → (t → k), s ∨ t, s ∨ k",
            "(¬a ∨ ¬b) → ((c → (a ∧ b)) → ¬c)"

    })
    void testSpecific(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    //.setHypothesesPerState(5)
                                    .setTimeout(1500)
                                    //.setTimeout(Integer.MAX_VALUE)
                                    .setHypothesesPerGoal(Integer.MAX_VALUE)
                                    .setTotalClosedNodes(Integer.MAX_VALUE)
                                    .setTrimStrategy(new SizeTrimStrategy())
                    )
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);

            Set<IFormula> premisesProof = new HashSet<>();
            proof.getPremises().forEachRemaining(premisesProof::add);
            Assertions.assertEquals(premises, premisesProof);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "(((p → (q ∨ s)) ∧ ((p ∧ r) → s)) ∧ ((s ∧ t) → (p ∨ ¬q))) → (((p ∧ (q → r)) → s) ∧ (((q ∧ s) ∧ t) → p))",
    })
    void testSingle3(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .setGoal(new AlgoProofPLGoalBuilder(
                            LogicAPI.parsePL("((p ∧ (q → r)) → s) ∧ (((q ∧ s) ∧ t) → p)"))
                            .addHypothesis(LogicAPI.parsePL("((p → (q ∨ s)) ∧ ((p ∧ r) → s)) ∧ ((s ∧ t) → (p ∨ ¬q))")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    //.setHypothesesPerState(5)
                                    .setTimeout(1000)
                                    .setTotalClosedNodes(Integer.MAX_VALUE)
                                    .setTrimStrategy(new HeightTrimStrategy())
                    )
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a → a",
    })
    void testSingle4(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split(",");
        String expression = parts[parts.length - 1].trim();

        Set<IPLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parsePL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofPLBuilder(
                    new AlgoProofPLMainGoalBuilder(LogicAPI.parsePL(expression))
                            .addPremises(premises))
                    .setGoal(new AlgoProofPLGoalBuilder(
                            LogicAPI.parsePL("⊥"))
                            .addHypothesis(LogicAPI.parsePL("¬(a → a)")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    //.setHypothesesPerState(5)
                                    .setTimeout(1000)
                                    .setTotalClosedNodes(Integer.MAX_VALUE)
                                    .setTrimStrategy(new HeightTrimStrategy())
                    )
                    .build();
            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);
        });
    }
}
