package api.nd;

import com.logic.api.IFOLFormula;
import com.logic.api.IFormula;
import com.logic.api.INDProof;
import com.logic.api.LogicAPI;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.nd.algorithm.AlgoProofFOLBuilder;
import com.logic.nd.algorithm.AlgoProofFOLMainGoalBuilder;
import com.logic.nd.algorithm.AlgoProofFOLGoalBuilder;
import com.logic.nd.algorithm.AlgoSettingsBuilder;
import com.logic.nd.algorithm.proofs.strategies.HeightTrimStrategy;
import com.logic.others.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

public class NDFOLTest {

    @ParameterizedTest
    @ValueSource(strings = {
            " [∀I] [∀x ∃y φ. [∃I] [∃y φ. [∀E] [φ. [∃E,1] [∀x φ. [H,2] [∃y ∀x φ.] [H,3] [∀x φ.]]]]]",
            " [∧I] [((∀x φ ∧ ψ) → ∀x (φ ∧ ψ)) ∧ (∀x (φ ∧ ψ) → (∀x φ ∧ ψ)). [→I, 1] [(∀x φ ∧ ψ) → ∀x (φ ∧ ψ). [∀I] [∀x (φ ∧ ψ). [∧I] [φ ∧ ψ. [∀E] [φ. [∧ER] [∀x φ. [H, 1] [∀x φ ∧ ψ.]]] [∧EL] [ψ. [H, 1] [∀x φ ∧ ψ.]]]]] [→I, 2] [∀x (φ ∧ ψ) → (∀x φ ∧ ψ). [∧I] [∀x φ ∧ ψ. [∀I] [∀x φ. [∧ER] [φ. [∀E] [φ ∧ ψ. [H, 2] [∀x (φ ∧ ψ).]]]] [∧EL] [ψ. [∀E] [φ ∧ ψ. [H, 2] [∀x (φ ∧ ψ).]]]]]]",
    })
    void testFailSingle(String proof) {
        Throwable thrown = Assertions.assertThrows(Throwable.class, () -> {
            LogicAPI.parseNDFOLProof(proof);
        });
        System.out.println(Utils.getToken(thrown.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " [∀E] [∃y (P(y) ∧ P(y)). [H, 1] [∀x∃y(P(x) ∧ P(y)).]]",
            " [∃I] [∃y∀x Eq(y,x). [H, 1] [∀x Eq(x,x).]]",
            " [∀I] [∀x Q(x). [→E] [ Q(x). [H,2] [P(x).] [∀E] [P(x) → Q(x). [H,1] [ ∀y (P(y) → Q(y)).]]]]",
            " [∀I] [ ∀x P(x). [H,1] [P(y).]]",
            " [∃I] [∃y ∀x (P(x) ∧ P(y)). [∀I] [∀x (P(x) ∧ P(y)). [∀E] [P(y) ∧ P(y). [H,1] [ ∀y (P(y) ∧ P(y)).]]]]",
            " [∀I] [ ∀x Par(sq(x)). [∃E, 3] [ Par(sq(x)). [H,1] [∃x Par(x).] [→E] [ Par(sq(x)). [H,3] [Par(x).] [∀E] [ Par(x) → Par(sq(x)). [H,2] [∀x (Par(x) → Par(sq(x))).]]]]]",
            " [∃E,3] [ ∃x (P(x) ∧Q(x)). [∃I] [ ∃x P(x). [H,1] [P(a).]] [∃I] [ ∃x (P(x) ∧Q(x)). [∧I] [ P(x) ∧Q(x). [H,3] [P(x).] [H,2] [Q(x).]]]]",
            " [∃E,2] [∃z(Par(z) ∧ P(z)). [H,1] [∃x (Par(x) ∧ P(y)).] [∃I] [∃z (Par(z) ∧ P(z)). [H,2] [Par(y) ∧ P(y).]]]",
            " [∀I] [∀x ∃y φ. [∃I] [∃y φ. [∀E] [φ. [∃E,1] [∀x φ. [H,2] [∃y ∀x φ.] [H,3] [∀x φ.]]]]]",
            " [∀E] [φ. [∃E,1] [∀x φ. [H,2] [∃y ∀x φ.] [H,1] [∀x φ.]]]"
    })
    void testFail(String proof) {
        Throwable thrown = Assertions.assertThrows(Throwable.class, () -> {
            LogicAPI.parseNDFOLProof(proof);
        });
        System.out.println(Utils.getToken(thrown.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " [∃E,2] [∃z(Par(z) ∧ P(z)). [H,1] [∃x (Par(x) ∧ P(y)).] [∃I] [∃z (Par(z) ∧ P(z)). [H,2] [Par(y) ∧ P(y).]]]",
    })
    void testFailSingle1(String proof) {
        Throwable thrown = Assertions.assertThrows(Throwable.class, () -> {
            LogicAPI.parseNDFOLProof(proof);
        });
        System.out.println(Utils.getToken(thrown.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " [→E] [Q(a).[H,2] [P(a).][∀E] [P(a) → Q(a).[H,1] [∀x (P(x) → Q(x)).]]]",
            " [∃I] [∃x Q(x). [→E] [Q(a). [H,2] [P(a).] [∀E] [P(a) → Q(a). [H,1] [∀x (P(x) → Q(x)).]]]]",
            " [∀I] [∀x Q(x). [→E] [ Q(x). [∀E] [P(x). [H,2] [∀y P(y).]] [∀E] [P(x) → Q(x). [H,1] [ ∀y (P(y) → Q(y)).]]]]",
            " [∀I] [∀x P(x). [⊥, 3] [P(x). [¬E] [⊥. [H,2] [ ¬∃x¬P(x).] [∃I] [ ∃x ¬P(x). [H,3] [¬P(x).]]]]]",
            " [∀I] [∀y ∀x P(y,x). [∀I] [∀x P(z,x). [∀E] [ P(z,x). [∀E] [ ∀y P(z,y). [H,1] [ ∀x ∀y P(x,y).]]]]]",
            " [∃E,2] [∀x ∃y φ. [H,1] [∃y ∀x φ.] [∀I] [∀x ∃y φ. [∃I] [∃y φ. [∀E] [φ. [H,2] [∀x φ.]]]]]",
            " [∃E,2] [∀x ∃y φ. [H,1] [∃y ∀x φ.] [∀I] [∀x ∃y φ. [∃I] [∃y φ. [∀E] [φ. [H,2] [∀x φ.]]]]]",
            " [→I,2] [¬∃x P(x) → ∀x ¬P(x). [∀I] [∀x ¬P(x). [¬I,1] [¬P(x). [¬E] [⊥. [H,2] [¬∃x P(x).] [∃I] [∃x P(x). [H,1] [P(x).]]]]]]",
            " [→I,2] [∀x φ → ¬∃x ¬φ. [¬I,1] [¬∃x ¬φ. [∃E, 3] [⊥. [H,1] [∃x ¬φ.] [¬E] [⊥. [H,3] [¬φ.] [∀E] [φ. [H,2] [∀x φ.]]]]]]",
            " [→I,22] [∀x φ → ¬∃x ¬φ. [¬I,11] [¬∃x ¬φ. [∃E, 3] [⊥. [H,10] [∃x ¬φ.] [¬E] [⊥. [H,3] [¬φ.] [∀E] [φ. [H,20] [∀x φ.]]]]]]",
            " [→I,] [∀x φ → ¬∃x ¬φ. [¬I,] [¬∃x ¬φ. [∃E, 3] [⊥. [H,10] [∃x ¬φ.] [¬E] [⊥. [H,3] [¬φ.] [∀E] [φ. [H,20] [∀x φ.]]]]]]",
            " [∃E, 4] [∃x D(x). [H, 2] [∃x ¬L(x).] [∨E, 5, 6] [∃x D(x). [∀E] [C(x) ∨ D(x). [H, 1] [∀y (C(y) ∨ D(y)).]] [⊥, 7] [∃x D(x). [¬E] [⊥. [H, 4] [¬L(x).] [→E] [L(x). [H, 5] [C(x).] [∀E] [C(x) → L(x). [H, 3] [∀x (C(x) → L(x)).]]]]] [∃I] [∃x D(x). [H, 6] [D(x).]]]]",
    })
    void testSuccess(String proof) {
        Assertions.assertDoesNotThrow(() -> {
            INDProof ndProof = LogicAPI.parseNDFOLProof(proof);

            System.out.print("{");
            ndProof.getPremises().forEachRemaining(i -> System.out.print(Utils.getToken(i + ".")));
            System.out.println("} |= " + ndProof.getConclusion());
        });
    }


    @ParameterizedTest
    @ValueSource(strings = {
            /*1*/"∀x P(x) ∨ ∀x Q(x). ∀x (P(x) ∨ Q(x))",
            /*2*/"∀x (P(x) ∧ Q(x)). ∀x P(x) ∧ ∀x Q(x)",
            /*3*/"(∀x P(x) ∧ ∀x Q(x)) → ∀x (P(x) ∧ Q(x))",
            /*4*/"∃x (P(x) ∧ Q(x)). ∃x P(x) ∧ ∃x Q(x)",
            /*5*/"(∃x P(x) ∨ ∃x Q(x)) → ∃x (P(x) ∨ Q(x))",
            /*6*/"∃x (P(x) ∨ Q(x)). ∃x P(x) ∨ ∃x Q(x)",
            /*7*/"∀x (P(x) → Q(x)). ∀x P(x) → ∀x Q(x)",
            /*8*/"∃y∀x φ. ∀x∃y φ",
            /*9*/"∃x ¬P(x) → ¬∀x P(x)",
            /*10*/"¬∀x P(x) → ∃x ¬P(x)",
            /*11*/"∀x ¬P(x) → ¬∃x P(x)",
            /*12*/"¬∃x P(x) → ∀x ¬P(x)",
            /*13*/"∃x φ → ¬∀x ¬φ",
            /*14*/"¬∀x ¬φ. ∃x φ",
            /*15*/"∀x φ → ¬∃x ¬φ",
            /*17*///"((∀x φ ∧ ψ) → ∀x (φ ∧ ψ)) ∧ (∀x (φ ∧ ψ) → (∀x φ ∧ ψ))", //Only works if we add x in the set of notfree
            /*23*///"(∀x (φ → ψ) → (∃x φ → ψ)) ∧ ((∃x φ → ψ) → ∀x (φ → ψ))", //Only works if we add x in the set of notfree

            /*2*///"∀y(C(y) ∨ D(y)). ∀x(C(x) → L(x)). ∃x¬L(x). ∃x D(x)",
            /*3*/"∀x(C(x) → S(x)). ∀x(¬A(x,b) → ¬S(x)). ∀x((C(x)∨S(x)) → A(x,b))",
            /*4*/"L(a,b). ∀x(∃y(L(y,x) ∨ L(x,y)) → L(x,x)). ∃x L(x,a)",
            /*5*///"∀x ∀y (L(x,y) → L(y,x)). ∃x ∀y L(x,y). ∀x ∃y L(x,y)", //Requires aux variables

            //Others
            //"∀x∀y P(x,y). ∀y∀x P(y,x)" //Require an aux variable z
    })
    void testAlgorithm(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises)
                            .addTerm(new ASTVariable("w")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    .setTotalClosedNodes(10000)
                                    .setHypothesesPerGoal(4)
                                    .setTimeout(1000)
                                    .setTrimStrategy(new HeightTrimStrategy()))
                    //.addTerm(new ASTVariable("z"))
                    //.addForbiddenRule(ERule.ELIM_NEGATION)
                    .build();

            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);

            Set<IFormula> premisesProof = new HashSet<>();
            proof.getPremises().forEachRemaining(premisesProof::add);
            System.out.println(Utils.getToken(premisesProof + ": " + premises));
            Assertions.assertEquals(premises, premisesProof);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "P(a) → ∃x Q(x). ∃x(P(a) → Q(x))",
    })
    void testSingleSimpleAlgorithm(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises)
                            .addTerm(new ASTVariable("b"))
                            .addTerm(new ASTVariable("c")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    .setTrimStrategy(new HeightTrimStrategy())
                                    .setTotalClosedNodes(20000)
                                    .setHypothesesPerGoal(5)
                                    .setHeightLimit(50)
                                    //.setInitialState(
                                    //        new AlgoProofStateBuilder(LogicAPI.parseFOL("P(a) → Q(b)"))
                                    //                .addHypothesis(LogicAPI.parseFOL("Q(b)")))
                                    .setTimeout(2000))
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
            "∃y∀x φ. ∀x∃y φ",
    })
    void testSingleAlgorithm(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }

        Assertions.assertThrows(Throwable.class, () -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises))
                    .setGoal(new AlgoProofFOLGoalBuilder(LogicAPI.parseFOL("∀x φ")))
                    .setAlgoSettingsBuilder(new AlgoSettingsBuilder().setTimeout(200))
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
            "∀x(P(a) → Q(x)). P(a) → ∀z Q(z)",
    })
    void testAlgorithmWithNoExtra(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    .setTimeout(1000))
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
            "∀x ∀y (L(x,y) → L(y,x)). ∃x ∀y L(x,y). ∀x ∃y L(x,y)"
    })
    void testAlgorithmSingle(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }
        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises)
                            .addTerm(new ASTVariable("w"))
                            .addTerm(new ASTVariable("z")))
                    .setGoal(new AlgoProofFOLGoalBuilder(LogicAPI.parseFOL("∃y L(w,y)"))
                            .addHypothesis(LogicAPI.parseFOL("∀y L(z,y)")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    //.setBuildStrategy(new LinearBuildStrategy())
                                    .setTotalClosedNodes(10000)
                                    .setHypothesesPerGoal(5)
                                    .setTimeout(100)
                                    .setTrimStrategy(new HeightTrimStrategy())
                    )
                    //.addForbiddenRule(ERule.ELIM_NEGATION)
                    .build();

            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);


        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "¬∃x P(x). ∀x ¬P(x)",
            "∃x ¬P(x). ¬∀x P(x)",
            "¬∀x P(x). ∃x ¬P(x)",
            "∀x ¬P(x). ¬∃x P(x)",
            "∀x(∃y P(y) → Q(x)). ∀x ∃y(P(y) → Q(x))",
            "∀x ¬∀y (P(x,y) → Q(x,y)). ∀x ∃y P(x,y)",
            "∀x (P(x,x) ∨ ∀y Q(x,y)). ∀x(∃y P(x,y) ∨ Q(x,x))",
            "∀y(C(y) ∨ D(y)). ∀x(C(x) → L(x)). ∃x¬L(x). ∃x D(x)",
            "∀x ∀y (L(x,y) → L(y,x)). ∃x ∀y L(x,y). ∀x ∃y L(x,y)"
    })
    void testAlgorithmWithNoExtra1(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises)
                            .addTerm(new ASTVariable("a")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    .setTimeout(2000)
                                    .setTotalClosedNodes(4000))
                    //.addTerm(new ASTVariable("b"))
                    //.addForbiddenRule(ERule.ABSURDITY)
                    //.addForbiddenRule(ERule.ELIM_EXISTENTIAL)
                    //.addForbiddenRule(ERule.ELIM_NEGATION)
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
            "∀x ∀y P(x,y). ∀y ∀x P(y,x)"
    })
    void testAlgorithmWithNoExtra2(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises)
                            .addTerm(new ASTVariable("z")))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    .setTimeout(2000)
                                    .setTotalClosedNodes(4000))
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
            "¬∃x P(x). ∀x ¬P(x)",
            "∃x ¬P(x). ¬∀x P(x)",
            "¬∀x P(x). ∃x ¬P(x)",
            "∀x ¬P(x). ¬∃x P(x)",
            "∀x (∃y P(y) → Q(x)). ∀x ∃y (P(y) → Q(x))",
            "∀x ¬∀y (P(x,y) → Q(x,y)). ∀x ∃y P(x,y)",
            "∀x (P(x,x) ∨ ∀y Q(x,y)). ∀x(∃y P(x,y) ∨ Q(x,x))"
    })
    void testAlgorithmWithNoExtra3(String premisesAndExpression) throws Exception {
        String[] parts = premisesAndExpression.split("\\.");
        String expression = parts[parts.length - 1].trim();

        Set<IFOLFormula> premises = new HashSet<>();
        for (int i = 0; i < parts.length - 1; i++) {
            premises.add(LogicAPI.parseFOL(parts[i].trim()));
        }

        Assertions.assertDoesNotThrow(() -> {
            INDProof proof = new AlgoProofFOLBuilder(
                    new AlgoProofFOLMainGoalBuilder(LogicAPI.parseFOL(expression))
                            .addPremises(premises))
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    .setTimeout(25000)
                                    .setTotalClosedNodes(10000))
                    //.addTerm(new ASTVariable("a"))
                    //.addTerm(new ASTVariable("b"))
                    .build();

            System.out.println("Size: " + proof.size() + " Height: " + proof.height());
            System.out.println(proof);

            Set<IFormula> premisesProof = new HashSet<>();
            proof.getPremises().forEachRemaining(premisesProof::add);
            Assertions.assertEquals(premises, premisesProof);
        });
    }

}
