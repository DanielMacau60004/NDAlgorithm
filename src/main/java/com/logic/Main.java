package com.logic;

import com.logic.api.IFOLFormula;
import com.logic.api.INDProof;
import com.logic.api.IPLFormula;
import com.logic.api.LogicAPI;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.nd.algorithm.*;
import com.logic.nd.algorithm.proofs.strategies.SizeTrimStrategy;

public class Main {

    // Helper method to parse a propositional logic formula
    private static IPLFormula pl(String formula) {
        return LogicAPI.parsePL(formula);
    }

    // Helper method to parse a first-order logic formula
    private static IFOLFormula fol(String formula) {
        return LogicAPI.parseFOL(formula);
    }

    /*
     * This class demonstrates how to use the LogicAPI to automatically generate
     * human-readable proofs for both propositional and first-order logic.
     *
     * Each example below showcases how to configure the API, define the premises
     * and goals, and run the proof search using the provided algorithms.
     *
     * For more usage examples, see the test suite under: tests/nd
     */
    public static void main(String[] args) throws Exception {

        /*
         * Example 1 — Propositional logic proof:
         * Try to prove the formula: a
         * From the premises:
         *   ¬p → q
         *   r ∨ ¬q
         *   p → (a ∨ b)
         *   ¬r ∧ ¬b
         */
        INDProof proof = new AlgoProofPLBuilder(
                new AlgoProofPLMainGoalBuilder(pl("a"))
                        .addPremise(pl("¬p → q"))
                        .addPremise(pl("r ∨ ¬q"))
                        .addPremise(pl("p → (a ∨ b)"))
                        .addPremise(pl("¬r ∧ ¬b"))
        )
                .setAlgoSettingsBuilder(new AlgoSettingsBuilder()
                                .setTrimStrategy(new SizeTrimStrategy()) // minimizes proof size
                                .setTimeout(200)                         // timeout in milliseconds
                                .setTotalNodes(5000)                     // max nodes explored during search
                        // .setHeightLimit(6)                    // optional: limit proof height
                        // .setHypothesesPerGoal(5)              // optional: limit hypotheses per subgoal
                ).build();

        System.out.println("Example 1: \n" + proof);


        /*
         * Example 2 — Propositional logic with an additional hypothesis:
         * Same premises as before, but this time the student already assumes 'b' is known.
         * We attempt to prove 'a' from the updated state.
         */
        proof = new AlgoProofPLBuilder(
                new AlgoProofPLMainGoalBuilder(pl("a"))
                        .addPremise(pl("¬p → q"))
                        .addPremise(pl("r ∨ ¬q"))
                        .addPremise(pl("p → (a ∨ b)"))
                        .addPremise(pl("¬r ∧ ¬b"))
        )
                .setGoal(new AlgoProofPLGoalBuilder(pl("a"))
                        .addHypothesis(pl("b")) // additional known fact
                ).build();

        System.out.println("Example 2: \n" + proof);


        /*
         * Example 3 — First-order logic proof:
         * Try to prove: ∀x ∃y L(x,y)
         * From the premises:
         *   ∀x ∀y (L(x,y) → L(y,x))
         *   ∃x ∀y L(x,y)
         *
         * An auxiliary term (z) is provided to assist with the instantiation during proof search.
         */
        proof = new AlgoProofFOLBuilder(
                new AlgoProofFOLMainGoalBuilder(fol("∀x ∃y L(x,y)"))
                        .addPremise(fol("∀x ∀y (L(x,y) → L(y,x))"))
                        .addPremise(fol("∃x ∀y L(x,y)"))
                        .addTerm(new ASTVariable("z")) // custom instantiation term
        ).build();

        System.out.println("Example 3: \n" + proof);


        /*
         * Example 4 — First-order logic with user-defined intermediate state:
         * Similar to Example 3, but the student has already applied some rules manually
         * and is trying to prove ∃y L(x,y) from:
         *   ∀y L(a,y)
         *
         * The system continues the proof from this partially derived state.
         */
        proof = new AlgoProofFOLBuilder(
                new AlgoProofFOLMainGoalBuilder(fol("∀x ∃y L(x,y)"))
                        .addPremise(fol("∀x ∀y (L(x,y) → L(y,x))"))
                        .addPremise(fol("∃x ∀y L(x,y)"))
                        .addTerm(new ASTVariable("z")) // auxiliary term
        )
                .setGoal(new AlgoProofFOLGoalBuilder(fol("∃y L(x,y)"))
                        .addHypothesis(fol("∀y L(a,y)")) // pre-derived hypothesis from student
                ).build();

        System.out.println("Example 4: \n" + proof);
    }
}
